package com.xawl.service.edu.mapper;

import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.frontvo.CourseWebVo;
import com.xawl.service.edu.entity.vo.CoursePublishVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;





public interface CourseMapper extends BaseMapper<Course> {
     CoursePublishVO getPublishCourseInfo(String courseId);

     CourseWebVo selectBaseCourse(String courseId);
}
