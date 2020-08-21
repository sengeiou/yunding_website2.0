package com.yundingshuyuan.website.util;

import cn.hutool.crypto.SecureUtil;
import com.yundingshuyuan.website.constants.SysConstant;

/**
 * @author:leeyf
 * @create: 2019-02-22 12:31
 * @Description:
 */
public class Md5PswdUtil {

    public static String md5Pswd(String password){
        //password = String.format("#%s",password);
        password = String.format(SysConstant.PZ,password);
        password = SecureUtil.md5(password);
        return password;
    }


}