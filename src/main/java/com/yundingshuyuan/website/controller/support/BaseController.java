package com.yundingshuyuan.website.controller.support;

import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.exception.SysException;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

public class BaseController {
    protected void validateParams(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new SysException(
                    ErrorCodeEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage()
            );
        }
    }

    /**
     * @author Mr.h
     * @param sourceList
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    protected <S, T> List<T> toVOList(List<S> sourceList, Class<T> targetClass) {

        return sourceList.stream()
                .map(source -> {
                    try {
                        T target = targetClass.getConstructor().newInstance();
                        BeanUtils.copyProperties(source, target);
                        return target;
                    } catch (Exception e) {
                        throw new RuntimeException("VO转换出错！");
                    }
                })
                .collect(Collectors.toList());

    }
}
