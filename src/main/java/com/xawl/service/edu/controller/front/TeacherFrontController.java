package com.xawl.service.edu.controller.front;

import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.service.CourseService;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.Teacher;
import com.xawl.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Api(description = "前台教师显示")
@RestController
@RequestMapping("/edu/teacher/front")
@CrossOrigin
public class TeacherFrontController {

    @Resource
    TeacherService teacherService;

    @Autowired
    CourseService courseService;

    @ApiOperation("前台查询分页")
    @GetMapping("/get/page/{page}/{size}")
    public R getTeacherPageList(
            @PathVariable("page") Long page,
            @PathVariable("size") Long size
    ) {
        Page<Teacher> pageTeacher = new Page(page,size);
        Map<String,Object> map = teacherService.seleteTeacherPage(pageTeacher);
        return R.ok().data(map);
    }

    @ApiOperation("通过id查询老师信息")
    @GetMapping ("/get/byId/{teacherId}")
    public R getTeacher(
            @PathVariable("teacherId") String teacherId
    ) {
        Teacher byId = teacherService.getById(teacherId);
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<Course> list = courseService.list(wrapper);
        return R.ok().data("teacher",byId).data("courseList",list);
    }

}

