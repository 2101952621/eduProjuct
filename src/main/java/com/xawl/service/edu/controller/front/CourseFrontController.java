package com.xawl.service.edu.controller.front;



import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.service.CourseService;
import com.xawl.service.edu.entity.Course;
import com.xawl.service.edu.entity.frontvo.CourseQueryVo;
import com.xawl.service.edu.entity.frontvo.CourseWebVo;
import com.xawl.service.edu.entity.ordervo.CourseWebOrderVo;
import com.xawl.service.edu.entity.vo.ChapterVO;
import com.xawl.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Api("前台课程信息")
@RestController
@CrossOrigin
@RequestMapping("/edu/front/course")
public class CourseFrontController {

    @Autowired
    CourseService courseService;

    @Autowired
    ChapterService chapterService;

//    @Autowired
//    Order


    @ApiOperation("前台数据显示")
    @PostMapping("/get/course/{page}/{size}")
    public R getFrontQueryCourse(
            @RequestBody(required = false) CourseQueryVo courseQueryVo,
            @PathVariable("page") Long page,
            @PathVariable("size") Long size
            ){
        Page<Course> pageCourse = new Page<Course>(page,size);
        Map<String,Object> map = courseService.getCourseFront(pageCourse,courseQueryVo);
        return  R.ok().data(map);
    }


    @ApiOperation("根据课程信id查询课程信息")
    @GetMapping("/get/course/base/{courseId}")
    public R getBaseCourseById(
            @PathVariable("courseId") String  courseId,
            HttpServletRequest request
    ){
        //查询课程基本信息
        CourseWebVo course = courseService.getBaseCourseById(courseId);
        //查询章节小节信息
        List<ChapterVO> chapterAndVideo = chapterService.getChapterAndVideo(courseId);
        //判断课程是否被用户购买
//        boolean byMemberBuy = orderClient.isByMemberBuy(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return  R.ok().data("courseBse",course)
                .data("chapterAndVideo",chapterAndVideo);
//                .data("isBuy",byMemberBuy);

    }


    @GetMapping("/get/course/{courseId}")
    public CourseWebOrderVo getCourseById(
            @PathVariable("courseId") String courseId
    ){
        CourseWebVo baseCourseById = courseService.getBaseCourseById(courseId);
        CourseWebOrderVo courseWebOrderVo = new CourseWebOrderVo();
        BeanUtils.copyProperties(baseCourseById,courseWebOrderVo);
        return courseWebOrderVo;
    }



}

