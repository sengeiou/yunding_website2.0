package com.yundingshuyuan.website.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

@Data
public class ScoreForm {
    @ApiModelProperty(required = true)
    String userId;
    Integer time;

}
