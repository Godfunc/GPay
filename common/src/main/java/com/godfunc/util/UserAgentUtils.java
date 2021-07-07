package com.godfunc.util;

import com.godfunc.enums.UserAgentEnum;

import javax.servlet.http.HttpServletRequest;

public class UserAgentUtils {

    private static final String UA_KEY = "User-Agent";

    public static String getUAStr(HttpServletRequest request) {
        return request.getHeader(UA_KEY);
    }

    public static int getUAType(HttpServletRequest request) {
        String uaStr = getUAStr(request);
        if (UserAgentEnum.ANDROID.getPattern().matcher(uaStr).matches()) {
            return UserAgentEnum.ANDROID.getValue();
        } else if (UserAgentEnum.IPHONE.getPattern().matcher(uaStr).matches()) {
            return UserAgentEnum.IPHONE.getValue();
        } else {
            return UserAgentEnum.OTHER.getValue();
        }
    }


}
