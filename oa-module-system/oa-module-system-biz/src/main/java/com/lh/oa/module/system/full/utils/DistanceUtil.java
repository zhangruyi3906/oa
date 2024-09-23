package com.lh.oa.module.system.full.utils;

public class DistanceUtil {

    private static final double EARTH_RADIUS = 6378137; //地球半径

    private static final double HL_JD = 102834.74258026089786013677476285; //每经度单位米;
    private static final double JL_WD = 111712.69150641055729984301412873; //每纬度单位米;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个经纬度之间的距离
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return (double) Math.round(s * 10000d) / 10000;
    }

    /**
     * 计算两个经纬度之间的距离
     */
    public static double distance(double lng1, double lat1, double lng2, double lat2) {
        double b = Math.abs((lng1 - lng2) * HL_JD);
        double a = Math.abs((lat1 - lat2) * JL_WD);
        return Math.sqrt((a * a + b * b));
    }

    /**
     * 判断一个点是否在圆形区域内
     */
    public static boolean isInCircleRange(double lng1, double lat1, double lng2, double lat2, int radius) {
        return distance(lng1, lat1, lng2, lat2) <= radius;
    }

}
