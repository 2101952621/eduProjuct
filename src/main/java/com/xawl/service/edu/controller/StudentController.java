package com.xawl.service.edu.controller;

import com.xawl.service.edu.commonutils.JwtUtils;
import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.Student;
import com.xawl.service.edu.entity.query.StudentQuery;
import com.xawl.service.edu.service.ClassService;
import com.xawl.service.edu.service.StudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/edu/student")
public class StudentController {

    @Autowired
    StudentService studentService;


    @Autowired
    ClassService classService;

    @ApiOperation("获取所有学生")
    @GetMapping("/find/all")
    public R getAllList() {
        List<Student> list = studentService.list(null);
        return R.ok().data("items", list);
    }


    @ApiOperation("根据id删除学生")
    @DeleteMapping("/remove/byId/{id}")
    public R remove(
            @ApiParam("删除学生id")
            @PathVariable("id") String id) {
        boolean b = studentService.removeStuById(id);

        if (b == true) {
            return R.ok();
        } else {
            return R.error();

        }
    }

    @ApiOperation("学生分页")
    @GetMapping("/get/page/{pageSize}/{pageInfo}")
    public R getTeacherPageList(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo
    ) {
        Page<Student> page = new Page(pageSize, pageInfo);
        IPage<Student> pageList = studentService.page(page, null);
        long total = pageList.getTotal();
        List<Student> row = pageList.getRecords();
        return R.ok().data("total", total).data("row", row);
    }

    @ApiOperation("条件分页")
    @PostMapping("/get/page/condition/{pageSize}/{pageInfo}")
    public R getPageByCondition(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo,
            @RequestBody(required = false) StudentQuery studentQuery
    ) {
        Page<Student> studentPage = studentService.pageByCondition(pageSize, pageInfo, studentQuery);
        List<Student> records = studentPage.getRecords();
        long total = studentPage.getTotal();
        return R.ok().data("total",total).data("rows", records);
    }

    @ApiOperation("添加学生")
    @PostMapping("/save/student")
    public R insert(
            @RequestBody(required = false) Student student
    ) {
        boolean save = studentService.saveInfo(student);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("通过id查询学生信息")
    @GetMapping("/get/byId/{id}")
    public R getStudent(
            @PathVariable("id") String id
    ) {
        Student byId = studentService.getById(id);
        return R.ok().data("student", byId);
    }

    @ApiOperation("更新学生信息")
    @PostMapping("/update")
    public R update(
            @RequestBody Student student
    ) {
        boolean b = studentService.updateStu(student);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @ApiOperation("通过班级id获取学生列表")
    @GetMapping("/get/by/class/{pageSize}/{pageInfo}/{classId}")
    public R getStudentListByClassId(
            @PathVariable("pageSize") Long pageSize,
            @PathVariable("pageInfo") Long pageInfo,
            @PathVariable("classId") String classId
    ) {
        if (StringUtils.isEmpty(classId)){
            return R.ok().message("没有信息");
        }
        List<Student> list = studentService.getListByClassId(pageSize, pageInfo, classId);
        Integer total = classService.getCountById(classId);
        return R.ok().data("list", list).data("total", total);
    }

    @ApiOperation("通过学生id删除学生加入班级信息")
    @PutMapping("/update/stu/class/{studentId}")
    public R removeByClassId(
            @PathVariable("studentId") String studentId
    ) {
        boolean result = studentService.updateStuById(studentId);
        if (result) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation("学生登录")
    @PostMapping("/login/{number}/{password}")
    public R studentLogin(
            @PathVariable("number") String number,
            @PathVariable("password") String password
    ) {
        String token = studentService.studentLogin(number, password);
        if (token == null) {
            return R.error();
        }
        return R.ok().data("token", token);
    }

    @GetMapping("/get/stu")
    public R getStudentInfo(HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        Student user = studentService.getById(id);
        return  R.ok().data("userInfo",user);
    }
}


