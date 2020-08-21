/**
 * FileName:JsonUtils
 * Author:leeyf
 * Date:19-1-20 下午8:22
 * Description:对象-JSON转换类
 */
package com.yundingshuyuan.website.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.exception.SysException;

public class JsonUtils {
    /**
     * 对象转化为JSON
     * @param o
     * @return
     */
    public static String toJson(Object o){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            //如果是String类就返回toString,否则就转化成String返回
            return o instanceof String ? o.toString() : objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SysException(ErrorCodeEnum.JSON_TRANS_ERROR);
        }
    }

    /**
     * JSON转化为对象
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static<T> T toObject(String json, Class<T> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return tClass.equals(String.class) ? (T) json :objectMapper.readValue(json,tClass);
        }catch (Exception e){
            e.printStackTrace();
            throw new SysException(ErrorCodeEnum.JSON_TRANS_ERROR);
        }
    }
}
