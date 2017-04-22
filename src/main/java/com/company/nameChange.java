package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by zhou on 17-4-14.
 */
public class nameChange {
    final static String oldPlace1 = "/home/zhou/location/GPS/GCJ/admin2locality/";
    final static String newPlace1 = "/home/zhou/target/GPS/GCJ/admin2locality/";
    final static String oldPlace2 = "/home/zhou/location/GPS/GCJ/locality2SubLocality/";
    final static String newPlace2 = "/home/zhou/target/GPS/GCJ/locality2SubLocality/";
    public static void creatN(String old,String nnew,HashMap<Integer,Integer> reflecter)
    {
        try {
            File file = new File(old);
            File[] fileList = file.listFiles();
//            System.out.println(fileList.length);
            for (int i = 0; i < fileList.length; i++) {
                String s = fileList[i].getName().substring(0, fileList[i].getName().length()-5);
                Integer newName = reflecter.get(Integer.parseInt(s));
                File creatFile = new File(nnew + newName + ".xml");
                creatFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public nameChange(HashMap<Integer, Integer> se, HashMap<Integer, Integer> th) {
        creatN(oldPlace1,newPlace1,se);
        creatN(oldPlace2,newPlace2,th);
    }
}
