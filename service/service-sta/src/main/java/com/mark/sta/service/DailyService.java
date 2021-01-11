package com.mark.sta.service;

import com.mark.sta.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mark.sta.entity.vo.DailyQueryVO;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author mark
 * @since 2021-01-11
 */
public interface DailyService extends IService<Daily> {

    void setStaData(String date);

    Map<String, Object> getStaData(DailyQueryVO dailyQueryVO);
}
