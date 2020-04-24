
package com.baoge.systemConfig.Filter;

import com.alibaba.fastjson.JSON;
import com.baoge.Util.Enums.ResultEnum;
import com.baoge.Util.PO.Result;
import com.baoge.Util.ResponseUtil;
import com.baoge.Util.JwtUtil;
import com.baoge.Util.ResultUtil;
import com.baoge.Util.SCException;
import com.baoge.personnel.Dao.AdministratorAccountDao;
import com.baoge.personnel.Dao.PersonalDao;
import com.baoge.personnel.PO.AdministratorAccount;
import com.baoge.personnel.PO.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;



@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private PersonalDao personalDao;

    @Autowired
    private AdministratorAccountDao administratorAccountDao;
/**
     * 预处理回调方法，实现处理器的预处理
     * 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或处理器
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        System.out.println(token + ": ???");
        Map<String,String> map = JwtUtil.verify(request,token);
        String judge = request.getParameter("identity");
        if(judge!=null&&judge.equals("identity")) return true;
        if (map == null){
            Result r = new Result();
            r.setCode(-2);
            r.setMsg("没有签名不能访问");
            r.setData(false);
            ResponseUtil.write(response, JSON.toJSONString(r));
            return false;
        }
        int count = 0;
        if(map.get("identity").equals("admin")){
            AdministratorAccount admin = administratorAccountDao.queryByAccountNumberAndUserPassword(map.get("account"),map.get("passWord"));
            if(admin != null && admin.getAid() != 0){
                if(admin.getIsEnd() == 2){
                    throw new SCException(ResultEnum.REFUSE_USER);
                }
                count = 1;
            }
        }else{
            UserPO user = personalDao.queryByUserPhoneAndUserPassword(map.get("account"),map.get("passWord"));
            if(user != null && user.getUid() != 0){
                if(user.getStatus() == 1){
                    throw new SCException(ResultEnum.REFUSE_USER);
                }
                count = 1;
            }
        }

        if (count < 1){
            Result r = new Result();
            r.setCode(-2);
            r.setMsg("用户密码错误或不存在");
            r.setData(false);
            ResponseUtil.write(response, JSON.toJSONString(r));
            return false;
        }

        return true;
    }


/**
     * 后处理回调方法，实现处理器（controller）的后处理，但在渲染视图之前
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理
     */

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
    }


/**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，
     * 但仅调用处理器执行链中
     */

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO Auto-generated method stub

    }

}
