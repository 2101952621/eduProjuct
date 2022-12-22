package com.xawl.service.edu.controller;


import com.xawl.service.edu.service.StudentService;
import com.xawl.service.edu.commonutils.JwtUtils;
import com.xawl.service.edu.commonutils.R;

import com.xawl.service.edu.entity.Comment;
import com.xawl.service.edu.entity.Student;
import com.xawl.service.edu.entity.frontvo.CommentVO;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.service.CommentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 提问
 */
@RestController
@CrossOrigin
@RequestMapping("/edu/comment")
public class CommentController {

    @Autowired
    CommentService commentService;


    @Autowired
    StudentService studentService;

    @PostMapping("/put")
    public R userProblem(
            HttpServletRequest request,
            @RequestBody CommentVO comment
            ){
        boolean b = JwtUtils.checkToken(request);
        if (b){
            String id = JwtUtils.getMemberIdByJwtToken(request);
            Student student = studentService.getById(id);
            commentService.saveCommon(student,comment);
            return R.ok().message("提交成功");
        }else{
            throw  new BusinessException(20001,"请先登录后访问");
        }
    }

    @ApiOperation("提问分页")
    @GetMapping("/get/page/{pageSize}/{pageInfo}/{courseId}")
    public R getCommentPageList(
            @PathVariable("pageSize") long pageSize,
            @PathVariable("pageInfo") long pageInfo,
            @PathVariable("courseId") String courseId
    ) {
        Page<Comment> pageParam = new Page(pageSize,pageInfo);
        Map<String, Object> map = commentService.getPageByCourseId(pageParam,courseId);
        return R.ok().data(map);
    }

    @ApiOperation("教师回答问题")
    @GetMapping("/get/comment/{pageSize}/{pageInfo}")
    public R getCommentByTeacherId(
            @PathVariable("pageSize") long pageSize,
            @PathVariable("pageInfo") long pageInfo,
            HttpServletRequest request
    ) {
        boolean b = JwtUtils.checkToken(request);
        if (!b){
            return R.error();
        }
        String id = JwtUtils.getMemberIdByJwtToken(request);
        Page<Comment> pageParam = new Page(pageSize,pageInfo);
        Page<Comment> list = commentService.getCommentByTeacherId(pageParam,id);
        List<Comment> records = list.getRecords();
        long total = list.getTotal();
        return R.ok().data("total",total).data("records",list);
    }


    @ApiOperation("恢复内容")
    @PostMapping("/solve/{id}/{solve}")
    public R userSolve(
            @PathVariable("id") String id,
            @PathVariable("solve") String solve
    ){
        Comment comment = commentService.getById(id);

        comment.setSolve(solve);
        comment.setIsSolve(true);
        boolean b = commentService.updateById(comment);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

