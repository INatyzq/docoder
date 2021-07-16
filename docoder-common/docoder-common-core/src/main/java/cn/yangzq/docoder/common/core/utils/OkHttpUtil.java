package cn.yangzq.docoder.common.core.utils;

import cn.hutool.json.JSONUtil;
import cn.yangzq.docoder.common.core.exception.SystemException;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * okhttp 工具类
 */
public class OkHttpUtil {

    private OkHttpUtil(){}

    private static final MediaType CONTENT_TYPE_JSON = MediaType.get("application/json");

    /**
     * post请求
     */
    public static <T> T postForEntity(String url,Map<String,String> headers,String bodyJsonStr,Class<T> tClass){
        Request post = getPost(url, headers,bodyJsonStr);
        return parseBody(post,tClass);
    }

    /**
     * post请求
     */
    public static String post(String url,Map<String,String> headers,String bodyJsonStr){
        Request post = getPost(url,headers, bodyJsonStr);
        return parseBody(post);
    }

    /**
     * delete请求
     */
    public static String delete(String url,Map<String,String> headers){
        Request post = getDelete(url,headers);
        return parseBody(post);
    }

    private static Request getPost(String url, Map<String,String> headers, String bodyJsonStr){
        RequestBody requestBody = FormBody.create(CONTENT_TYPE_JSON, bodyJsonStr);
        Request.Builder post = new Request.Builder().url(url).post(requestBody);
        if(headers!=null && headers.size()>0){
            headers.forEach((k,v)->{
                post.addHeader(k,v);
            });
        }
        return post.build();
    }

    private static Request getDelete(String url, Map<String,String> headers){
        Request.Builder delete = new Request.Builder().url(url).delete();
        if(headers!=null && headers.size()>0){
            headers.forEach((k,v)->{
                delete.addHeader(k,v);
            });
        }
        return delete.build();
    }

    private static String parseBody(Request request){
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new SystemException("okhttp调用失败："+request.url().toString());
        }
        int code = response.code();
        if(code==200){
            ResponseBody body = response.body();
            if(body!=null){
                String bodyStr = null;
                try {
                    bodyStr = body.string();
                } catch (IOException e) {
                    return null;
                }
                return bodyStr;
            }
        }else{
            String msg = null;
            ResponseBody body = response.body();
            if(body!=null){
                try {
                    msg = body.string();
                } catch (IOException e) {
                    msg = response.message();
                }
            }
            throw new SystemException("okhttp响应异常：{"+code+":"+msg+"}");
        }
        return null;
    }

    private static <T> T parseBody(Request request,Class<T> tClass){
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        Response response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new SystemException("okhttp调用失败："+request.url().toString());
        }
        int code = response.code();
        if(code==200){
            ResponseBody body = response.body();
            if(body!=null){
                String bodyStr = null;
                try {
                    bodyStr = body.string();
                } catch (IOException e) {
                    return null;
                }
                return JSONUtil.toBean(bodyStr,tClass);
            }
        }else{
            throw new SystemException("okhttp响应异常：{"+code+":"+response.message());
        }
        return null;
    }

}
