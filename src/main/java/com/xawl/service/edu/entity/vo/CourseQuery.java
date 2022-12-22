package com.xawl.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "课程查询对象", description = "查询对象封装")
@Data
public class CourseQuery {
    @ApiModelProperty(value = "课程名称")
    private String title;

    @ApiModelProperty(value = "课程转态")
    private String status;
}
