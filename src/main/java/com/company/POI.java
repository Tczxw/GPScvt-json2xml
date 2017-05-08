//package com.company;
//
//import com.xiaomi.micloud.common.ExceptionHelper;
//import com.xiaomi.micloud.common.GeneralErrorCode;
//import com.xiaomi.micloud.thrift.OpBadRequestException;
//import com.xiaomi.micloud.thrift.gallery.location.POIType;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by jxh on 16-12-29.
// */
//public class POI extends BasicLocationInfo {
//    private static final Logger LOGGER = LoggerFactory.getLogger(POI.class);
//    private String id;
//    private String name;
//    private POIType type;
//    private double lng;
//    private double lat;
//    private double extensionDistance;
//    private double maxDistance;
//    private double area;
//    private Set<String> alias;
//
//    public POI(Node poiNode) throws OpBadRequestException {
//        NodeList children = poiNode.getChildNodes();
//        locationInfo = new LocationInfo();
//        for (int i = 0; i < children.getLength(); i++) {
//            Node child = children.item(i);
//            String content = child.getTextContent();
//            if (child.getNodeName().equals("poi_id")) {
//                this.id = content;
//            } else if (child.getNodeName().equals("poi_name_zh_cn")) {
//                this.name = content;
//            } else if (child.getNodeName().equals("poi_type")) {
//                this.type = parsePOIType(content);
//            } else if (child.getNodeName().equals("country_name_zn_cn")) {
//                locationInfo.country = content;
//            } else if (child.getNodeName().equals("admin_name_zh_cn")) {
//                locationInfo.admin = content;
//            } else if (child.getNodeName().equals("locality_name_zh_cn")) {
//                locationInfo.locality = content;
//            } else if (child.getNodeName().equals("center_gps")) {
//                parseCenterGPS(content);
//            } else if (child.getNodeName().equals("extension_distance")) {
//                this.extensionDistance = Double.valueOf(content);
//            } else if (child.getNodeName().equals("max_distance")) {
//                this.maxDistance = Double.valueOf(content);
//            } else if (child.getNodeName().equals("square_meters")) {
//                this.area = Double.valueOf(content);
//            } else if (child.getNodeName().equals("alias")) {
//                parseAlias(content);
//            } else if (child.getNodeName().equals("boundaries")) {
//                parseBoudaries(child);
//            }
//        }
//    }
//
//    private POIType parsePOIType(String typeName) {
//        if (typeName.equals("旅游景点")) {
//            return POIType.scenic_spot;
//        }
//        return POIType.unknown;
//    }
//
//    private void parseCenterGPS(String centerGPS) throws OpBadRequestException {
//        String[] lat2lng = StringUtils.split(centerGPS, ",，");
//        if (lat2lng.length != 2) {
//            LOGGER.warn("poi center gps error:{}", centerGPS);
//            throw ExceptionHelper.buildOpBadRequestException(GeneralErrorCode.ParameterError, "poi center gps error.");
//        }
//        this.lat = Double.valueOf(lat2lng[0]);
//        this.lng = Double.valueOf(lat2lng[1]);
//    }
//
//    private void parseAlias(String aliasStr) {
//        String[] aliasNames = StringUtils.split(aliasStr, ",，");
//        this.alias = new HashSet();
//        for (String aliasName : aliasNames) {
//            alias.add(aliasName);
//        }
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public POIType getType() {
//        return type;
//    }
//
//    public double getLng() {
//        return lng;
//    }
//
//    public double getLat() {
//        return lat;
//    }
//
//    public double getExtensionDistance() {
//        return extensionDistance;
//    }
//
//    public double getMaxDistance() {
//        return maxDistance;
//    }
//
//    public double getArea() {
//        return area;
//    }
//
//    public Set<String> getAlias() {
//        return alias;
//    }
//}