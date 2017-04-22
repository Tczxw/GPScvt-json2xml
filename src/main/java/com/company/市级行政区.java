package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhou on 17-4-13.
 */
class 市级行政区 {
    final static String newPlace = "/home/zhou/target";
    final static String oldPlace = "/home/zhou/location";
    HashMap<Integer,Integer>市级;
    HashMap<String,String>市级名字;

    public HashMap<String, String> get市级名字() {
        return 市级名字;
    }

    public HashMap<Integer, Integer> get市级() {
        return 市级;
    }

    public 市级行政区(HashMap<Integer,Integer> fi)
    {
        File file = new File(newPlace+"/index","locality_cn_index.txt");
        HashMap<Integer,Integer>coutner = new HashMap<>();
        try {
            file.createNewFile();
            OutputStream iofile = new FileOutputStream(file);
            FileReader old_file = new FileReader(oldPlace+"/index/locality_cn_index.txt");
            BufferedReader rd = new BufferedReader(old_file);
            String str = rd.readLine();
            市级 = new HashMap<>();
            市级名字 = new HashMap<>();
            while(str!=null)
            {
                String s[] = str.split("\t");
                String news = fi.get(Integer.parseInt(s[0].substring(0,6)))+s[0].substring(6,8);
                int in = Integer.parseInt(news);
                市级.put(Integer.parseInt(s[0]),in);
                String tmp = news+" "+s[1]+'\n';
                市级名字.put(news,s[1]);
                iofile.write(tmp.getBytes());
                str = rd.readLine();
            }
            iofile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
