package com.mark.servicecms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mark.commonutil.entity.Result;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
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
@RequestMapping("/service/cms")
public class CrmBannerController {

    @Resource
    private CrmBannerService bannerService;

    /**
     * 分页查询banner
     * @param current 当前页
     * @param size 当页显示数
     * @return Result.ok().data("banners", page.getRecords()).data("total", page.getTotal())
     */
    @ApiOperation(value = "分页查询banner")
    @GetMapping("/banner/{current}/{size}")
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
    @PostMapping("/banner/save")
    public Result saveBanner(@ApiParam(value = "banner实体") @RequestBody CrmBanner crmBanner) {

        boolean save = bannerService.save(crmBanner);
        if (!save) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }
        return Result.ok();
    }

    /**
     * 根据id查询banner
     * @param id banner的id
     * @return Result.ok().data("banner", banner)
     */
    @ApiOperation(value = "根据id查询banner")
    @GetMapping("/banner/{id}")
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
    @PutMapping("/banner")
    public Result updateBanner(@ApiParam(value = "banner实体") @RequestBody CrmBanner crmBanner) {

        boolean update = bannerService.updateById(crmBanner);
        if (!update) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }
        return Result.ok();
    }

    /**
     * 根据id删除banner
     * @param id banner的id
     * @return Result.ok()
     */
    @DeleteMapping("/banner/{id}")
    public Result deleteBanner(@PathVariable("id") String id) {

        boolean delete = bannerService.removeById(id);
        if (!delete) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }

        return Result.ok();
    }

}

