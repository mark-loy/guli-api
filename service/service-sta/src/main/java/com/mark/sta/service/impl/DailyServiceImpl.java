package com.mark.sta.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mark.commonutil.entity.Result;
import com.mark.servicebase.enums.CustomExceptionEnum;
import com.mark.servicebase.exception.CustomException;
import com.mark.sta.client.MemberClient;
import com.mark.sta.entity.Daily;
import com.mark.sta.entity.vo.DailyQueryVO;
import com.mark.sta.mapper.DailyMapper;
import com.mark.sta.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author mark
 * @since 2021-01-11
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Resource
    private MemberClient memberClient;

    @Override
    public void setStaData(String date) {
        // 远程调用用户服务
        Result memberRegister = memberClient.getMemberRegister(date);
        if (!memberRegister.getSuccess()) {
            throw new CustomException(CustomExceptionEnum.SELECT_DATA_ERROR);
        }
        // 获取某一天的注册人数
        Integer count = (Integer) memberRegister.getData().get("count");

        // 先删除当天的统计数据
        QueryWrapper<Daily> dailyWrapper = new QueryWrapper<>();
        dailyWrapper.eq("date_calculated", date);
        baseMapper.delete(dailyWrapper);

        Daily daily = new Daily();
        // 设置统计数据
        daily.setRegisterNum(count)
                .setCourseNum(RandomUtil.randomInt(100, 200))
                .setLoginNum(RandomUtil.randomInt(100, 200))
                .setVideoViewNum(RandomUtil.randomInt(100, 200));
        // 设置统计日期
        daily.setDateCalculated(date);
        // 执行保存
        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getStaData(DailyQueryVO dailyQueryVO) {
        // 构建查询条件对象
        QueryWrapper<Daily> dailyWrapper = new QueryWrapper<>();
        // 设置日期
        dailyWrapper.between("date_calculated", dailyQueryVO.getBegin(), dailyQueryVO.getEnd());
        // 设置统计字段
        dailyWrapper.select(dailyQueryVO.getType(), "date_calculated");
        List<Daily> dailies = baseMapper.selectList(dailyWrapper);

        // 构造统计日期集合
        List<String> dateList = new ArrayList<>();
        // 构造统计字段集合
        List<Integer> typeList = new ArrayList<>();

        // 遍历dailies
        for (Daily daily : dailies) {
            // 设置日期集合
            dateList.add(daily.getDateCalculated());
            // 设置统计字段集合
            switch (dailyQueryVO.getType()) {
                case "register_num":
                    typeList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    typeList.add(daily.getLoginNum());
                    break;
                case "video_view_num":
                    typeList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    typeList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        // 结果封装
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("dates", dateList);
        resultMap.put("types", typeList);
        return resultMap;
    }
}
