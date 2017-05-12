package com.company;

import java.io.*;
import java.util.HashSet;

/**
 * Created by zhou on 17-5-9.
 */
public class Compare {

    public static void main(String[] args){
        try {
            FileReader frn = new FileReader("/home/zhou/target/index/NO_FIND.txt");
            FileReader fro = new FileReader("/home/zhou/target/index/no_found.txt");
            BufferedReader brn = new BufferedReader(frn);
            BufferedReader bro = new BufferedReader(fro);
            String str = bro.readLine();
            HashSet<String>hs = new HashSet<>();
            while(str!=null){
                String [] k = str.split(" ");
                System.out.println(k[1]);
                hs.add(k[1]);
                str = bro.readLine();
            }
            FileWriter fwn = new FileWriter("/home/zhou/target/index/无法找到.txt");
            FileWriter fwo = new FileWriter("/home/zhou/target/index/删除关键词找到.txt");
            BufferedWriter bwn = new BufferedWriter(fwn);
            BufferedWriter bwo = new BufferedWriter(fwo);
            str = brn.readLine();
            while(str!=null){
                System.out.println(str);
                if(hs.contains(str)) bwn.write(str+"\n");
                else bwo.write(str+"\n");
                str = brn.readLine();
            }
            bwn.close();bwo.close();
            fwn.close();fwo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
