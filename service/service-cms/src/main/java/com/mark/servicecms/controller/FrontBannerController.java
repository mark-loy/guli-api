package com.mark.servicecms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.commonutil.entity.Result;
import com.mark.servicecms.entity.CrmBanner;
import com.mark.servicecms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/1 21:21
 */
@Api(value = "banner发布管理", tags = {"banner发布服务接口"})
@RestController
@RequestMapping("/service/cms")
public class FrontBannerController {

    @Resource
    private CrmBannerService bannerService;

    /**
     * 获取发布的banner图信息
     * @return Result.ok().data("banners", banners)
     */
    @ApiOperation(value = "获取发布的banner图信息")
    @GetMapping("/banner/publish")
    public Result getBannerPublish() {
        QueryWrapper<CrmBanner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 2");
        List<CrmBanner> banners = bannerService.list(queryWrapper);
        return Result.ok().data("banners", banners);
    }
}
