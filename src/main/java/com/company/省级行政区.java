package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by zhou on 17-4-13.
 */
class 省级行政区 {
    final static String newPlace = "/home/zhou/target";
    final static String oldPlace = "/home/zhou/location";
    HashMap<Integer,Integer>省级;
    HashMap<String,String>省级名字;
    public HashMap<Integer,Integer> get省级() {
        return 省级;
    }

    public HashMap<String, String> get省级名字() {
        return 省级名字;
    }

    public 省级行政区()
    {
        File file = new File(newPlace+"/index","admin_cn_index.txt");
        try {
            file.createNewFile();
            OutputStream iofile = new FileOutputStream(file);
            FileReader old_file = new FileReader(oldPlace+"/index/admin_cn_index.txt");
            BufferedReader rd = new BufferedReader(old_file);
            String str = rd.readLine();
            省级 = new HashMap<>();
            省级名字 = new HashMap<>();
            int kk = 100100;
            int i = 1;
            while(str!=null)
            {
                String s[] = str.split("\t");
                省级.put(Integer.parseInt(s[0]),(kk+i));
                //System.out.println(Integer.parseInt(s[0])+","+(kk+i));
                String tmp = String.valueOf(kk+i)+" "+s[1]+'\n';
                省级名字.put(String.valueOf(kk+i),s[1]);
                iofile.write(tmp.getBytes());
                str = rd.readLine();
                i++;
            }
            iofile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
