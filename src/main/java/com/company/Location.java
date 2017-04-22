package com.company;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by zhou on 17-4-20.
 */
public class Location {
    private static final String INCLUDE_BOUNDARY_FLAT = "1";
    private static final String EXCLUDE_BOUNDARY_FLAT = "2";
    private LocationInfo locationInfo;
    private Rectangle outerRectangle;
    private ArrayList<Polygon> includedPolygons = new ArrayList<>();
    private ArrayList<Polygon> excludedPolygons = new ArrayList<>();

    // 参数是xml资源文件中的一个area节点。
    public Location(Node locationNode) {
        NodeList children = locationNode.getChildNodes();
        locationInfo = new LocationInfo();
        for (int j = 0; j < children.getLength(); j++) {  //暂不支持英文
            Node child = children.item(j);
            String content = child.getTextContent();
            if (child.getNodeName().equals("country_id")) {
                locationInfo.countryId = Integer.valueOf(content);
            } else if (child.getNodeName().equals("country_name#zh_cn")) {
                locationInfo.country = content;
            } else if (child.getNodeName().equals("country_name#en")) {
                locationInfo.country = content;
            } else if (child.getNodeName().equals("admin_area_id")) {
                locationInfo.adminId = Integer.valueOf(content);
            } else if (child.getNodeName().equals("admin_area_name#zh_cn")) {
                locationInfo.admin = content;
            } else if (child.getNodeName().equals("locality_id")) {
                locationInfo.localityId = Integer.valueOf(content);
            } else if (child.getNodeName().equals("locality_name#zh_cn")) {
                locationInfo.locality = content;
            } else if (child.getNodeName().equals("sub_locality_id")) {
                locationInfo.subLocalityId = Integer.valueOf(content);
            } else if (child.getNodeName().equals("sub_locality_name#zh_cn")) {
                locationInfo.subLocality = content;
            } else if (child.getNodeName().equals("boundaries")) {
                NodeList boundaryList = child.getChildNodes();
                for (int i = 0; i < boundaryList.getLength(); ++i) {
                    Node boundary = boundaryList.item(i);
                    if (boundary.getNodeName().equals("boundary")) {
//                        Polygon polygon = parsePolygonFromString(boundary.getTextContent());
                        String flag = boundary.getAttributes().getNamedItem("flag").getTextContent();
//                        Validate.isTrue(flag.equals(INCLUDE_BOUNDARY_FLAT) || flag.equals(EXCLUDE_BOUNDARY_FLAT));
                        if (flag.equals(INCLUDE_BOUNDARY_FLAT)) {
//                            includedPolygons.add(polygon);
                        } else {
//                            excludedPolygons.add(polygon);
                        }
                    }
                }
                //calculateOuterRectangle();
            }
        }
    }
}