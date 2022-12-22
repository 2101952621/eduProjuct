package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.vo.SubjectVO;
import com.xawl.service.edu.service.SubjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author javaCoder
 * @since 2022-01-10
 */
@RestController
@RequestMapping("/edu/subject")
@CrossOrigin
public class SubjectController {

    @Resource
    SubjectService subjectService;

    @ApiOperation("excel表格的导入")
    @PostMapping("/import")
    public R importData(MultipartFile file) {
        subjectService.saveSubjectData(file);
        return R.ok().code(20000).message("上传成功");
    }

    @ApiOperation("课程分类显示")
    @GetMapping("/list")
    public R getSubList() {
        List<SubjectVO> list = subjectService.getSubList();
        return R.ok().code(20000).data("data",list);
    }
}

