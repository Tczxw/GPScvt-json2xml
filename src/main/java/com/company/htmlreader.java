package com.company;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhou on 17-4-26.
 */
public class htmlreader {
    private static String headurl = "http://ditu.amap.com/service/poiInfo?query_type=TQUERY&pagesize=20&pagenum=1&qii=true&cluster_state=5&need_utd=true&utd_sceneid=1000&div=PC1000&addr_poi_merge=true&is_classify=true&zoom=15&city=";
    private static String erStr = "{\"status\":\"2\",\"data\":\"Return Failure!\"}";

    public static String textFormat(String str) {
        if (str.hashCode() == erStr.hashCode()) return str = "not found!";
        String prefix = "\"bounds\":\"";
        String endfix = "\",\"version\":\"";
        int idx = str.indexOf(prefix) + prefix.length();
        int end = str.indexOf(endfix);
        if(end==-1) end = str.indexOf("\",\"total\":\"");
        if (end <= idx) {
            str = "not found!";
            System.out.println(idx+" "+end);
            System.out.println(str);
            return str;
        } else {
            String target = str.substring(idx, end);
            String spx = "\"type\":\"text\",\"id\":\"1013\",\"value\":\"";
            if(str.indexOf(spx)==-1) spx = "\"type\":\"text\",\"value\":\"";
            String spy = "\",\"name\":\"aoi\"";
            String[] impr = str.split(spx);
            ArrayList<String> E = new ArrayList<>();
            for (int i = 1; i < impr.length; i++) {
                int pdx = impr[i].indexOf(spy);
                if (pdx > 0) {
                    String iim = impr[i].substring(0, pdx);
                    char c =iim.charAt(iim.length()-1);
                    if(c>='9'||c<='0') continue;
                    E.add(iim);
                }
            }
            StringBuffer sb = new StringBuffer();
            if (E.size() == 0) {
                String[] impt = target.split(";");
                if (impt.length != 4) System.out.println("impt error!");
                else {
                    String a = impt[0], b = impt[1], c = impt[2], d = impt[3];
                    sb.append("\t<boundary flag=\"1\">" + (b + "," + a) + ";" + (b + "," + c) + ";" + (d + "," + c) + ";" + (d + "," + a) + ";" + (b + "," + a) + "</boundary>\n");
                }
            } else {
                for (int i = 0; i < E.size(); i++) {
                    String[] irm = E.get(i).split("_");
                    StringBuffer spk = new StringBuffer();
                    for (int j = 0; j < irm.length; j++) {
                        String[] ipp = irm[j].split(",");
                        GPSconverter.GPS g = GPSconverter.gcj02_To_Bd09(Double.parseDouble(ipp[1]), Double.parseDouble(ipp[0]));
                        String ss = polyConverter.convertCoordinateToInt(g.getWgLat()) + "," + polyConverter.convertCoordinateToInt(g.getWgLon());
                        if (j != 0) spk.append(";");
                        spk.append(ss);
                    }
                    sb.append("\t" + "<boundary flag=\"1\">" + spk.toString() + "</boundary>\n");
                }
            }
            str = sb.toString();
            return str;
        }
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

    public static String getHTMLword(String s, int citycode,String place) {
        String str = new String();
        int cnt = 0;
        File file = new File("/home/zhou/target/GPS/Tour/" + s + ".json");
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
//                System.out.println(toUtf8String(s));
                String formatUrl =  headurl + citycode + "&keywords=" + toUtf8String(s);
                URL url = new URL(formatUrl);
                URLConnection URLconnection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = httpConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    BufferedReader bufr = new BufferedReader(isr);
                    str = bufr.readLine();
                    if(str.indexOf("too fast")!=-1)
                    {
                        Thread.sleep(1000);
                        System.out.println("slept! 1000ms");
                        return getHTMLword(s,citycode,place);
                    }
                    if (str.hashCode() == erStr.hashCode()||(str.indexOf("未找到")!=-1)) {
                        FileWriter fww= new FileWriter("/home/zhou/target/index/no_found.txt",true);
                        fww.write(place+" "+s+"\n");
                        fww.flush();
                        fww.close();
//                        file.delete();
                    }
                    bw.write(str);
                    bufr.close();
                    bw.close();
                    fw.close();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.out.println("error!");
                        e.printStackTrace();
                    }
                    str = textFormat(str);
                    return str;
                }
            } else {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                str = br.readLine();
                if(str.indexOf("未找到")!=-1||str.equals("not found!")||str.indexOf("Return Failure!")!=-1) {
//                    file.delete();
                    FileWriter fww= new FileWriter("/home/zhou/target/index/no_found.txt",true);
                    fww.write(place+" "+s+"\n");
                    fww.flush();
                    fww.close();
                }
                return textFormat(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "no!";
    }
}
