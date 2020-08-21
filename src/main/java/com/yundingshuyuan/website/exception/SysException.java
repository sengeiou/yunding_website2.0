/**
 * FileName:SysException
 * Author:leeyf
 * Date:19-1-20 上午9:27
 * Description:系统异常
 */
package com.yundingshuyuan.website.exception;

import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import lombok.Data;

@Data
public class SysException extends RuntimeException {
    private Integer code;

    public SysException(Integer code, String msg){
        super(msg);
        this.code = code;
    }

    public SysException(ErrorCodeEnum errorCodeEnum){
        super((String) errorCodeEnum.getMsg());
        this.code = errorCodeEnum.getCode();
    }

    public SysException(){

    }
}
