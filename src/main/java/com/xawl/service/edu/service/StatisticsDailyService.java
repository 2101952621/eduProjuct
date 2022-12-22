package com.xawl.service.edu.service;

import com.xawl.service.edu.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author javaCoder
 * @since 2022-01-18
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void getSta(String day);

    Map getRangeData(String type, String start, String end);
}
