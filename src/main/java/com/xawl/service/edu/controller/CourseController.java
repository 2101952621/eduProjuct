package com.xawl.service.edu.controller;



import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.vo.CourseInfoVO;
import com.xawl.service.edu.entity.vo.CoursePublishVO;
import com.xawl.service.edu.entity.vo.CourseQuery;
import com.xawl.service.edu.service.CourseService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Api("课程管理")
@RestController
@CrossOrigin
@RequestMapping("/edu/course")
public class CourseController {

    @Resource
    CourseService courseService;


    @ApiOperation("查询所有课程信息")
    @GetMapping("/get/list")
    public R getList(){
        List<Course> list = courseService.list();
        return R.ok().data("data",list);
    }


    @ApiOperation("添加课程信息")
    @PostMapping("/add/course/info")
    public R addCourse(
            @RequestBody CourseInfoVO courseInfoVO
            ){
        String courseId = courseService.addCourseInfo(courseInfoVO);
        return R.ok().data("courseId",courseId).message("课程添加成功");
    }

    @ApiOperation("通过课程id获取课程信息")
    @GetMapping("/get/course/byId/{id}")
    public R getCourseById(@PathVariable("id") String id){
        CourseInfoVO course = courseService.getCourseInfoById(id);
        return R.ok().data("courseInfoVo",course);
    }

    @PostMapping("/update/course")
    public R updateById(@RequestBody CourseInfoVO course){
        courseService.updateCourse(course);
        return R.ok().message("数据更新成功");
    }


    @ApiOperation("课程最终发布信息")
    @GetMapping("/get/publish/{id}")
    public R getCoursePublish(@PathVariable String id){
        CoursePublishVO vo = courseService.getCoursePublishVo(id);
        return R.ok().data("publish",vo);
    }


    @ApiOperation("课程最终发布")
    @PostMapping("/publish/{id}")
    public R publishCourse(@PathVariable String id){
        Course course = new Course();
        course.setId(id);
        course.setStatus("Normal");
        courseService.updateById(course);
        return R.ok();
    }

    @ApiOperation("删除课程信息")
    @DeleteMapping("/remove/{id}")
    public R removeCourse(@PathVariable("id") String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }

    @ApiOperation("条件分页")
    @PostMapping("/get/page/condition/{pageSize}/{pageInfo}")
    public R getPageByCondition(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo,
            @RequestBody(required = false) CourseQuery courseQuery
    ) {
        IPage<Course> list =  courseService.pageByCondition(pageSize, pageInfo, courseQuery);
        long total = list.getTotal();
        List<Course> row = list.getRecords();
        return R.ok().data("total",total).data("rows",row);
    }
}

