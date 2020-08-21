package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class AlbumFilePageForm {
    private String id;
    private String albumId;
    private String status;

    Integer pageNum;
    Integer pageSize;

}
