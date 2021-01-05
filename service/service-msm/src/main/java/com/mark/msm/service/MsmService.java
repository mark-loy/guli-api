package com.mark.msm.service;

import java.util.Map;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/3 19:20
 */
public interface MsmService {
    boolean sendMessageCode(String number, Map<String, Object> codeMap);
}
