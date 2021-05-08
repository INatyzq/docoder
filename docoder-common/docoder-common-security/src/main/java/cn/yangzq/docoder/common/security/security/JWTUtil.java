package cn.yangzq.docoder.common.security.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

/**
*@author yangzq
*@description  jwt工具类
**/
public class JWTUtil {

    private static final String SECRET_KEY = "DOCODER";
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";

    /**
     * author:yangzq
     * desc: 校验token是否正确
     * params:
     */
    public static boolean verify(String token, String userId, String username) {
        Boolean result = true;
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(userId + SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(USER_ID, userId).withClaim(USER_NAME, username).build();
            //效验TOKEN
            verifier.verify(token);
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            return result;
        }
    }


    /**
     * author:yangzq
     * desc: 获得token中的获取userId
     * params:
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USER_ID).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * author:yangzq
     * desc: 获得token中的获取userName
     * params:
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USER_NAME).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * auth yangzq
     * desc 生成签名,给定时间后过期
     * params:
     */
    public static String sign(String userId, String userName,Long expireSecond) {
        Algorithm algorithm = Algorithm.HMAC256(userId + SECRET_KEY);
        if (expireSecond == null) {
            expireSecond = 24 * 60 * 60L;
        }
        Date expireDate = new Date(System.currentTimeMillis() + expireSecond * 1000);
        // 附带user信息
        return JWT.create()
                .withClaim(USER_ID, userId)
                .withClaim(USER_NAME, userName)
                .withExpiresAt(expireDate)
                .sign(algorithm);

    }
}
