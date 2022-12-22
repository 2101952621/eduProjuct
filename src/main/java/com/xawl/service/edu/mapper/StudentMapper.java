package com.xawl.service.edu.mapper;

import com.xawl.service.edu.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface StudentMapper extends BaseMapper<Student> {

    Student selectByNumber(String number);

    Integer selectUserByDay(String day);
}
