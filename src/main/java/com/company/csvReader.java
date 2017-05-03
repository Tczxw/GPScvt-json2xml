package com.company;

import javax.annotation.processing.Filer;
import java.io.*;
import java.util.HashMap;

/**
 * Created by zhou on 17-5-3.
 */
public class csvReader {

    public static void main(String[] args) {
        HashMap<String,Integer>mp = new HashMap<String, Integer>();
        try {
            FileReader fr = new FileReader("/home/zhou/target/output/csv/tag_info.txt");
            BufferedReader br = new BufferedReader(fr);
            String str = br.readLine();
            while(str!=null){
                String[] s = str.split(",");
                System.out.println(s[1]+" "+s[0]);
                mp.put(s[1],Integer.parseInt(s[0]));
                str = br.readLine();
            }
            FileReader fR = new FileReader("/home/zhou/target/output/csv/tags_894(1).csv");
            BufferedReader bR = new BufferedReader(fR);
            File file = new File("/home/zhou/target/output/csv/tags_894.csv");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            String Str = bR.readLine();
            while(Str!=null){
                int idx = Str.indexOf(",");
                String tmp = Str.substring(0,idx);

                Integer k = mp.get(tmp);
                System.out.println(tmp+" "+k);
                if(k!=null)
                bw.write(k+","+Str+"\n");
                Str = bR.readLine();
            }
            bw.close();
            fw.close();
            fR.close();
            bR.close();
            fr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
