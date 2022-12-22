package com.xawl.service.edu.entity.vo;

import com.xawl.service.edu.entity.Video;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "章节", description = "章节数据封装")
@Data
public class ChapterVO {
    private  String id;
    private  String title;
    //封装小节对象
    private  List<Video> videos;
}
