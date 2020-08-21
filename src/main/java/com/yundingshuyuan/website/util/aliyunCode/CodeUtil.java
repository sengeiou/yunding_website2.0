/**
 * FileName:CodeUtil
 * Author:leeyf
 * Date:19-1-20 下午2:42
 * Description:手机验证工具类
 */
package com.yundingshuyuan.website.util.aliyunCode;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtil {
    /**
     * 判断是否为手机号
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        System.out.println(phone);
        String regex = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    /**
     * 判断是否为邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        System.out.println(email);
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(email);
            boolean isMatch = m.matches();
            return isMatch;
    }

    /**
     * 随机生成手机验证码(6位)
     * @return
     */
    public static String smsCode() {
        Random random = new Random();
        int codeNum = random.nextInt(900000) + 100000;
        return String.valueOf(codeNum);
    }
}
