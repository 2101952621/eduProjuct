package com.xawl.service.edu.controller;



import com.xawl.service.edu.service.StatisticsDailyService;
import com.xawl.service.edu.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/sta")
public class StatisticsDailyController {

    @Autowired
    StatisticsDailyService service;

    //通过远程调用接口,通过日期获取注册人数
    @GetMapping("/count/{day}")
    public R getSta(
            @PathVariable("day") String day
    ){
        service.getSta(day);
        return R.ok();
    }
    //通过日期范围,查询类型查询数据返回
    @GetMapping("/range/{type}/{start}/{end}")
    public R getRangeData(
            @PathVariable("type") String type,
            @PathVariable("start") String start,
            @PathVariable("end") String end
    ){
        Map map = service.getRangeData(type,start,end);
        return R.ok().data(map);
    }




}

