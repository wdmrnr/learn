package org.near.commons.coordinate;

/**
 * 坐标点计算
 * 
 * @author wdmrnr
 *
 */
public class PointDistance {

	private static double EARTH_RADIUS = 6378.137;

	/**
	 * 根据两个坐标点的经纬度 获取两点间的米数
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000D;
		s *= 1000;
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
}
