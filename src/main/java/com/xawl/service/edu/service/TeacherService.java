package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Teacher;
import com.xawl.service.edu.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface TeacherService extends IService<Teacher> {
    IPage<Teacher> pageByCondition(Long pageSize, Long pageInfo, TeacherQuery teacherQuery);

    List<Teacher> getTeacherIndex();

    Map<String, Object> seleteTeacherPage(Page<Teacher> page);

    boolean saveTeacher(Teacher teacher);

    String teacherLogin(String mobile, String password);

    boolean removeTeacherById(String id);
}
