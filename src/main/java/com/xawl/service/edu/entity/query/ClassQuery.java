package com.xawl.service.edu.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClassQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班主任id")
    private String classTeacherId;

    @ApiModelProperty(value = "查询注册开始时间")
    private String begin;

    @ApiModelProperty(value = "查询注册结束时间")
    private String end;
}
