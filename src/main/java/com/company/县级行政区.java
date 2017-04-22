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
class 县级行政区 {
    final static String newPlace = "/home/zhou/target";
    final static String oldPlace = "/home/zhou/location";
    HashMap<Integer,Integer>县级;
    HashMap<Integer,Integer>市下县城个数;

    public HashMap<String, String> get县级名字() {
        return 县级名字;
    }

    HashMap<String,String>县级名字;
    public HashMap<Integer, Integer> get市下县城个数() {
        return 市下县城个数;
    }

    public HashMap<Integer, Integer> get县级() {
        return 县级;
    }

    public 县级行政区(HashMap<Integer,Integer> se)
    {
//        System.out.println(se.size());
//        for(Map.Entry<Integer,Integer>entry:se.entrySet())
//        {
//            System.out.println(entry.getKey()+" "+entry.getValue());
//        }
        File file = new File(newPlace+"/index","sub_locality_cn_index.txt");
        try {
            file.createNewFile();
            OutputStream iofile = new FileOutputStream(file);
            FileReader old_file = new FileReader(oldPlace+"/index/sub_locality_cn_index.txt");
            BufferedReader rd = new BufferedReader(old_file);
            String str = rd.readLine();
            县级 = new HashMap<>();
            县级名字 = new HashMap<>();
            while(str!=null)
            {
                String s[] = str.split("\t");
                String news = se.get(Integer.parseInt(s[0].substring(0,8)))+s[0].substring(8);
//                System.out.println(Integer.parseInt(s[0].substring(0,6))+" "+s[0].substring(7));
//                System.out.println(fi.get(Integer.parseInt(s[0].substring(0,6))));
                县级.put(Integer.parseInt(s[0]),Integer.parseInt(news));
                String tmp = news+" "+s[1]+'\n';
                县级名字.put(news,s[1]);
//                System.out.println(tmp);
                iofile.write(tmp.getBytes());
                str = rd.readLine();
            }
            iofile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
