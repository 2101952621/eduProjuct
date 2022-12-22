package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.frontvo.CourseQueryVo;
import com.xawl.service.edu.entity.frontvo.CourseWebVo;
import com.xawl.service.edu.entity.vo.CourseInfoVO;
import com.xawl.service.edu.entity.vo.CoursePublishVO;
import com.xawl.service.edu.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface CourseService extends IService<Course> {

    String addCourseInfo(CourseInfoVO courseInfoVO);

    CourseInfoVO getCourseInfoById(String id);

    void updateCourse(CourseInfoVO course);

    CoursePublishVO getCoursePublishVo(String id);

    void removeCourse(String courseId);

    IPage<Course> pageByCondition(Long pageSize, Long pageInfo, CourseQuery teacherQuery);

    List<Course> getCourseIndex();

    Map<String, Object> getCourseFront(Page<Course> pageCourse, CourseQueryVo courseQueryVo);


    CourseWebVo getBaseCourseById(String courseId);
}
