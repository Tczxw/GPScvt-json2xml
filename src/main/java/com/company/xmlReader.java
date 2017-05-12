package com.company;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


/**
 * Created by zhou on 17-4-20.
 */

public class xmlReader {


    public static String read_xml(BufferedReader br) {
        try {
            String str = br.readLine();
            String name = new String();
            double dis = 0;
            boolean find = false;
            if(str==null||str.equals("</areas>")) return "</areas>";
            else while(!str.equals("</area>")){
                if(str.contains("poi_name_zh_cn")){
                    int idx = "\t<poi_name_zh_cn>".length();
                    int edx = str.indexOf("</poi_name_zh_cn>");
                    name = str.substring(idx,edx);
                }
                if(str.contains("extension_distance")){
                    int idx = "\t<extension_distance>".length();
                    int edx = str.indexOf("</extension_distance>");
                    dis = Double.parseDouble(str.substring(idx,edx));
                }
                if(str.contains("boundary flag"))
                    find = true;
                str = br.readLine();
            }
            FileWriter fww = new FileWriter("/home/zhou/target/index/IN2000.txt", true);
            FileWriter fwp = new FileWriter("/home/zhou/target/index/OUT2000.txt", true);
            FileWriter fwk = new FileWriter("/home/zhou/target/index/NO_FIND.txt", true);
            if(find) {
                if(dis<2000) fww.write(name+" "+dis+"\n");
                else fwp.write(name+" "+dis+"\n");
            }
            else fwk.write(name+"\n");
            fww.close();
            fwp.close();
            fwk.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args){
        try {
            String path = "/home/zhou/target/output/Tourist_attractions/Tourist_attractions.xml";
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String str = br.readLine();
            while(!str.equals("</areas>")){
                str=read_xml(br);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//        String path = "/home/zhou/target/output/Tourist_attractions/"; //你遍历一下所有文件
//        File filenew = new File(path);
//        File[] crList = filenew.listFiles();
//        for (int i = 0; i < crList.length; i++) {
//            if(crList[i].isDirectory()) continue;
//            System.out.println(crList[i].getPath());
//            loadDataFromXml(crList[i].getPath());
//        }
//    }
//
//
//    private static void loadDataFromXml(String path) throws IOException, ParserConfigurationException, SAXException {
//        InputStream inputStream = null;
//        try {
//            // Loads country location data.
//            inputStream = new FileInputStream(new File(path));
//            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
//            NodeList locationNodes = doc.getDocumentElement().getChildNodes();
//            for (int i = 0; i < locationNodes.getLength(); ++i) {
//                Node locationNode = locationNodes.item(i);
//                parseNode(locationNode);
//            }
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    System.out.println("Failed to close inputStream.\n");
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
//
//
//    private static void parseNode(Node locationNode) {
//        if (!locationNode.getNodeName().equals("area")) {
//            return;
//        }
//
//        Location location = new Location(locationNode);
//        System.out.println(location);
//    }
}