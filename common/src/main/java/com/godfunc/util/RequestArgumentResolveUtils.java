package com.godfunc.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class RequestArgumentResolveUtils {

    public static ObjectMapper objectMapper = new ObjectMapper();

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
        BufferedReader read = null;
        try {
            read = request.getReader();
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = read.readLine()) != null) {
                sb.append(str);
            }
            return objectMapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            log.error("获取请求参数异常", e);
        } finally {
            try {
                if (read != null) {
                    read.close();
                }
            } catch (IOException e) {
                log.error("关闭流异常", e);
            }
        }
        return null;
    }


}
