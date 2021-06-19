package com.godfunc.util;


import java.util.HashMap;
import java.util.Map;

/**
 * @author godfunc
 * @email godfunc@outlook.com
 * @date 2021/6/16
 */
public class HtmlPlaceholderUtils {

    public static final String startStr = "${";
    public static final String endStr = "}";

    public static String replacePlaceholder(Map<String, Object> params, String html) {
        int fromIndex = 0;
        int startIndex;
        int endIndex = 0;
        StringBuilder sb = new StringBuilder();
        while ((startIndex = html.indexOf(startStr, fromIndex)) != -1) {
            int startOffset = startIndex + startStr.length();
            endIndex = html.indexOf(endStr, startOffset);
            if (endIndex == -1) {
                break;
            }
            String key = html.substring(startOffset, endIndex);
            Object value = params.get(key);
            sb.append(html, fromIndex, startIndex).append(value);
            fromIndex = startOffset;
        }
        return sb.append(html, endIndex + endStr.length(), html.length()).toString();
    }

    public static void main(String[] args) {
        String html = "12312412${b}${a}d${c}sfds${}f";
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("b", "jack");
        String startStr = "${";
        String endStr = "}";

    }
}
