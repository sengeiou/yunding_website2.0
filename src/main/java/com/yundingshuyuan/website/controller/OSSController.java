package com.yundingshuyuan.website.controller;

import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.util.OSSUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/oss")
public class OSSController {

    /**
     * 上传文件
     * @param file 文件
     * @return ServerResponse
     */
    @ApiOperation("通用上传文件接口")
    @PostMapping("/uploadFile")
    public ServerResponse<Object> uploadFile(MultipartFile file,HttpServletRequest request){


        try {


            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件后缀
            assert fileName != null;
            String prefix=fileName.substring(fileName.lastIndexOf("."));
            // 用uuid作为文件名，防止生成的临时文件重复
            final File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);

            file.transferTo(excelFile);

            String url = OSSUtil.upload(excelFile,request);

            //程序结束时，删除临时文件
            deleteFile(excelFile);

            return ServerResponse.createBySuccess(url);

        } catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IMAGE_UPLOAD_ERROR);
        }

    }
    @ApiOperation("通用图片上传接口")
    @PostMapping("/uploadPic")
    public ServerResponse uploadPic(MultipartFile file) throws IOException {
        String url= OSSUtil.uploadMultipartFile(file);
        return ServerResponse.createBySuccess(url);
    }


    /**
     * 删除
     * @param files 文件们
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.deleteOnExit();
            }
        }
    }

    /**
     * 获取实时长传进度
     * @param request
     * @return
     */
    @GetMapping("/item/percent")
    @ResponseBody
    public int getUploadPercent(HttpServletRequest request){
        HttpSession session = request.getSession();
        int percent = session.getAttribute("upload_percent") == null ? 0: (int) session.getAttribute("upload_percent");
        return percent;
    }

    /**
     * 重置上传进度
     * @param request
     * @return
     */
    @GetMapping ("/percent/reset")
    public void resetPercent(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("upload_percent",0);
    }


}
