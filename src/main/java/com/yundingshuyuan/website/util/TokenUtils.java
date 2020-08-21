/**
 * FileName:TokenUtils
 * Author:leeyf
 * Date:19-1-20 下午6:00
 * Description:Token工具类
 */
package com.yundingshuyuan.website.util;

import java.util.Random;
import java.util.UUID;

public class TokenUtils {
    final static int RANDOM_RANGE = 3;

    final static int RAND_UPPER_CASE = 0;

    final static int RAND_LOWER_CASE = 1;

    final static String TOKEN_PREFIX = "YDSY";

    private final static Random randow = new Random();

    /**
     * 生成token
     * @return
     */
    public static String genToken(){
        String uuid = UUID.randomUUID().toString().replace("-"," ").toLowerCase();
        return randomUpperLowerCase(uuid);
    }

    /**
     * 随机生成
     * @param uuid
     * @return
     */
    public static String randomUpperLowerCase(String uuid){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TOKEN_PREFIX);
        for (int i=0;i< uuid.length();i++){
            int rand = randow.nextInt(RANDOM_RANGE);

            if(rand == RAND_UPPER_CASE){
                stringBuilder.append(String.valueOf(uuid.charAt(i)).toUpperCase());
            }else if(rand == RAND_LOWER_CASE){
                stringBuilder.append(String.valueOf(uuid.charAt(i)).toLowerCase());
            }
        }
        return stringBuilder.toString();
    }

}
