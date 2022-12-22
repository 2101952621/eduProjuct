package com.xawl.service.edu.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("edu_class")
@ApiModel(value="Class对象", description="用户基本信息")
public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "班级类型")
    private String classType;

    @ApiModelProperty(value = "班级人数")
    private Integer classNumber;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班主任id")
    private String classTeacherId;

    @ApiModelProperty(value = "班级头像")
    private String headImage;

    @ApiModelProperty(value = "班级积分")
    private Integer integral;

    @ApiModelProperty(value = "班级简介")
    private String intro;

    @ApiModelProperty(value = "状态（0：锁定 1：正常）")
    private Integer status;


    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "班主任姓名")
    @TableField(exist = false)
    private String teacherName;

    @ApiModelProperty(value = "班级课程信息")
    @TableField(exist = false)
    private List<Course> courseList;


}
