package com.company;

import java.io.*;
import java.util.HashMap;

/**
 * Created by zhou on 17-5-3.
 */
public class NoFound {
    public static String place = "/home/zhou/target/index/no_found.txt";

    public static void main(String[] args) {
        try {
            int cnt = 0;
//            HashMap<String, Integer> ac = htmlwriter.cityCode();
            FileReader fw = new FileReader(place);
            BufferedReader bw = new BufferedReader(fw);
            String str = bw.readLine();
            while (str != null) {
                System.out.println(str);
                String[] s = str.split(" ");
                String ss = fun(s[1]);
                if(ss.length()<=4) {str = bw.readLine();continue;}
                File file = new File("/home/zhou/target/GPS/Tour/" + s[0] + "_" + s[2] + "/" + s[1] + ".json");
                if(file.exists()) {str = bw.readLine();continue;}
                String word = htmlreader.getHT(ss, Integer.parseInt(s[0]));
                if(word.contains("too fast")) {System.out.println("too fast!");Thread.sleep(2000);}
                if ((word.indexOf("未找到") != -1 && word.indexOf("帮您找x到") == -1) || word.equals("not found!") || word.indexOf("Return Failure!") != -1 ||word.contains("too fast")) {
                    cnt++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(word);
                } else {
                    System.out.println(s[1] + " " + s[0] + word);
                    file.createNewFile();
                    FileWriter fk = new FileWriter(file);
                    BufferedWriter bk = new BufferedWriter(fk);
                    bk.write(word);
                    bk.close();
                    fk.close();
                }
                str = bw.readLine();
            }
            System.out.println(cnt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String fun(String s) {
        s = s.replace("自然保护区", "");
        s = s.replace("旅游度假区", "");
        s = s.replace("国家级", "");
        s = s.replace("省级", "");
        s = s.replace("市级", "");
        s = s.replace("国家", "");
        s = s.replace("国际", "");
        s = s.replace("中国", "");
        s = s.replace("联票", "");
        s = s.replace("公园", "");
        s = s.replace("名胜", "");
        s = s.replace("上海", "");
        s = s.replace("北京", "");
        s = s.replace("贵阳", "");
        s = s.replace("县博物馆", "博物馆");
        s = s.replace("市博物馆", "博物馆");
        if(s.indexOf("·")!=-1) s = s.substring(0,s.indexOf("·"));
        if (s.indexOf("-") != -1) s = s.substring(0, s.indexOf("-"));
        if (s.indexOf("(") != -1) s = s.substring(0, s.indexOf("("));
        if(s.indexOf("省")!=-1) s = s.substring(s.indexOf("省")+1);
        if(s.indexOf("市")!=-1) s = s.substring(s.indexOf("市")+1);
        if(s.indexOf("县")!=-1) s = s.substring(s.indexOf("县")+1);
        s.replace("纪念馆","");
        if(s.length()>2)
            s = s.substring(0,s.length()-4);
        System.out.println("s: " + s);
        return s;
    }
}
