package com.xawl.service.edu.entity.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectDTO {

    //设置列对应的属性
    @ExcelProperty(index = 0)
    private String oneSub;

    @ExcelProperty(index = 1)
    private String toSub;
}
