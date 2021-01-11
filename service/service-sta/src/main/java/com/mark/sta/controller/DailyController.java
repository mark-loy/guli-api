package com.mark.sta.controller;


import com.mark.commonutil.entity.Result;
import com.mark.sta.entity.vo.DailyQueryVO;
import com.mark.sta.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author mark
 * @since 2021-01-11
 */
@Api(value = "统计管理", tags = {"日常统计服务接口"})
@RestController
@RequestMapping("/api/sta/daily")
public class DailyController {

    @Resource
    private DailyService dailyService;

    /**
     * 生成统计数据
     * @param date 日期
     * @return Result
     */
    @ApiOperation("生成统计数据")
    @GetMapping("/generate/{date}")
    public Result generateStaData(@ApiParam("日期") @PathVariable("date") String date) {
        dailyService.setStaData(date);
        return Result.ok();
    }

    /**
     * 获取统计数据
     * @param dailyQueryVO 查询条件对象
     * @return Result
     */
    @ApiOperation("获取统计数据")
    @PostMapping("/get")
    public Result getStaData(@ApiParam("查询条件对象") @RequestBody DailyQueryVO dailyQueryVO) {
        Map<String, Object> map = dailyService.getStaData(dailyQueryVO);
        return Result.ok().data(map);
    }
}

