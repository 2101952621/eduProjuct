package com.xawl.service.edu.controller;


import com.xawl.service.edu.commonutils.R;
import com.xawl.service.edu.entity.Chapter;
import com.xawl.service.edu.entity.vo.ChapterVO;
import com.xawl.service.edu.service.ChapterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/edu/chapter")
@CrossOrigin
public class ChapterController {

    @Resource
    ChapterService chapterService;

    @ApiOperation("获取章节内容及其小节内容")
    @GetMapping("/get/list/{id}")
    public R getList(
            @PathVariable("id")
            String id){
        List<ChapterVO> list = chapterService.getChapterAndVideo(id);
        return R.ok().data("list",list);
    }

    @ApiOperation(value = "新增章节")
    @PostMapping("/save/chapter")
    public R save(
            @ApiParam(name = "chapterVo", value = "章节对象", required = true)
            @RequestBody Chapter chapter){
        chapterService.save(chapter);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询章节")
    @GetMapping("/{id}")
    public R getById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){

        Chapter chapter = chapterService.getById(id);
        return R.ok().data("item", chapter);
    }


    @ApiOperation(value = "根据ID修改章节")
    @PutMapping("/update/chapter")
    public R updateById(
            @ApiParam(name = "chapter", value = "章节对象", required = true)
            @RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除章节")
    @DeleteMapping("/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable String id){

        boolean result = chapterService.removeChapterById(id);
        if(result){
            return R.ok();
        }else{
            return R.error().message("删除章节中存在小节,删除失败");
        }
    }


}

