package com.xawl.service.edu.entity.frontvo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClassWebVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论内容")
    private String content;

}
