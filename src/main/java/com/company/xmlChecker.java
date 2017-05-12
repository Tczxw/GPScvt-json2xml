package com.company;

import java.util.Scanner;

/**
 * Created by zhou on 17-5-10.
 */
public class xmlChecker {


    public static void main(String [] args){
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        while(str!=null) {
            String [] s = str.split(";");
            for(int i=0;i<s.length;i++){
                String [] si = s[i].split(",");
                double lat = Double.parseDouble(si[0])/1000000;
                double lon = Double.parseDouble(si[1])/1000000;
                GPSconverter.GPS g = GPSconverter.gps84_To_Gcj02(lat,lon);
                System.out.println("polygonArr.push(["+g.getWgLon()+", "+g.getWgLat()+"]);");
            }
            str = sc.nextLine();
        }
    }
}
