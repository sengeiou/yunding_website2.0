package com.yundingshuyuan.website.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    private Long total;

    private Long currentPage;

    private Long size;

    private Long pageCounts;

    private List<T> commentList;

}
