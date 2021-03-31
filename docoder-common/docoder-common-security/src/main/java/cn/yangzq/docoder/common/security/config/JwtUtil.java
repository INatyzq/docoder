package cn.yangzq.docoder.common.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
*@author yangzq
*@description jwt工具类
**/
public class JwtUtil {

    /**
     * author:yangzq
     * desc: 校验token是否正确
     * params:
     */
    public static boolean verify(String token, String account, String userPsw) {
        boolean result = true;
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(userPsw + SecurityConst.SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(SecurityConst.ACCOUNT, account).build();
            //效验TOKEN
            verifier.verify(token);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * author:yangzq
     * desc: 获得token中的信息无需psw解密也能获得
     * params:
     */
    public static String getAccountId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SecurityConst.ACCOUNT_ID).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SecurityConst.ACCOUNT).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * author:yangzq
     * desc: 获得token中的信息无需psw解密也能获得
     * params:
     */
    public static boolean isRememberMe(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Boolean.parseBoolean(jwt.getClaim(SecurityConst.LOGIN_REMEMBER_ME).asString());
        } catch (JWTDecodeException e) {
            return false;
        }
    }


    /**
     * auth yangzq
     * desc 生成签名,给定时间后过期
     * params:
     */
    public static String sign(String accountId, String account, String userPsw, boolean isRememberMe) {
        Date expireTime = new Date(System.currentTimeMillis() + SecurityConst.EXPIRE_TIME_JWT);
        Algorithm algorithm = Algorithm.HMAC256(userPsw + SecurityConst.SECRET_KEY);
        // 附带user信息
        return JWT.create()
                .withClaim("1", "2")
                .withClaim(SecurityConst.ACCOUNT_ID, accountId)
                .withClaim(SecurityConst.ACCOUNT, account)
                .withClaim(SecurityConst.LOGIN_REMEMBER_ME, isRememberMe)
                //.withExpiresAt(expireTime)
                .sign(algorithm);

    }

}
