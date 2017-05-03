package com.company;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by zhou on 17-4-20.
 */

public class xmlReader {


    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String path = "/home/zhou/target/output/Tourist_attractions/"; //你遍历一下所有文件
        File filenew = new File(path);
        File[] crList = filenew.listFiles();
        for (int i = 0; i < crList.length; i++) {
            if(crList[i].isDirectory()) continue;
            System.out.println(crList[i].getPath());
            loadDataFromXml(crList[i].getPath());
        }
    }


    private static void loadDataFromXml(String path) throws IOException, ParserConfigurationException, SAXException {
        InputStream inputStream = null;
        try {
            // Loads country location data.
            inputStream = new FileInputStream(new File(path));
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            NodeList locationNodes = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < locationNodes.getLength(); ++i) {
                Node locationNode = locationNodes.item(i);
                parseNode(locationNode);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Failed to close inputStream.\n");
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private static void parseNode(Node locationNode) {
        if (!locationNode.getNodeName().equals("area")) {
            return;
        }

        Location location = new Location(locationNode);
        System.out.println(location);
    }
}