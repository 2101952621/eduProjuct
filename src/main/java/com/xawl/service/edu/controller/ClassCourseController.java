package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.service.ClassCourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author javaCoder
 * @since 2022-02-12
 */
@RestController
@CrossOrigin
@RequestMapping("/edu/class/course")
public class ClassCourseController {

    @Autowired
    ClassCourseService classCourseService;

    @ApiOperation("通过班级id获取班级所有课程信息")
    @GetMapping("/find/all/{id}")
    public R getAllListByClassId(
            @PathVariable("id") String classId
    ) {
        List<Course> list = classCourseService.getListByClassId(classId);
        return R.ok().data("items", list);
    }

    @ApiOperation("通过课程id删除班级信息")
    @DeleteMapping("/remove/class/base/by/{classId}/{courseId}")
    public R removeByCourseId(
            @PathVariable("classId") String classId,
            @PathVariable("courseId") String courseId
    ) {
        if (!StringUtils.isEmpty(classId) && !StringUtils.isEmpty(courseId)) {
            classCourseService.removeByCourseId(classId, courseId);
            return R.ok();
        }
        return R.error();

    }

    @ApiOperation("通过课程id删除班级信息")
    @PostMapping("/insert/class/base/by/{classId}/{courseId}")
    public R addCourseToClassInfo(
            @PathVariable("classId") String classId,
            @PathVariable("courseId") String courseId
    ) {
        classCourseService.addCourse(classId, courseId);
        return R.ok();

    }
}

