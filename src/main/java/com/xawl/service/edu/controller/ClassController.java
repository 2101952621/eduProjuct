package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.JwtUtils;
import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.Class;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.Teacher;
import com.xawl.service.edu.entity.query.ClassQuery;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.service.ClassCourseService;
import com.xawl.service.edu.service.ClassService;
import com.xawl.service.edu.service.TeacherService;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/edu/class")
public class ClassController {

    @Autowired
    ClassService classService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    ClassCourseService classCourseService;


    @ApiOperation("获取所有班级")
    @GetMapping("/find/all")
    public R getAllList() {
        List<Class> list = classService.list(null);
        return R.ok().data("items", list);
    }


    @ApiOperation("根据id删除班级")
    @DeleteMapping("/remove/byId/{id}")
    public R remove(
            @ApiParam("删除课程id")
            @PathVariable("id") String id) {
        boolean b = classService.removeClassById(id);
        if (b == true) {
            return R.ok();
        } else {
            return R.error();

        }
    }

    @ApiOperation("班级分页")
    @GetMapping("/get/page/{pageSize}/{pageInfo}")
    public R getClassPageList(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo
    ) {
        Page<Class> page = new Page(pageSize, pageInfo);
        IPage<Class> pageList = classService.page(page, null);
        long total = pageList.getTotal();
        List<Class> row = pageList.getRecords();
        return R.ok().data("total", total).data("row", row);
    }

    @ApiOperation("班级分页")
    @GetMapping("/get/front/page/{pageSize}/{pageInfo}")
    public R getClassFrontList(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo
    ) {
        Page<Class> page = new Page(pageSize, pageInfo);
        Map<String,Object> map = classService.getCourseFront(page);
        return R.ok().data(map);
    }

    @ApiOperation("条件分页")
    @PostMapping("/get/page/condition/{pageSize}/{pageInfo}")
    public R getPageByCondition(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo,
            @RequestBody(required = false) ClassQuery classQuery
    ) {
        IPage<Class> list = classService.pageByCondition(pageSize, pageInfo, classQuery);
        long total = list.getTotal();
        List<Class> row = list.getRecords();
        row.forEach(a -> {
            String classTeacherId = a.getClassTeacherId();
            if (!StringUtils.isEmpty(classTeacherId)) {
                Teacher teacher = teacherService.getById(classTeacherId);
                if (!StringUtils.isEmpty(teacher)) {
                    a.setTeacherName(teacher.getName());
                }
            }
        });
        System.out.println(row.toString()+ "**************************");
        return R.ok().data("total", total).data("rows", row);
    }


    @PostMapping("/save/class")
    public R insert(
            @RequestBody Class classInfo) {
        //保存班级信息
        String classId = classService.saveClassInfo(classInfo);
        //保存班级课程信息
        List<Course> courseList = classInfo.getCourseList();
        if (classId!=null){
            if (!courseList.isEmpty()){
                classCourseService.saveClassCourse(classId,courseList);
            }
            return R.ok();
        }else {
            return R.error();
        }

    }


    @GetMapping("/get/byId/{id}")
    public R getClass(
            @PathVariable("id") String id
    ) {
        Class classInfo = classService.getById(id);
        String teacherId = classInfo.getClassTeacherId();
        Teacher teacher = teacherService.getById(teacherId);
        classInfo.setTeacherName(teacher.getName());
        List<Course> listByClassId = classCourseService.getListByClassId(id);
        return R.ok().data("classInfo", classInfo).data("courseList",listByClassId);
    }

    @PostMapping("/update")
    public R update(
            @RequestBody Class classInfo
    ) {
        boolean b = classService.updateById(classInfo);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("学生加入班级")
    @PostMapping("/stu/get/in/{classId}")
    public R getInClass(
            HttpServletRequest request,
            @PathVariable("classId") String classId) {
        boolean b = JwtUtils.checkToken(request);
        if (!b){
            throw new BusinessException(20001,"请先登录后访问");
        }
        String id = JwtUtils.getMemberIdByJwtToken(request);
        boolean result = classService.stuJoinClass(id,classId);
        if (result){
            return R.ok().message("加入班级成功了");
        }else {
            return R.error();
        }
    }

    @ApiOperation("学生退出班级")
    @PostMapping("/stu/exit/{classId}")
    public R exitClass(
            HttpServletRequest request,
            @PathVariable("classId") String classId) {
        boolean result = JwtUtils.checkToken(request);
        if (!result){
            throw new BusinessException(20001,"请先登录后访问");
        }
        String number = JwtUtils.getMemberIdByJwtToken(request);
        boolean b = classService.stuExitClass(number,classId);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

