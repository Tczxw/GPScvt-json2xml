package com.company;

import org.apache.commons.lang3.Validate;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jxh on 17-5-5.
 */
public abstract class BasicLocationInfo {
    private static final String INCLUDE_BOUNDARY_FLAT = "1";
    private static final String EXCLUDE_BOUNDARY_FLAT = "2";
    protected LocationInfo locationInfo;
    protected Rectangle outerRectangle;
    protected ArrayList<Polygon> includedPolygons = new ArrayList<>();
    protected ArrayList<Polygon> excludedPolygons = new ArrayList<>();


    // 返回给定的GPS坐标是否落在此区域。
    public boolean contains(int latitude, int longitude) {
        if (outerRectangle.contains(latitude, longitude)) {
            for (int i = 0; i < excludedPolygons.size(); ++i) {
                if (excludedPolygons.get(i).contains(latitude, longitude)) {
                    return false;
                }
            }
            for (int i = 0; i < includedPolygons.size(); ++i) {
                if (includedPolygons.get(i).contains(latitude, longitude)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void parseBoudaries(Node boundaries){
        NodeList boundaryList = boundaries.getChildNodes();
        for (int i = 0; i < boundaryList.getLength(); ++i) {
            Node boundary = boundaryList.item(i);
            if (boundary.getNodeName().equals("boundary")) {
                Polygon polygon = parsePolygonFromString(boundary.getTextContent());
                String flag = boundary.getAttributes().getNamedItem("flag").getTextContent();
                Validate.isTrue(flag.equals(INCLUDE_BOUNDARY_FLAT) || flag.equals(EXCLUDE_BOUNDARY_FLAT));
                if (flag.equals(INCLUDE_BOUNDARY_FLAT)) {
                    includedPolygons.add(polygon);
                } else {
                    excludedPolygons.add(polygon);
                }
            }
        }
        calculateOuterRectangle();
    }

    private void calculateOuterRectangle() {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int i = 0; i < includedPolygons.size(); ++i) {
            Rectangle bounds = includedPolygons.get(i).getBounds();
            minX = Math.min(minX, bounds.x);
            maxX = Math.max(maxX, bounds.x + bounds.width);
            minY = Math.min(minY, bounds.y);
            maxY = Math.max(maxY, bounds.y + bounds.height);
        }
        this.outerRectangle = new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    private Polygon parsePolygonFromString(String pointList) {
        String[] points = pointList.split(";");

        int xpoints[] = new int[points.length];
        int ypoints[] = new int[points.length];
        for (int i = 0; i < points.length; ++i) {
            String[] xy = points[i].split(",");
            xpoints[i] = Integer.parseInt(xy[0]);
            ypoints[i] = Integer.parseInt(xy[1]);
        }
        Validate.isTrue(xpoints[0] == xpoints[points.length - 1]);
        Validate.isTrue(ypoints[0] == ypoints[points.length - 1]);
        return new Polygon(xpoints, ypoints, xpoints.length);
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }
}