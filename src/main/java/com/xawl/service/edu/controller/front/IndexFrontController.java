package com.xawl.service.edu.controller.front;

import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.service.CourseService;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.Teacher;
import com.xawl.service.edu.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/edu/index/front")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    CourseService courseService;


    @Autowired
    TeacherService teacherService;

    @ApiOperation("查询热门课程和热门讲师")
    @GetMapping("/get")
    public R getIndex(){
        List<Course> courseList =  courseService.getCourseIndex();
        List<Teacher> teacherList =  teacherService.getTeacherIndex();
        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }



}
