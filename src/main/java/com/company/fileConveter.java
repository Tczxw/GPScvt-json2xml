package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by zhou on 17-4-14.
 */
public class fileConveter {
    final static String oldPlace = "/home/zhou/location/GPS/GCJ/";
    final static String newPlace = "/home/zhou/target/GPS/GCJ/";

    public static void Conveter(HashMap<Integer, Integer> Ref, boolean type) {
        try {
            File fileold = new File(oldPlace+(type?"admin2locality/":"locality2SubLocality/"));
            File[] oldList = fileold.listFiles();
            for (int i = 0; i < oldList.length; i++) {
                BufferedReader reader = new BufferedReader(new FileReader(oldList[i]));
                String str = null;
                while ((str = reader.readLine()) != null) {
                    String poly = "polyline\":\"";
                    int idx = str.indexOf(poly) + poly.length();
                    StringBuffer falstr = new StringBuffer();
                    String ks[] = oldList[i].getName().split("\\.");
//                    System.out.println(ks[0]);
//                    System.out.println(poly.length());
                    if (idx < poly.length()) {
//                        falstr.append(str);
                    } else {
//                        falstr.append(str.substring(0, idx));
                        int ctr = str.indexOf("\",\"center");
//                        System.out.println(idx+" "+ctr);
//                        str = str.replace('|', ',');
                       /* if (type && Integer.parseInt(ks[0]) % 100 < 80) {
                            //System.out.println(ks[0]);
                            //System.out.println(Ref.get(Integer.parseInt(ks[0])));
                        } else*/ {
                            String stk = str.substring(idx, ctr);
                            stk = stk.replace('|','\n');
                            String [] stp = stk.split("\n");
//                            if(stp.length==2) {System.out.println(stp[0]);
//                                System.out.println();
//                                System.out.println();
//                                System.out.println(stp[1]);}
//                            System.out.println(stp.length);
                            for(int k = 0;k<stp.length;k++)
                            {falstr.append(polyConverter.conVert(stp[k]));falstr.append("\n");}
                        }
//                        falstr.append(str.substring(ctr));
                    }
                    long name = Ref.get(Integer.parseInt(ks[0]));
                    if(!type)
                    {
                        String path = newPlace +"locality2SubLocality/"+ name/100+"/";
                        File p = new File(path);
                        if(!p.exists()) p.mkdir();
                        OutputStream iofile = new FileOutputStream(newPlace +"locality2SubLocality/"+ name/100+"/"+name + ".xml");
                        iofile.write(falstr.toString().getBytes());
                    }
                    else
                    {
                        OutputStream iofile = new FileOutputStream(newPlace +"admin2locality/"+name + ".xml");
                        iofile.write(falstr.toString().getBytes());
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
