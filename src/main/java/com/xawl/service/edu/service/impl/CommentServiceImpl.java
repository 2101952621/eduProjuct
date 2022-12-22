package com.xawl.service.edu.service.impl;


import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.service.CommentService;
import com.xawl.service.edu.service.TeacherService;
import com.xawl.service.edu.entity.Comment;
import com.xawl.service.edu.entity.Student;
import com.xawl.service.edu.entity.Teacher;
import com.xawl.service.edu.entity.frontvo.CommentVO;
import com.xawl.service.edu.mapper.CommentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    TeacherService teacherService;

    @Override
    public void saveCommon(Student user, CommentVO commentVO) {
        Comment comment = new Comment();
        comment.setMemberId(user.getId());
        if (!StringUtils.isEmpty(commentVO.getContent())){
            comment.setContent(commentVO.getContent());
        }
        comment.setNickname(user.getNickname());
        comment.setAvatar(user.getAvatar());
        comment.setCourseId(commentVO.getCourseId());
        comment.setTeacherId(commentVO.getTeacherId());
        baseMapper.insert(comment);

    }

    @Override
    public Map<String, Object> getPageByCourseId(Page<Comment> page, String courseId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        Page<Comment> pageParam = baseMapper.selectPage(page, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("items", page.getRecords());
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return map;
    }

    @Override
    public Page<Comment> getCommentByTeacherId(Page<Comment> pageParam, String teacherId) {
        //判断是否是教师
        Teacher teacherServiceById = teacherService.getById(teacherId);
        if (teacherServiceById == null){
            throw new BusinessException(20001,"只有教师能够回答问题");
        }
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer == 0){
            throw new BusinessException(20001,"当前教师还没有管理班级");
        }
        Page<Comment> commentPage = baseMapper.selectPage(pageParam, wrapper);
        return commentPage;
    }
}
