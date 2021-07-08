package com.godfunc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class RequestArgumentResolveUtils {

    public static Map<String, Object> getParams(HttpServletRequest request) {
        if (MediaType.APPLICATION_JSON.includes(MediaType.parseMediaType(request.getContentType()))) {
            Map<String, Object> result = requestBody(request);
            if (MapUtils.isEmpty(result)) {
                result = requestParam(request);
            }
            return result;
        } else {
            Map<String, Object> result = requestParam(request);
            if (MapUtils.isEmpty(result)) {
                result = requestBody(request);
            }
            return result;
        }
    }

    public static Map<String, Object> requestParam(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> result = new LinkedHashMap<>();
        parameterMap.forEach((key, value) -> result.put(key, value.length > 0 ? value[0] : null));
        return result;
    }

    public static Map<String, Object> requestBody(HttpServletRequest request) {
        try {
            BufferedReader read = request.getReader();
            String body = IOUtils.readAll(read);
            read.close();
            return JSON.parseObject(body, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            log.error("获取请求参数异常", e);
        }
        return null;
    }


}
