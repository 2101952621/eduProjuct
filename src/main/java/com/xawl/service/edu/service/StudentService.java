package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Student;

import com.xawl.service.edu.entity.query.StudentQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StudentService extends IService<Student> {

    String studentLogin(String number, String password);

    boolean updateStuById(String studentId);

    List<Student> getListByClassId(Long pageSize, Long pageInfo, String classId);

    Page<Student> pageByCondition(Long pageSize, Long pageInfo, StudentQuery studentQuery);

    boolean saveInfo(Student student);

    Student getByNumber(String number);

    boolean removeStuById(String id);

    List<Student> getByClassId(String id);

    boolean updateStu(Student student);

    Integer getUserSta(String day);
}
