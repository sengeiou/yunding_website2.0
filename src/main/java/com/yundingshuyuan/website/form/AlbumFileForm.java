package com.yundingshuyuan.website.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlbumFileForm {
    private String id;
    @ApiModelProperty(required = true)
    private String albumId;

    private String introduce;

    private String name;

    private String status;

    private String url;
}
