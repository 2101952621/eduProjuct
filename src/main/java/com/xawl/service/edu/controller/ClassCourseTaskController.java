package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.ClassCourseTask;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.service.ClassCourseTaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author javaCoder
 * @since 2022-12-23
 */
@RestController
@RequestMapping("/edu/task")
public class ClassCourseTaskController {

    @Autowired
    ClassCourseTaskService classCourseTaskService;

    @ApiOperation("查询班级课程任务")
    @GetMapping("/get/list/{classId}")
    public R getList(
            @PathVariable("classId") String id
    ){
        List<ClassCourseTask> taskByCourseId = classCourseTaskService.getTaskByCourseId(id);
        return R.ok().data("data",taskByCourseId);
    }
}

