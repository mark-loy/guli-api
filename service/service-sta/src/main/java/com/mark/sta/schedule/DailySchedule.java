package com.mark.sta.schedule;

import com.mark.sta.service.DailyService;
import com.mark.sta.utils.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/11 17:08
 */
@Component
public class DailySchedule {

    @Resource
    private DailyService dailyService;

    /**
     * 每天凌晨1点，生成统计数据
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void generateStaDataTask() {
        // 生成前一天日期
        Date date = DateUtil.addDays(new Date(), -1);
        // 生成统计数据
        dailyService.setStaData(DateUtil.formatDate(date));
    }
}
