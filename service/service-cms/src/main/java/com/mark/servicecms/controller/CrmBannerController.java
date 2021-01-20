package com.mark.servicecms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.servicecms.client.OssClient;
import com.mark.servicecms.entity.CrmBanner;
import com.mark.servicecms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author mark
 * @since 2021-01-01
 */
@Api(value = "banner管理", tags = {"banner服务接口"})
@RestController
@RequestMapping("/api/cms/banner")
public class CrmBannerController {

    @Resource
    private CrmBannerService bannerService;

    @Resource
    private OssClient ossClient;

    /**
     * 分页查询banner
     * @param current 当前页
     * @param size 当页显示数
     * @return Result
     */
    @ApiOperation(value = "分页查询banner")
    @GetMapping("/{current}/{size}")
    public Result getBannerByPage(@ApiParam(value = "当前页") @PathVariable("current") Integer current,
                                  @ApiParam(value = "当页显示数") @PathVariable("size") Integer size) {

        // 构建分页对象
        Page<CrmBanner> page = new Page<>(current, size);

        // 构建查询对象
        QueryWrapper<CrmBanner> bannerWrapper = new QueryWrapper<>();
        // 按创建时间降序排列
        bannerWrapper.orderByDesc("gmt_create");

        // 执行分页
        bannerService.page(page, bannerWrapper);

        return Result.ok().data("banners", page.getRecords()).data("total", page.getTotal());
    }

    /**
     * 添加banner
     * @param crmBanner banner实体
     * @return Result.ok()
     */
    @ApiOperation(value = "添加banner")
    @PostMapping("/")
    public Result saveBanner(@ApiParam(value = "banner实体") @RequestBody CrmBanner crmBanner) {
        bannerService.saveBanner(crmBanner);
        return Result.ok();
    }

    /**
     * 根据id查询banner
     * @param id banner的id
     * @return Result.ok().data("banner", banner)
     */
    @ApiOperation(value = "根据id查询banner")
    @GetMapping("/{id}")
    public Result getBannerById(@ApiParam(value = "banner的id") @PathVariable("id") String id) {

        CrmBanner banner = bannerService.getById(id);

        return Result.ok().data("banner", banner);
    }

    /**
     * 修改banner
     * @param crmBanner banner实体
     * @return Result.ok()
     */
    @ApiOperation(value = "修改banner")
    @PutMapping("/")
    public Result updateBanner(@ApiParam(value = "banner实体") @RequestBody CrmBanner crmBanner) {
        bannerService.updateBanner(crmBanner);
        return Result.ok();
    }

    /**
     * 根据id删除banner
     * @param id bannerId
     * @return Result.ok()
     */
    @ApiOperation("根据id删除banner")
    @DeleteMapping("/{id}")
    public Result deleteBanner(@ApiParam("bannerId") @PathVariable("id") String id) {
        // 根据id查询banner的上传地址
        CrmBanner banner = bannerService.getById(id);
        if (banner == null) {
            return Result.error().message("banner不存在");
        }
        String imageUrl = banner.getImageUrl();

        // 获取文件的路由+文件名
        // 去掉了url中的协议部分
        String temUrl = imageUrl.substring(imageUrl.indexOf("://") + 3);
        // 去掉了url中的域名部分
        String filename = temUrl.substring(temUrl.indexOf("/") + 1);
        // 删除oss中的图片
        ossClient.deleteFile(filename);

        // 删除本地的banner数据
        bannerService.removeById(id);
        return Result.ok();
    }



}

