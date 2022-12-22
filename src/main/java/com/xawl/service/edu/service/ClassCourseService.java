package com.xawl.service.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xawl.service.edu.entity.ClassCourseInfo;
import com.xawl.service.edu.entity.Course;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author javaCoder
 * @since 2022-02-12
 */
public interface ClassCourseService extends IService<ClassCourseInfo> {

    void saveClassCourse(String classId, List<Course> courseList);

    List<Course> getListByClassId(String classId);

    void removeByCourseId(String classId, String courseId);

    void addCourse(String classId, String courseId);
}
