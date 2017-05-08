package com.company;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by zhou on 17-4-27.
 */
public class htmlwriter {
    public static String txtPlace = "/home/zhou/target/index/poi_info.txt";

    public static int knum;

    public static String tourimPlace = "/home/zhou/target/output/Tourist_attractions/";

    public static void Clean() {
        String path = "/home/zhou/target/GPS/Tour/"; //你遍历一下所有文件
        File filenew = new File(path);
        File[] crList = filenew.listFiles();
        for (int i = 0; i < crList.length; i++) {
            if (crList[i].isDirectory()) continue;
//            if (!crList[i].canRead()) crList[i].delete();
            System.out.println(i);
            try {
                FileReader kfr = new FileReader(crList[i].getPath());
                BufferedReader kbr = new BufferedReader(kfr);
                String str = kbr.readLine();
                if (str == null || str.equals("<!DOCTYPE>")||(str.indexOf("未找到") != -1 && str.indexOf("帮您找到") == -1) || str.equals("not found!") || str.indexOf("Return Failure!") != -1)
                    crList[i].delete();
                kfr.close();
                kbr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void xmlwritter(String[] ifm, StringBuffer sb, HashMap<String, Integer> ac) {
        int num = Integer.parseInt(ifm[0]) + 1;
        String name = ifm[1];
//        System.out.print(num+" ");
        sb.append("<area>\n");
        sb.append("\t<poi_id>" + "1001" + "001" + String.format("%07d", num) + "</poi_id>\n");
        sb.append("\t<poi_name_zh_cn>" + name + "</poi_name_zh_cn>\n");
        if (ifm.length == 8)
            sb.append("\t<alias>" + ifm[7] + "</alias>\n");
        sb.append("\t<poi_type>旅游景点</poi_type>\n");
        sb.append("\t<country_name_zh_cn>中国</country_name_zh_cn>\n");
        sb.append("\t<admin_name_zh_cn>" + ifm[3] + "</admin_name_zh_cn>\n");
        sb.append("\t<locality_name_zh_cn>" + ifm[4] + "</locality_name_zh_cn>\n");
        sb.append("\t<center_gps>" + ifm[6] + "," + ifm[5] + "</center_gps>\n");
        System.out.print(num + " ");
        Integer it = ac.get(ifm[4]);
        if (it == null)
//            it = ac.get(ifm[3]);
            System.out.println(ifm[4]);
        int a = polyConverter.convertCoordinateToInt(Double.parseDouble(ifm[5]));
        int b = polyConverter.convertCoordinateToInt(Double.parseDouble(ifm[6]));
        String ss = htmlreader.getHTMLword(name, it, ifm[4], new Point(b, a));
        if (ss.contains("not found")) sb.append("\t<boundaries>\n" +
                "\t</boundaries>\n");
        else sb.append(ss);
        sb.append("</area>\n");
    }

    public static HashMap<String, Integer> cityCode() {
        HashMap<String, Integer> ac = new HashMap<>();
        String path = "/home/zhou/target/index/city_code.txt";
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String str = br.readLine();
            while (str != null) {
                if (str.length() >= 2) {
                    String[] args = str.split(" ");
                    String ss = args[0].replaceAll("　", "");
                    int it = Integer.parseInt(ss);
//                    System.out.println(it);
//                    if (it % 100 == 0) {
//                        System.out.println(args[1]+' '+it);
                    ac.put(args[1], it);
                    File file = new File("/home/zhou/target/GPS/Tour/" + it + "_" + args[1]);
                    file.mkdirs();
                    System.out.println(file.getAbsolutePath());
//                    }
                }
                str = br.readLine();
            }
            System.out.println(ac.size());
//            System.out.println(ac.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ac;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileReader file = new FileReader(txtPlace);
        BufferedReader br = new BufferedReader(file);
//        cityCode();
        try {
//            Clean();
            knum = 0;
            System.out.println(123);
            File writefile = new File(tourimPlace + "Tourist_attractions" + ".xml");
            writefile.createNewFile();
            FileWriter fw = new FileWriter(tourimPlace + "Tourist_attractions" + ".xml");
            BufferedWriter bf = new BufferedWriter(fw);
            StringBuffer sb = new StringBuffer();
            sb.append("<areas>\n");
            int cnt = 0;
            HashMap<String, Integer> ac = cityCode();
            String str = br.readLine();
            while (str != null) {
                cnt++;
//                if(cnt==4624) break;x
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
                xmlwritter(ifm, sb, ac);
//                System.out.println(sb);
//                System.out.println(sb.toString());
                bf.write(sb.toString());
                sb = new StringBuffer();
                str = br.readLine();
            }
            System.out.println(knum);
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
