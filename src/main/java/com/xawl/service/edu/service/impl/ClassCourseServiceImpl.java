package com.xawl.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.service.CourseService;
import com.xawl.service.edu.entity.ClassCourseInfo;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.mapper.ClassCourseMapper;
import com.xawl.service.edu.service.ClassCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author javaCoder
 * @since 2022-02-12
 */
@Service
public class ClassCourseServiceImpl extends ServiceImpl<ClassCourseMapper, ClassCourseInfo> implements ClassCourseService {

    @Autowired
    CourseService courseService;

    @Override
    public void saveClassCourse(String classId, List<Course> courseList) {
        courseList.forEach(a -> {
            ClassCourseInfo classCourseInfo = new ClassCourseInfo();
            classCourseInfo.setClassId(classId + "");
            classCourseInfo.setCourseId(a.getId());
            baseMapper.insert(classCourseInfo);
        });

    }

    @Override
    public List<Course> getListByClassId(String classId) {
        List<Course> arrayList = new ArrayList<>();
        QueryWrapper<ClassCourseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        List<ClassCourseInfo> list = baseMapper.selectList(queryWrapper);
        list.forEach(a -> {
            String courseId = a.getCourseId();
            if (!StringUtils.isEmpty(courseId)) {
                Course course = courseService.getById(courseId);
                arrayList.add(course);
            }
        });
        return arrayList;
    }

    @Override
    public void removeByCourseId(String classId, String courseId) {
        QueryWrapper<ClassCourseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId)
                .eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public void addCourse(String classId, String courseId) {
        if ("undefined".equals(classId)){
            throw new BusinessException(20001,"请先选择添加的班级信息");
        }
        if (StringUtils.isEmpty(classId) || StringUtils.isEmpty(courseId)){
            throw new BusinessException(20001, "信息错误请重试");
        }
        QueryWrapper<ClassCourseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id", classId)
                .eq("course_id", courseId);
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer > 0) {
            throw new BusinessException(20001, "课程已添加到该班级");
        }
        ClassCourseInfo classCourseInfo = new ClassCourseInfo();
        classCourseInfo.setCourseId(courseId);
        classCourseInfo.setClassId(classId);
        baseMapper.insert(classCourseInfo);
    }
}
