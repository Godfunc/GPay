package com.godfunc.util;

import javax.servlet.http.HttpServletRequest;

public class HostUtils {

    public static String getFullHost(HttpServletRequest request) {
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contentPath = request.getContextPath();
        if (80 == serverPort) {
            return "http://" + serverName + "/" + contentPath;
        } else if (443 == serverPort) {
            return "https://" + serverName + "/" + contentPath;
        } else {
            return "http://" + serverName + ":" + serverPort + "/" + contentPath;
        }
    }
}
