package com.baoge.Util;

import com.baoge.Util.PO.Distance;


public class MapUtil {
    private static final double EARTH_RADIUS = 6371393; // 平均半径,单位：m

    /**
     * 通过AB点经纬度获取距离
     * @param distanceA A点(经，纬)
     * @param distanceB B点(经，纬)
     * @return 距离(单位：米)
     */
    public static double getDistance(Distance distanceA, Distance distanceB) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(distanceA.getX()); // A经弧度
        double radiansAY = Math.toRadians(distanceA.getY()); // A纬弧度
        double radiansBX = Math.toRadians(distanceB.getX()); // B经弧度
        double radiansBY = Math.toRadians(distanceB.getY()); // B纬弧度

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
//        System.out.println("cos = " + cos); // 值域[-1,1]
        double acos = Math.acos(cos); // 反余弦值
//        System.out.println("acos = " + acos); // 值域[0,π]
//        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        return EARTH_RADIUS * acos; // 最终结果
    }

    public static void main(String[] args) {
        double aX=116.425249;
        double aY=39.914504;
        double bX=116.382001;
        double bY=39.913329;
        Distance distanceA = new Distance(aX,aY);
        Distance distanceB = new Distance(bX,bY);

        System.out.println(getDistance(distanceA,distanceB));
    }
}
