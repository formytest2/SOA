package com.github.bluecatlee.gs4d.message.api.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;

public class IPUtil {

    public static String getIp(HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }

        return ip;
    }

    public static String getIP() {
        Enumeration netInterfaces = null;

        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;

            while(netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                Enumeration ips = ni.getInetAddresses();

                while(ips.hasMoreElements()) {
                    ip = (InetAddress)ips.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        String _IP = ip.getHostAddress();
                        if (!"127.0.0.1".equals(_IP)) {
                            return _IP;
                        }
                    }
                }
            }

            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getServerIpAddr() {
        String ipAddr = "";

        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();

            while(enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)enumeration.nextElement();
                if (networkInterface.getName().equals("eth0")) {        // eth0是linux系统的第一个物理网卡
                    Enumeration inetAddressEnumeration = networkInterface.getInetAddresses();

                    while(inetAddressEnumeration.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress)inetAddressEnumeration.nextElement();
                        if (!(inetAddress instanceof Inet6Address)) {
                            ipAddr = inetAddress.getHostAddress();
                        }
                    }

                    return ipAddr;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return ipAddr;
    }

}
