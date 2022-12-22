package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.JwtUtils;
import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.Teacher;
import com.xawl.service.edu.entity.vo.TeacherQuery;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api(description = "教师管理")
@RestController
@RequestMapping("/edu/teacher")
@CrossOrigin
public class TeacherController {

    @Resource
    TeacherService teacherService;

    @ApiOperation("获取所有教师")
    @GetMapping("/find/all")
    public R getAllList() {
        List<Teacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }


    @ApiOperation("根据id删除教师")
    @DeleteMapping("/remove/byId/{id}")
    public R remove(
            @ApiParam("删除教师id")
            @PathVariable("id") String id) {
        boolean b = teacherService.removeTeacherById(id);
        if (b == true) {
            return R.ok();
        } else {
            return R.error();

        }
    }

    @ApiOperation("教师分页")
    @GetMapping("/get/page/{pageSize}/{pageInfo}")
    public R getTeacherPageList(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo
    ) {
        Page<Teacher> page = new Page(pageSize,pageInfo);
        IPage<Teacher> pageList = teacherService.page(page,null);
        long total = pageList.getTotal();
        List<Teacher> row = pageList.getRecords();
        return R.ok().data("total",total).data("row",row);
    }


    @ApiOperation("条件分页")
    @PostMapping("/get/page/condition/{pageSize}/{pageInfo}")
    public R getPageByCondition(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo,
            @RequestBody(required = false) TeacherQuery teacherQuery
    ) {
        IPage<Teacher>  list =  teacherService.pageByCondition(pageSize, pageInfo, teacherQuery);
        long total = list.getTotal();
        List<Teacher> row = list.getRecords();
        return R.ok().data("total",total).data("rows",row);
    }

    @ApiOperation("添加老师")
    @PostMapping("/save/teacher")
    public R insert(
            @RequestBody(required = false) Teacher teacher
    ) {
        boolean save = teacherService.saveTeacher(teacher);
        if (save){
            return  R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation("通过id查询老师信息")
    @GetMapping ("/get/byId/{id}")
    public R getTeacher(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {

        boolean b = JwtUtils.checkToken(request);
        if (!b){
           throw  new BusinessException(20001,"请先登录后访问");
        }
        Teacher byId = teacherService.getById(id);
        return R.ok().data("teacher",byId);
    }

    @ApiOperation("更新教师信息")
    @PostMapping ("/update")
    public R update(
            @RequestBody Teacher teacher
    ) {
        boolean b = teacherService.updateById(teacher);
        if (b){
            return  R.ok();
        }else {
            return R.error();
        }
    }
    @ApiOperation("教师登录")
    @PostMapping("/login/{mobile}/{password}")
    public R studentLogin(
            @PathVariable("mobile") String mobile,
            @PathVariable("password") String password
    ) {
        String token = teacherService.teacherLogin(mobile, password);
        if (token == null) {
            return R.error();
        }
        return R.ok().data("token", token);
    }

    @GetMapping("/get/stu")
    public R getStudentInfo(HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        Teacher user = teacherService.getById(id);
        return  R.ok().data("userInfo",user);
    }


}

