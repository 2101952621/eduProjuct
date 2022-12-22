package com.xawl.service.edu.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "学生查询对象", description = "查询对象封装")
@Data
public class StudentQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生名称,模糊查询")
    private String name;

    @ApiModelProperty(value = "学生所属班级")
    private String classId;

    @ApiModelProperty(value = "查询注册开始时间")
    private String begin;

    @ApiModelProperty(value = "查询注册结束时间")
    private String end;
}