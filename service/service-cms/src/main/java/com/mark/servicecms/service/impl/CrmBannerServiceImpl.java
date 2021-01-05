package com.mark.servicecms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.servicecms.entity.CrmBanner;
import com.mark.servicecms.mapper.CrmBannerMapper;
import com.mark.servicecms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author mark
 * @since 2021-01-01
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    @Cacheable(value = "banner", key = "'getPublish'")
    public List<CrmBanner> getBannerPublish() {
        QueryWrapper<CrmBanner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 2");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    @CacheEvict(value = "banner", allEntries = true)
    public void saveBanner(CrmBanner crmBanner) {
        int insert = baseMapper.insert(crmBanner);
        if (insert != 1) {
            throw new CustomException(CustomExceptionEnum.INSERT_DATA_ERROR);
        }
    }

    @Override
    @CacheEvict(value = "banner", allEntries = true)
    public void updateBanner(CrmBanner crmBanner) {
        int update = baseMapper.updateById(crmBanner);
        if (update != 1) {
            throw new CustomException(CustomExceptionEnum.UPDATE_DATA_ERROR);
        }
    }

    @Override
    @CacheEvict(value = "banner", allEntries = true)
    public void deleteBannerById(String id) {
        int delete = baseMapper.deleteById(id);
        if (delete != 1) {
            throw new CustomException(CustomExceptionEnum.DELETE_DATA_ERROR);
        }
    }
}
