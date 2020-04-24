package com.baoge.Util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil{

    private static final long EXPIRE_TIME = 60*60*1000*24*356;
    private static final String TOKEN_SECRET = "n61e825d105445m3l258u43q2n2w4z537";


/**
 * 生成签名,EXPIRE_TIME后过期
 * @param userName 用户名
 * @return 加密的token
 */
    public static String sign(String userName,String account,String userId,String passWord,String identity){

        try{
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //附带username,userId信息,生成签名
            return JWT.create().withHeader(header).withClaim("account",account)
                    .withClaim("userId",userId)
                    .withClaim("userName",userName)
                    .withClaim("passWord",passWord)
                    .withClaim("identity",identity)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            return null;
        }

    }



    public static Map<String,String> verify(HttpServletRequest request,String token){
        Map<String,String> map = new HashMap<>();
        try{
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            map.put("identity",jwt.getClaim("identity").asString());
            map.put("userName",jwt.getClaim("userName").asString());
            map.put("account",jwt.getClaim("account").asString());
            map.put("userId",jwt.getClaim("userId").asString());
            map.put("passWord",jwt.getClaim("passWord").asString());
            request.setAttribute("userId",map.get("userId"));
            request.setAttribute("userName",map.get("userName"));
            return map;
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return data
     */
    public static String getData(String token,String data){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(data).asString();
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 获取数据
     * @param token
     * @return
     */
    public static Integer getUserId(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asInt();
        }catch (Exception e){
            return null;
        }
    }
}
