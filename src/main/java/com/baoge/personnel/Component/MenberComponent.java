package com.baoge.personnel.Component;

import com.baoge.data.PO.ProductList;
import com.baoge.personnel.Dao.MenberDao;
import com.baoge.personnel.PO.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component("menberComponent")
public class MenberComponent {

    @Autowired
    private MenberDao menberDao;

    public Boolean decideMenber(int uid) {
        Boolean isEnd = false;
        int count = menberDao.countByUidAndExpiryDateGreaterThan(uid,new Date());
        if(count>0){
            isEnd = true;
        }
        return isEnd;
    }

    public void increaseMemberTime(int uid, ProductList productList) throws Exception{
        MemberEntity memberEntity = null;
        memberEntity = menberDao.queryByUid(uid);
        Date currentTime = new Date();
        if(memberEntity == null || memberEntity.getMid() == 0){
            memberEntity = new MemberEntity();
            memberEntity.setUid(uid);
            memberEntity.setExpiryDate(currentTime);
        }else if(currentTime.compareTo(memberEntity.getExpiryDate()) == 1){
            memberEntity.setExpiryDate(currentTime);
        }
        Integer year = Integer.valueOf(productList.getRemarks());
        Calendar cal = Calendar.getInstance();
        cal.setTime(memberEntity.getExpiryDate());
        cal.add(Calendar.YEAR, year);
        memberEntity.setExpiryDate(cal.getTime());
        memberEntity.setCreateTime(new Date());
        menberDao.save(memberEntity);
    }
}
