package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.Comment;
import com.xawl.service.edu.entity.Student;
import com.xawl.service.edu.entity.frontvo.CommentVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


public interface CommentService extends IService<Comment> {

    void saveCommon(Student user, CommentVO comment);

    Map<String, Object> getPageByCourseId(Page<Comment> pageParam, String courseId);

    Page<Comment> getCommentByTeacherId(Page<Comment> pageParam, String teacherId);
}
