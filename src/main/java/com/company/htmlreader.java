package com.company;

import javafx.scene.shape.*;
import org.apache.commons.lang.Validate;

import java.awt.*;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhou on 17-4-26.
 */

public class htmlreader {
    private static String headurl = "http://ditu.amap.com/service/poiInfo?query_type=TQUERY&pagesize=20&pagenum=1&qii=true&cluster_state=5&need_utd=true&utd_sceneid=1000&div=PC1000&addr_poi_merge=true&is_classify=true&zoom=18&city=";
    private static String erStr = "{\"status\":\"2\",\"data\":\"Return Failure!\"}";

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    static double getGPSDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137; //地球半径，单位千米
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    private static Polygon parsePolygonFromString(String pointList) {
        String[] points = pointList.split(";");

        int xpoints[] = new int[points.length];
        int ypoints[] = new int[points.length];
        for (int i = 0; i < points.length; ++i) {
            String[] xy = points[i].split(",");
            xpoints[i] = Integer.parseInt(xy[0]);
            ypoints[i] = Integer.parseInt(xy[1]);
        }
        Validate.isTrue(xpoints[0] == xpoints[points.length - 1]);
        Validate.isTrue(ypoints[0] == ypoints[points.length - 1]);
        return new Polygon(xpoints, ypoints, xpoints.length);
    }

    public static double convertCoordinateToDouble(int coordinate) {
        return coordinate / 1000000.0;
    }

    private static boolean less1km(Polygon polygon, Point p, double area) {
        int n = polygon.npoints;
        if (n == 0)
            System.out.println("1231l3k1l2jlkljfdsaf");
        boolean ok = false;
        if (area <= 1) area = 100000;
        for (int i = 0; i < n; i++) {
            double tdis = getGPSDistance(convertCoordinateToDouble(polygon.xpoints[i]), convertCoordinateToDouble(polygon.ypoints[i]), convertCoordinateToDouble(p.x), convertCoordinateToDouble(p.y));
            tdis = Math.abs(tdis);
            if (tdis < area * area)
                ok = true;
            mmax = Math.max(mmax, tdis);
            ddis = Math.min(ddis, tdis);
            System.out.println(tdis+" "+mmax+" "+ddis);
        }
        return ok;
    }

    public static double ddis = 0;
    public static double mmax = 0;
    public static String textFormat(String str, Point po) {
        if (str.hashCode() == erStr.hashCode() || (str.indexOf("未找到") != -1 && str.indexOf("帮您找到") == -1))
            return str = "not found!";
        String prefix = "\"bounds\":\"";
        String endfix = "\",\"version\":\"";
        int idx = str.indexOf(prefix) + prefix.length();
        int end = str.indexOf(endfix);
        if (end == -1) end = str.indexOf("\",\"total\":\"");
        if (end <= idx) {
            str = "not found!";
            System.out.println(idx + " " + end);
            System.out.println(str);
            return str;
        } else {
            String target = str.substring(idx, end);
            String spx = "\"type\":\"text\",\"id\":\"1013\",\"value\":\"";
            int kdx = str.indexOf(spx);
            if (kdx == -1) spx = "\"type\":\"text\",\"value\":\"";
            String spy = "\",\"name\":\"aoi\"";
            if (kdx == -1) spy = "\",\"name\":\"aoi\",\"id\":\"1013\"";
            String[] impr = str.split(spx);
            ArrayList<String> E = new ArrayList<>();
            for (int i = 1; i < impr.length; i++) {
                int pdx = impr[i].indexOf(spy);
                if (pdx > 0) {
                    String iim = impr[i].substring(0, pdx);
//                    System.out.println(iim);
                    char c = iim.charAt(iim.length() - 1);
                    if (c > '9' || c < '0') continue;
                    E.add(iim);
                }
            }
            boolean find = false;
            ddis = Long.valueOf(9223372036854775806L);
            mmax = Long.valueOf(-1);
            double area = 0;
            StringBuffer sb = new StringBuffer();
            StringBuffer sp = new StringBuffer();
            if (E.size() == 0) {
                String[] impt = target.split(";");
                if (impt.length != 4) System.out.println("impt error!");
                else {
                    String a = impt[0], b = impt[1], c = impt[2], d = impt[3];
                    GPSconverter.GPS g1 = GPSconverter.gcj_To_Gps84(Double.parseDouble(b), Double.parseDouble(a));
                    GPSconverter.GPS g2 = GPSconverter.gcj_To_Gps84(Double.parseDouble(d), Double.parseDouble(c));
                    b = String.valueOf(g1.getWgLon());
                    a = String.valueOf(g1.getWgLat());
                    d = String.valueOf(g2.getWgLon());
                    c = String.valueOf(g2.getWgLat());
                    String ss = (b + "," + a) + ";" + (b + "," + c) + ";" + (d + "," + c) + ";" + (d + "," + a) + ";" + (b + "," + a);
                    StringBuffer kb = polyConverter.conVert(ss);
                    Polygon P = parsePolygonFromString(kb.toString());
                    area = Math.abs(polygonArea.GetArea(P));
                    if (P.contains(po))
                        find = true;
                    if (less1km(P, po, area))
                        find = true;
                    sp.append("\t<boundary flag=\"1\">" + kb.toString() + "</boundary>\n");
                    sb.append("\t<square_meters>" + String.format("%.4f", area / 10000) + "</square_meters>\n");
                    sb.append("\t<extension_distance>" + String.format("%.4f", P.contains(po) ? 0 : ddis / 10) + "</extension_distance>\n");
                    if ((P.contains(po) ? 0 : ddis / 10) > 2000) htmlwriter.knum++;
                    sb.append("\t<max_distance>" + String.format("%.4f", mmax/10) + "</max_distance>\n");
                    sb.append("\t<boundaries>\n");
                    sb.append(sp.toString());
                }
            } else {
                boolean inside = false;
                for (int i = 0; i < E.size(); i++) {
                    String[] irm = E.get(i).split("_");
                    StringBuffer spk = new StringBuffer();
                    for (int j = 0; j < irm.length; j++) {
                        String[] ipp = irm[j].split(",");
//                        System.out.println(ipp[0]+" "+ipp[1]);
                        GPSconverter.GPS g = GPSconverter.gcj_To_Gps84(Double.parseDouble(ipp[1]), Double.parseDouble(ipp[0]));
                        String ss = polyConverter.convertCoordinateToInt(g.getWgLat()) + "," + polyConverter.convertCoordinateToInt(g.getWgLon());
//                        String ss = polyConverter.convertCoordinateToInt(Double.parseDouble(ipp[0]))+","+polyConverter.convertCoordinateToInt(Double.parseDouble(ipp[1]));
                        if (j != 0) spk.append(";");
                        spk.append(ss);
                    }
                    Polygon P = parsePolygonFromString(spk.toString());
                    area += Math.abs((long) polygonArea.GetArea(P));
                    if (P.contains(po))
                        find = true;
                    if (less1km(P, po, area))
                        find = true;
                    if (P.contains(po)) inside = true;
                    sp.append("\t" + "<boundary flag=\"1\">" + spk.toString() + "</boundary>\n");
                }
                sb.append("\t<square_meters>" + String.format("%.4f", area / 10000) + "</square_meters>\n");
                sb.append("\t<extension_distance>" + String.format("%.4f", inside ? 0 : ddis / 10) + "</extension_distance>\n");
                if (Math.abs(ddis-mmax)<0.1) htmlwriter.knum++;
                sb.append("\t<max_distance>" + String.format("%.4f", mmax/10) + "</max_distance>\n");
                sb.append("\t<boundaries>\n");
                sb.append(sp.toString());
            }
            if (find == false && ddis > area / 100) sb.append("\t<error>This POI may be incorrect</error>\n");
            str = sb.toString();
            return str;
        }
    }

    public static String LitextFormat(String str) {
        if (str.hashCode() == erStr.hashCode() || (str.indexOf("未找到") != -1 && str.indexOf("帮您找到") == -1))
            return str = "not found!";
        String prefix = "\"bounds\":\"";
        String endfix = "\",\"version\":\"";
        int idx = str.indexOf(prefix) + prefix.length();
        int end = str.indexOf(endfix);
        if (end == -1) end = str.indexOf("\",\"total\":\"");
        if (end <= idx) {
            str = "not found!";
            System.out.println(idx + " " + end);
            System.out.println(str);
            return str;
        } else {
            String target = str.substring(idx, end);
            String spx = "\"type\":\"text\",\"id\":\"1013\",\"value\":\"";
            int kdx = str.indexOf(spx);
            if (kdx == -1) spx = "\"type\":\"text\",\"value\":\"";
            String spy = "\",\"name\":\"aoi\"";
            if (kdx == -1) spy = "\",\"name\":\"aoi\",\"id\":\"1013\"";
            String[] impr = str.split(spx);
            ArrayList<String> E = new ArrayList<>();
            for (int i = 1; i < impr.length; i++) {
                int pdx = impr[i].indexOf(spy);
                if (pdx > 0) {
                    String iim = impr[i].substring(0, pdx);
                    char c = iim.charAt(iim.length() - 1);
                    if (c > '9' || c < '0') continue;
                    E.add(iim);
                }
            }
            StringBuffer sb = new StringBuffer();
            StringBuffer sp = new StringBuffer();
            if (E.size() == 0) {
                String[] impt = target.split(";");
                if (impt.length != 4) System.out.println("impt error!");
                else {
                    String a = impt[0], b = impt[1], c = impt[2], d = impt[3];
                    GPSconverter.GPS g1 = GPSconverter.gcj_To_Gps84(Double.parseDouble(a), Double.parseDouble(b));
                    GPSconverter.GPS g2 = GPSconverter.gcj_To_Gps84(Double.parseDouble(c), Double.parseDouble(d));
                    a = String.valueOf(g1.getWgLon());
                    b = String.valueOf(g1.getWgLat());
                    c = String.valueOf(g2.getWgLon());
                    d = String.valueOf(g2.getWgLat());
                    String ss = (b + "," + a) + ";" + (b + "," + c) + ";" + (d + "," + c) + ";" + (d + "," + a) + ";" + (b + "," + a);
                    StringBuffer kb = polyConverter.conVert(ss);
                    sb.append(sp.toString());
                }
            } else {
                boolean inside = false;
                for (int i = 0; i < E.size(); i++) {
                    String[] irm = E.get(i).split("_");
                    StringBuffer spk = new StringBuffer();
                    for (int j = 0; j < irm.length; j++) {
                        String[] ipp = irm[j].split(",");
                        GPSconverter.GPS g = GPSconverter.gcj_To_Gps84(Double.parseDouble(ipp[1]), Double.parseDouble(ipp[0]));
                        String ss = polyConverter.convertCoordinateToInt(g.getWgLat()) + "," + polyConverter.convertCoordinateToInt(g.getWgLon());
                        if (j != 0) spk.append(";");
                        spk.append(ss);
                    }
                    Polygon P = parsePolygonFromString(spk.toString());
                    sp.append(spk.toString());
                }
                sb.append(sp.toString());
            }
            str = sb.toString();
            return str;
        }
    }

    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        String ss = sc.nextLine();
        String ss = "恭王府";

//        int city = sc.nextInt();
        int city = 110000;
        HashSet<String> s = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String kk = LitextFormat(getHT(ss, city));
            System.out.println(kk);
            s.add(kk);
        }
        System.out.println(s.size());
    }

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
//                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    public static String getHT(String s, int citycode) {
        String str = null;
        try {
            String formatUrl = headurl + citycode + "&keywords=" + toUtf8String(s);
            URL url = new URL(formatUrl);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
//            httpConnection.setRequestProperty("Cookie","guid=eeee-e526-4122-c2ec; UM_distinctid=15ba9a66e4dcd6-01073121c3b168-1c2d1f03-1fa400-15ba9a66e4e11bb; _uab_collina=149328625109851840936247; key=bfe31f4e0fb231d29e1d3ce951e2c780; cna=JFqHEcVZKXkCAdIMeosC8c4P; l=AgsLX/zvd1B2uuHqpPcOdsWdG6X1oB8i; isg=AqCgH4Qm7e-CLFHnpoazJf5hcac9QoRzlOERuBqxbLtOFUA_wrlUA3an2wpv; CNZZDATA1255626299=2009747565-1493197347-%7C1493976360");
            httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            httpConnection.setRequestProperty("amapuuid", "6340343a-4d04-4850-aceb-919baebb20d7");
            httpConnection.setRequestProperty("If-None-Match", "W/\"7288-lv335YwTerfZuRhiU8os7N6CiG4\"W");
//            httpConnection.setRequestProperty("Accept","*/*");
//            httpConnection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
//            httpConnection.setRequestProperty("Accept-Encoding","gzip, deflate, sdch");
//            httpConnection.setRequestProperty("Host","ditu.amap.com");
//            httpConnection.setRequestProperty("Connection","keep-alive");
//            httpConnection.setRequestProperty("X-Requested-With","XMLHttpRequest");
//            httpConnection.setRequestProperty("Referer","http://ditu.amap.com/search?query=%E6%81%AD%E7%8E%8B%E5%BA%9C&city=110000&geoobj=116.322191%7C40.042305%7C116.336943%7C40.048958&zoom=17");
//            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader bufr = new BufferedReader(isr);
                str = bufr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getHTMLword(String s, int citycode, String place, Point po) {
        String str = new String();
        int cnt = 0;
        File file = new File("/home/zhou/target/GPS/Tour/" + citycode + "_" + place + "/" + s + ".json");
        try {
//            if (!file.exists()) {
//                file.createNewFile();
//                FileWriter fw = new FileWriter(file);
//                BufferedWriter bw = new BufferedWriter(fw);
////                System.out.println(toUtf8String(s));
//                str = getHT(s, citycode);
//                if (str.indexOf("too fast") != -1) {
//                    Thread.sleep(1000);
//                    System.out.println("slept! 1000ms");
//                    file.delete();
//                    return "not found!";
////                        return getHTMLword(s,citycode,place);
//                }
////                if (str.hashCode() == erStr.hashCode() || (str.indexOf("未找到") != -1)) {
////                    System.out.println(place + " " + citycode + " " + s);
////                    FileWriter fww = new FileWriter("/home/zhou/target/index/no_found.txt", true);
////                    fww.write(place + " " + s + "\n");
////                    fww.flush();
////                    fww.close();
//////                        file.delete();
////                }
//                bw.write(str);
//                bw.close();
//                fw.close();
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    System.out.println("error!");
//                    e.printStackTrace();
//                }
//                str = textFormat(str,po);
//                if (str == "not found!") file.delete();
//                return str;
//            } else
            {
                if (!file.exists()) {
                    no_found(place, citycode, s);
                    return "not found!";
                }
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                str = br.readLine();
                System.out.println(place + " " + citycode + " " + s);
                if (str == null || (str.indexOf("未找到") != -1 && str.indexOf("帮您找到") == -1) || str.equals("not found!") || str.indexOf("Return Failure!") != -1) {
//                    file.delete();
                    no_found(place, citycode, s);
                    return "not found!";
                }
                str = textFormat(str, po);
                if (str == "not found!") file.delete();
                return str + "\t</boundaries>\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "no!";
    }

    public static void no_found(String place, int citycode, String s) {
        System.out.println(place + " " + citycode + " " + s);
        try {
            FileWriter fww = new FileWriter("/home/zhou/target/index/no_found.txt", true);
            fww.write(citycode + " " + s + " " + place + "\n");
            fww.flush();
            fww.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}