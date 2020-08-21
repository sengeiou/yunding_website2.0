package com.yundingshuyuan.website.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConstantProperties implements InitializingBean {

    @Value("${java4all.file.endpoint}")
    private String java4all_file_endpoint;

    @Value("${java4all.file.keyid}")
    private String java4all_file_keyid;

    @Value("${java4all.file.keysecret}")
    private String java4all_file_keysecret;

    @Value("${java4all.file.filehost}")
    private String java4all_file_filehost;

    @Value("${java4all.file.bucketname}")
    private String java4all_file_bucketname;


    public static String JAVA4ALL_END_POINT         ;
    public static String JAVA4ALL_ACCESS_KEY_ID     ;
    public static String JAVA4ALL_ACCESS_KEY_SECRET ;
    public static String JAVA4ALL_BUCKET_NAME       ;
    public static String JAVA4ALL_FILE_HOST         ;
    public static String URL ="https://yundingweb.oss-cn-beijing.aliyuncs.com/"         ;
    @Override
    public void afterPropertiesSet() throws Exception {
        JAVA4ALL_END_POINT = java4all_file_endpoint;
        JAVA4ALL_ACCESS_KEY_ID = java4all_file_keyid;
        JAVA4ALL_ACCESS_KEY_SECRET = java4all_file_keysecret;
        JAVA4ALL_FILE_HOST = java4all_file_filehost;
        JAVA4ALL_BUCKET_NAME = java4all_file_bucketname;
    }
}