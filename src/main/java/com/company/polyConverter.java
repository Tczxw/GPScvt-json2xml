package com.company;

import java.awt.geom.GeneralPath;
import javax.crypto.spec.GCMParameterSpec;
import javax.print.DocFlavor;

/**
 * Created by zhou on 17-4-14.
 */
public class polyConverter {
    // 将gps坐标中的浮点数转换成整数表示（乘以10^6，取整)
    public static int convertCoordinateToInt(double coordinate) {
        return new Double(coordinate * 1000000).intValue();
    }

    public static StringBuffer conVert(String input)
    {
        String[] str = input.split(";");
        //System.out.println(str.length);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<str.length;i++)
        {
            String[] s = str[i].split(",");
            Double x = Double.parseDouble(s[0]);
            Double y = Double.parseDouble(s[1]);
            GPSconverter.GPS g = GPSconverter.gcj_To_Gps84(y,x);
            sb.append(convertCoordinateToInt(g.getWgLat()));//weidu
            sb.append(",");
            sb.append(convertCoordinateToInt(g.getWgLon()));//jindu
            sb.append(";");
        }
//        System.out.println(sb);
        return sb;
    }
    public static StringBuffer Kvert(String input)
    {
        String[] str = input.split(",");
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<str.length;i+=2)
        {
            Double x = Double.parseDouble(str[i+0]);
            Double y = Double.parseDouble(str[i+1]);
            sb.append(convertCoordinateToInt(y));
            sb.append(",");
            sb.append(convertCoordinateToInt(x));
            sb.append(";");
        }
        return sb;
    }
    public static void pvert(String input)
    {
        //System.out.println(ssk.length());
        String[] str = input.split("\\{");
        for(int i=0;i<str.length;i++)
            if(i!=1&&i%3==1)
            {
                String ssp = str[i].substring(36);
                ssp = ssp.substring(0,ssp.length()-3);
//                System.out.println(ssp);
                System.out.println(Kvert(ssp));
//                str[i].substring(35);
            }
    }
}
