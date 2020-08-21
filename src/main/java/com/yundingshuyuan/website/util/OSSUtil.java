package com.yundingshuyuan.website.util;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.yundingshuyuan.website.config.OSSConstantProperties.*;

/**
 * @author:leeyf
 * @create: 2019-07-26 09:42
 * @Description: OSS工具类
 */
public class OSSUtil {
    /**
     * The uploading progress listener. Its progressChanged API is called by the SDK when there's an update.
     */
    static class PutObjectProgressListener implements ProgressListener {
        private HttpSession session;
        private long bytesWritten = 0;
        private long totalBytes = -1;
        private boolean succeed = false;
        private int percent = 0;

        //构造方法中加入session
        public PutObjectProgressListener() {
        }
        public PutObjectProgressListener(HttpSession mSession) {
            this.session = mSession;
            session.setAttribute("upload_percent", percent);
        }

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("Start to upload......");
                    break;

                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                    break;

                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                        //将进度percent放入session中
                        session.setAttribute("upload_percent", percent);

                        logger.info(bytes + " bytes have been written at this time, upload progress: " +
                                percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                    } else {
                        logger.info(bytes + " bytes have been written at this time, upload ratio: unknown" +
                                "(" + this.bytesWritten + "/...)");
                    }
                    break;

                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                    break;

                case TRANSFER_FAILED_EVENT:
                    System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                    break;

                default:
                    break;
            }
        }

        public boolean isSucceed() {
            return succeed;
        }
    }
    static Logger logger = LoggerFactory.getLogger(OSSUtil.class);
    //oss访问域名，在oss后台添加bucket之后 查看
    private static String endpoint = JAVA4ALL_END_POINT;
    // accessKeyId和accessKeySecret是OSS的访问密钥
    private static String accessKeyId = JAVA4ALL_ACCESS_KEY_ID;
    private static String accessKeySecret = JAVA4ALL_ACCESS_KEY_SECRET;
    //bucketName
    private static String bucketName = JAVA4ALL_BUCKET_NAME;
    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件
    private static String fileHost = JAVA4ALL_FILE_HOST;

    static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    static String dateStr = format.format(new Date());


    //文件上传
    public static String upload(File file, HttpServletRequest request) throws UnsupportedEncodingException {

        if(null == file){
            return null;
        }
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        //容器不存在，就创建
        if(! ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
        try {
            //ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file)).<PutObjectRequest>withProgressListener(new PutObjectProgressListener()));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);
            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file)
                    .<PutObjectRequest>withProgressListener(new PutObjectProgressListener(request.getSession())));
            if(null != result){
                logger.info("==========>OSS文件上传成功,OSS地址："+fileUrl);
                logger.info("==========>OSS文件上传成功,访问地址："+URL+fileUrl);
                return URL+fileUrl;
            }
        } catch (OSSException oe) {
            logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.Error Message: " + oe.getErrorCode()
                    + "Error Code:" + oe.getErrorCode() + "Request ID:" + oe.getRequestId() + "Host ID:" + oe.getHostId(), oe);
            throw new OSSException(oe.getErrorMessage(), oe);
        } catch (ClientException ce) {
            logger.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.Error Message:" + ce.getMessage(), ce);
            throw new ClientException(ce);
        } finally {
            //关闭
            ossClient.shutdown();
        }

        return null;
    }

    //文件上传
    public static String uploadMultipartFile(MultipartFile multipartFile) throws IOException {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);

        multipartFile.transferTo(excelFile);
        if(null == excelFile){
            return null;
        }
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        //容器不存在，就创建
        if(! ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
        //创建文件路径
        String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+multipartFile.getName());
        //上传文件
        PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, excelFile));
        //设置权限 这里是公开读
        ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);

        if(null != result){
            logger.info("==========>OSS文件上传成功,OSS地址："+fileUrl);
            logger.info("==========>OSS文件上传成功,访问地址："+URL+fileUrl);
            return URL+fileUrl;
        }
        //关闭
        ossClient.shutdown();
        return null;
    }

    public static void delete(String url){
        String[] strings= url.split(".com/");
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        ossClient.deleteObject(bucketName,strings[1]);
        logger.info("==========>OSS文件删除成功");

    }



}