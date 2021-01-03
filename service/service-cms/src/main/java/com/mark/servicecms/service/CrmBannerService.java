package com.mark.servicecms.service;

import com.mark.servicecms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author mark
 * @since 2021-01-01
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> getBannerPublish();
}
