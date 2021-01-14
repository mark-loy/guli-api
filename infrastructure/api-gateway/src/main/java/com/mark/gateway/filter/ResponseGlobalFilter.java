package com.mark.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/13 12:11
 */
@Slf4j
@Component
public class ResponseGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 将 List 数据以""分隔进行拼接
     **/
    private static Joiner joiner = Joiner.on("");

    @Override
    public int getOrder() {
        // 必须小于等于-1
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        ArrayList<Object> list = Lists.newArrayList();
                        // gateway 针对返回参数过长的情况下会分段返回，使用如下方式接受返回参数则可避免
                        dataBuffers.forEach(dataBuffer -> {
                            // probably should reuse buffers
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            // 释放掉内存
                            DataBufferUtils.release(dataBuffer);
                            list.add(new String(content, StandardCharsets.UTF_8));
                        });
                        // 将多次返回的参数拼接起来
                        String responseData = joiner.join(list);
                        // 判断返回类型，对返回空白页进行处理
                        if (responseData.indexOf("Whitelabel Error Page") != -1) {
                            return bufferFactory.wrap(out(response).getBytes(StandardCharsets.UTF_8));
                        }
                        return bufferFactory.wrap(responseData.getBytes(StandardCharsets.UTF_8));
                    }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decorator).build());
    }

    private String out(ServerHttpResponse response) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", false);
        responseMap.put("code", 28004);
        responseMap.put("message", "资源访问错误");
        return JSON.toJSONString(responseMap);
    }
}
