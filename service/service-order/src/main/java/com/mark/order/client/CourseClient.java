package com.mark.order.client;

import com.mark.order.client.impl.CourseClientImpl;
import com.mark.servicebase.vo.CourseOrderVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/9 16:39
 */
@FeignClient(value = "service-edu", fallback = CourseClientImpl.class)
public interface CourseClient {

    /**
     * 消费者：获取订单中的课程信息
     * @param cid 课程id
     * @return CourseOrderVO
     */
    @ApiOperation(value = "获取订单中的课程信息")
    @GetMapping("/api/edu/course/provider/{cid}")
    CourseOrderVO getCourseOrderById(@ApiParam("课程cid") @PathVariable("cid") String cid);
}
