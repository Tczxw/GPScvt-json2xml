package com.company;

import java.io.*;
import java.util.HashMap;

/**
 * Created by zhou on 17-4-17.
 */
public class json3xml {
    final static String oldPlace = "/home/zhou/target/GPS/GCJ/admin2locality/";
    final static String newPlace = "/home/zhou/target/output/locality/";

    public static String getName(String name) {
        return name.substring(0, name.length() - 4);
    }

    public static void createmm(HashMap<String, String> fi, HashMap<String, String> se, HashMap<String, String> th) {
//        System.out.println(se.size());
//        System.out.println(th.size());
//        for(Map.Entry<String,String>entry:se.entrySet())
//        {
//            System.out.println(entry.getKey()+" "+entry.getValue());
//        }

        System.out.println("---------------------------------------------------");
        File filenew = new File(oldPlace);
        File[] crList = filenew.listFiles();
        try {
            System.out.println("---------------------------------------------------");
            System.out.println(crList.length);
            for (int i = 0; i < crList.length; i++) {
                String name = getName(crList[i].getName());
                String spname = name.substring(0, name.length() - 2);
                int kk = Integer.parseInt(name);
                System.out.println(kk);
//                if (kk % 100 < 80) continue;
//                else System.out.println(kk);
                File f = new File(newPlace + name + ".xml");
                f.createNewFile();
                FileWriter fw = new FileWriter(newPlace + name + ".xml");
                BufferedWriter bf = new BufferedWriter(fw);
                StringBuffer sb = new StringBuffer();
                sb.append("<areas>\n");
                File fileold = new File(oldPlace + name + ".xml");
                String sename = se.get(name);
                sb.append("\t<area>\n");
                sb.append("\t<country_id>1001</country_id>\n");
                sb.append("\t<country_name_zh_cn>中国</country_name_zh_cn>\n");
                sb.append("\t<admin_area_id>" + spname + "</admin_area_id>\n");
                sb.append("\t<admin_area_name_zh_cn>" + fi.get(spname) + "</admin_area_name_zh_cn>\n");
                sb.append("\t<locality_id>" + name + "</locality_id>\n");
                sb.append("\t<locality_name_zh_cn>" + sename + "</locality_name_zh_cn>\n");
                bf.write(sb.toString());
                sb = new StringBuffer();
                FileReader fr = new FileReader(fileold);
                BufferedReader br = new BufferedReader(fr);
                String sst = br.readLine();
                sb.append("\t<boundaries>\n");
                while (sst != null) {
                    sb.append("\t<boundary flag=\"1\">" + sst + "</boundary>\n");
                    sst = br.readLine();
                    bf.write(sb.toString());
                    sb = new StringBuffer();
                }
                sb.append("\t</boundaries>\n");
                fr.close();
                br.close();
                sb.append("\t</area>\n");
                sb.append("</areas>\n");
                bf.write(sb.toString());
                bf.close();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
