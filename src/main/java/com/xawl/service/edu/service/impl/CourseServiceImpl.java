package com.xawl.service.edu.service.impl;

import com.xawl.service.edu.service.CourseService;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.CourseDescription;
import com.xawl.service.edu.entity.frontvo.CourseQueryVo;
import com.xawl.service.edu.entity.frontvo.CourseWebVo;
import com.xawl.service.edu.entity.vo.CourseInfoVO;
import com.xawl.service.edu.entity.vo.CoursePublishVO;
import com.xawl.service.edu.entity.vo.CourseQuery;
import com.xawl.service.edu.exception.BusinessException;
import com.xawl.service.edu.mapper.CourseMapper;
import com.xawl.service.edu.service.ChapterService;
import com.xawl.service.edu.service.CourseDescriptionService;
import com.xawl.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    CourseDescriptionService courseDescriptionService;

    @Autowired
    ChapterService chapterService;

    @Autowired
    VideoService videoService;


    @Override
    public String addCourseInfo(CourseInfoVO courseInfoVO) {
        //添加课程信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoVO, course);
        int insert = baseMapper.insert(course);
        if (insert == 0) {
            throw new BusinessException(20001, "数据添加失败");
        }
        //添加课程简介信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoVO.getDescription());
        courseDescriptionService.save(courseDescription);
        return course.getId();
    }

    //通过id查询课程信息
    @Override
    public CourseInfoVO getCourseInfoById(String id) {
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        Course course = baseMapper.selectById(id);
        BeanUtils.copyProperties(course, courseInfoVO);
        //通过课程id获取描述信息
        CourseDescription courseDescription = courseDescriptionService.getById(id);
        courseInfoVO.setDescription(courseDescription.getDescription());
        return courseInfoVO;
    }

    @Override
    public void updateCourse(CourseInfoVO course) {
        Course course1 = new Course();
        BeanUtils.copyProperties(course, course1);
        int i = baseMapper.updateById(course1);
        if (i == 0) {
            throw new BusinessException(20001, "更新数据失败");
        }
        //更新描述信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(course.getDescription());
        courseDescriptionService.updateById(courseDescription);
    }

    @Override
    public CoursePublishVO getCoursePublishVo(String id) {
        return baseMapper.getPublishCourseInfo(id);
    }

    //删除课程信息
    @Override
    public void removeCourse(String courseId) {
        //根据id删除小节内容
        videoService.removeByCourseId(courseId);
        //根据课程id删除章节
        chapterService.removeByCourseId(courseId);
        //删除课程描述信息
        courseDescriptionService.removeById(courseId);
        //删除课程信息
        int i = baseMapper.deleteById(courseId);
        if (i == 0) {
            throw new BusinessException(20001, "删除课程信息失败");
        }
    }

    //条件查询分页
    @Override
    public IPage<Course> pageByCondition(Long pageSize, Long pageInfo, CourseQuery teacherQuery) {
        String name = teacherQuery.getTitle();
        String status = teacherQuery.getStatus();
        Page<Course> coursePage = new Page<>(pageSize, pageInfo);
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        if (name != null) {
            courseQueryWrapper.eq("title", teacherQuery.getTitle());
        }
        if (status != null) {
            courseQueryWrapper
                    .eq("status", teacherQuery.getStatus());
        }
        return baseMapper.selectPage(coursePage, courseQueryWrapper);
    }

    @Override
    public List<Course> getCourseIndex() {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id")
                .last("limit  4");
        List<Course> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    @Override
    public Map<String, Object> getCourseFront(Page<Course> pageParam, CourseQueryVo courseQuery) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseQuery.getSubjectParentId())) {
            queryWrapper.eq("subject_parent_id", courseQuery.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(courseQuery.getSubjectId())) {
            queryWrapper.eq("subject_id", courseQuery.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseQuery.getBuyCountSort())) {
            queryWrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseQuery.getGmtCreateSort())) {
            queryWrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseQuery.getPriceSort())) {
            queryWrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageParam, queryWrapper);

        List<Course> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    //查询课程基本信息
    @Override
    public CourseWebVo getBaseCourseById(String courseId) {
        return  baseMapper.selectBaseCourse(courseId);
    }


}