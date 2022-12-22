package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Class;
import com.xawl.service.edu.entity.query.ClassQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ClassService extends IService<Class> {

    IPage<Class> pageByCondition(Long pageSize, Long pageInfo, ClassQuery classQuery);

    String saveClassInfo(Class classInfo);


    Integer getCountById(String classId);

    Map<String, Object> getCourseFront(Page<Class> page);

    boolean stuJoinClass(String id, String classId);

    boolean stuExitClass(String number, String classId);

    boolean getClassInfoByTeacherId(String id);

    void updateByTeacherId(String id);

    boolean removeClassById(String id);
}
