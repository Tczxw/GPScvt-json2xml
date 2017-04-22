//import com.sun.xml.internal.ws.util.StringUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.hadoop.hbase.util.Pair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
///**
// * Created by zhou on 17-4-13.
// */
//public class Towgs84 {
//    private static final String apiUrlFormat = "http://api.gpsspg.com/convert/coord/?oid=4590&key=943D115A71E8700B1BFBEBB55F40724E&" +
//            "from=0&to=3&latlng=%s&output=json"; //新华的key
////    private static final String apiUrlFormat = "http://api.gpsspg.com/convert/coord/?oid=4591&key=A021E779239FB10E310055ADB9133538&" +
////            "from=0&to=3&latlng=%s&output=json";　//浩然的key
//
//    public static String invokeApi(String latlng) throws OpFailureException {
//        String requestUrl = String.format(apiUrlFormat, latlng, System.currentTimeMillis());
//        System.out.println("url:" + requestUrl);
//        String response = HttpUtil.httpGet(requestUrl);
//        return response;
//    }
//
//    private static void validateLatlng(String longitude, String latitude) throws NumberFormatException {
//        Float.valueOf(longitude);
//        Float.valueOf(latitude);
//    }
//
//    public static void main(String[] args) throws IOException {
//        List<String> lines = FileUtils.readLines(new File("/home/jxh/Documents/poi/poi_info4.txt"));
//        File file = new File("/home/jxh/Documents/poi/poi_info5.txt");
//        File logFile = new File("/home/jxh/Documents/poi/logFile.txt");
//        for (int i = 0; i < lines.size(); ) {
//            List<String> poiInfos = new ArrayList();
//            StringBuilder latlngs = new StringBuilder();
//            for (int j = 0; j < 15 && i < lines.size(); j++) {
//                String line = lines.get(i);
//                String[] str = StringUtils.split(line, "\t");
//                if (str.length < 7) {
//                    FileUtils.writeStringToFile(logFile, line, true);
//                    continue;
//                }
//                String id = str[0];
//                String name = str[1];
//                String type = str[2];
//                String province = str[3];
//                String city = str[4];
//                String longitude = str[5];
//                String latitude = str[6];
//                try {
//                    validateLatlng(longitude, latitude);
//                } catch (NumberFormatException e) {
//                    FileUtils.writeStringToFile(logFile, line, true);
//                    continue;
//                }
//                String alias = null;
//                if (str.length == 8) {
//                    alias = str[7];
//                }
//                String latlng = latitude + "," + longitude;
//                latlngs.append(latlng + ";");
//                String newInfo = id + "\t" + name + "\t" + type + "\t" + province + "\t" + city + "\t" + "%s" + "\t" + "%s";
//                if (!StringUtils.isEmpty(alias)) {
//                    newInfo += "\t" + alias;
//                }
//                newInfo += "\n";
//                poiInfos.add(newInfo);
//            }
//
//            String latlngInfo = latlngs.toString();
//            try {
//                String response = null;
//                try {
//                    response = invokeApi(StringUtils.substring(latlngInfo, 0, latlngInfo.length() - 1));
//                } catch (OpFailureException e) {
//                    FileUtils.writeStringToFile(logFile, latlngInfo, true);
//                }
//                if (StringUtils.isEmpty(response)) {
//                    FileUtils.writeStringToFile(logFile, latlngInfo, true);
//                }
//
//                List<Pair<String, String>> wgs84 = new ArrayList();
//                try {
//                    JSONObject jsonObjects = new JSONObject(response);
//                    JSONArray jsonArray = jsonObjects.getJSONArray("result");
//                    for (int k = 0; k < jsonArray.length(); k++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(k);
//                        String match = jsonObject.getString("match");
//                        if (match.equals("1")) {
//                            String lat = jsonObject.getString("lat");
//                            String lng = jsonObject.getString("lng");
//                            wgs84.add(Pair.newPair(lng, lat));
//                        } else {
//                            wgs84.add(Pair.newPair(null, null));
//                        }
//                    }
//                } catch (JSONException e) {
//                    FileUtils.writeStringToFile(logFile, latlngInfo, true);
//                }
//
//                if (poiInfos.size() != wgs84.size()) {
//                    FileUtils.writeStringToFile(logFile, latlngInfo, true);
//                } else {
//                    for (int k = 0; k < poiInfos.size(); k++) {
//                        String newPoiInfo = String.format(poiInfos.get(k), wgs84.get(k).getSecond(), wgs84.get(k).getFirst());
//                        FileUtils.writeStringToFile(file, newPoiInfo, true);
//                    }
//                }
//            } catch (IOException e) {
//                System.out.println("write info to file error. latlngInfo:" + latlngInfo);
//            }
//        }
//    }
//}
