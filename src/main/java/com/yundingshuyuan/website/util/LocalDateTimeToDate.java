package com.yundingshuyuan.website.util;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * @author:leeyf
 * @create: 2019-04-02 20:31
 * @Description:
 */
public class LocalDateTimeToDate {

    public static StringBuilder date( LocalDateTime date) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date.getYear()+"-"+date.getMonth()+"-"+date.getDayOfMonth()+" "+date.getHour()+":"+date.getMinute()+":"+date.getSecond());
        return stringBuilder;
    }
}