package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by zhou on 17-4-27.
 */
public class htmlwriter {
    public static String txtPlace = "/home/zhou/target/index/poi_info_bd.txt";

    public static String tourimPlace = "/home/zhou/target/output/Tourist_attractions/";

    public static void xmlwritter(String[] ifm, StringBuffer sb,HashMap<String,Integer>ac) {
        int num = Integer.parseInt(ifm[0]) + 1;
        String name = ifm[1];
        System.out.println(num+" "+name);
        sb.append("<area>\n");
        sb.append("\t<poi_id>" + num + "</poi_id>\n");
        sb.append("\t<poi_name_zh_cn>" + name + "</poi_name_zh_cn>\n");
        sb.append("\t<poi_type>旅游景点</poi_type>");
        sb.append("\t<country_name_zh_cn>中国</country_name_zh_cn>\n");
        sb.append("\t<admin_name_zh_cn>" + ifm[3] + "</admin_name_zh_cn>\n");
        sb.append("\t<locality_name_zh_cn>" + ifm[4] + "</locality_name_zh_cn>\n");
        sb.append("\t<center_gps>" + ifm[5]+","+ifm[6] + "</center_gps>\n");
        sb.append("\t<boundaries>\n");
        System.out.println(ifm[3]+" "+ac.get(ifm[3]));
        sb.append(htmlreader.getHTMLword(name,ac.get(ifm[3]),ifm[3]));
        sb.append("\t</boundaries>\n");
        sb.append("</area>\n");
    }

    private static HashMap<String,Integer> cityCode()
    {
        HashMap<String,Integer>ac = new HashMap<>();
        String path = "/home/zhou/target/index/city_code.txt";
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String str = br.readLine();
            while(str!=null){
                if(str.length()>=2) {
                    String [] args = str.split(" ");
                    String ss = args[0].replaceAll("　","");
                    int it = Integer.parseInt(ss);
//                    System.out.println(it);
                    if(it%100==0)
                    {
//                        System.out.println(args[1]+' '+it);
                        ac.put(args[1],it);
                    }
                }
                str = br.readLine();
            }
//            System.out.println(ac.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ac;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileReader file = new FileReader(txtPlace);
        BufferedReader br = new BufferedReader(file);
        try {
            File writefile = new File(tourimPlace + "Tourist_attractions" + ".xml");
            writefile.createNewFile();
            FileWriter fw = new FileWriter(tourimPlace + "Tourist_attractions" + ".xml");
            BufferedWriter bf = new BufferedWriter(fw);
            StringBuffer sb = new StringBuffer();
            sb.append("<areas>\n");
            int cnt = 0;
            HashMap<String,Integer>ac = cityCode();
            String str = br.readLine();
            while (str != null) {
                cnt++;
//                if(cnt==4624) break;
//                Scanner sc = new Scanner(System.in);
//                str = sc.nextLine();
//                System.out.println(str);
                String[] ifm = str.split("\\t");
//                System.out.println(ifm.length);
                if (ifm.length < 7) {
//                    System.out.println(ifm.length);
                    System.out.printf("error!");
                    continue;
                }
//                System.out.println(sb);
                xmlwritter(ifm, sb,ac);
//                System.out.println(sb);
//                System.out.println(sb.toString());
                bf.write(sb.toString());
                sb = new StringBuffer();
                str = br.readLine();
            }
            sb.append("</areas>\n");
            bf.write(sb.toString());
            bf.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            ;
        }
    }
}
