package com.mark.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

    /** 将 List 数据以""分隔进行拼接 **/
    private static Joiner joiner = Joiner.on("");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>)body;
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
                        // 判断返回类型
                        if (responseData.indexOf("Whitelabel Error Page") != -1) {
                            return bufferFactory.wrap(out(response).getBytes(StandardCharsets.UTF_8));
                        }
                        // 重置返回参数
                        String result = response(responseData);
                        byte[] uppedContent =
                                new String(result.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8).getBytes();

                        // 修改后的返回参数应该重置长度，否则如果修改后的参数长度超出原始参数长度时会导致客户端接收到的参数丢失一部分
                        response.getHeaders().setContentLength(uppedContent.length);

                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decorator).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private String response(String result) {
        try {
            Map json = JSONObject.parseObject(result, HashMap.class);
            if (json.containsKey("error")) {
                json.clear();
                json.put("code", json.get("status"));
                json.put("msg", json.get("message"));
                json.put("payload","");
                json.put("timestamp", System.currentTimeMillis());
                result = json.toString();
            }
        } catch (Exception e) {
            log.warn("转换JSON异常：{}", result);
        }
        return result;
    }

    private String out(ServerHttpResponse response) {
        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", 28004);
        message.addProperty("data", "资源访问错误");
        return JSON.toJSONString(message);
    }
}
