package com.ubtechinc.alpha2ctrlapp.util.bluetooth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ubt on 2017/3/8.
 */

public class BLECommandTool {
    /**
     * 命令版本
     * */
    private static final String COMMAND_VERSION = "1.0";
    /**
     * 命令版本key
     * */
    private static final String COMMAND_VERSION_KEY = "versionName";

    /**
     * 命令版本号
     * */
    private static final String COMMAND_VERSION_CODE = "1";
    /**
     * 命令版本号key
     * */
    private static final String COMMAND_VERSION_CODE_KEY = "versionCode";

    /**
     * 系统类型
     * */
    private static final String CLIENT_TYPE = "1";//代表android
    /**
     * 系统类型key
     * */
    private static final String CLIENT_TYPE_KEY = "client";

    /**
     * 命令类型key
     */
    private static final String COMMAND_KEY = "command";
    /**
     * 回应码
     * */
    private static final String RESPONSE_CODE = "responseCode";
    /**
     * wifi列表key
     * */
    private static final String WIFI_LIST_KEY = "APList";
    private static final String LEVEL_KEY = "Level";
    private static final String SSID_KEY = "SSID";
    private static final String CAPABLITIES_KEY="Capabilities";

    /**
     * 发送wifi信息命令
     * */
    public static final String COMMAND_WIFI_SEND = "1";
    /**
     * wifi列表命令
     * */
    public static final String COMMAND_WIFI_LIST = "2";

    /**
     * 退出蓝牙模式命令
     * */
    public static final String COMMAND_EXIT = "5";

    public static String packExitRequest() {
        HashMap<String, Object> commandMap = getBaseCommandMap();
        commandMap.put(COMMAND_KEY, COMMAND_EXIT);
        return mapToJson(commandMap);
    }

    /**
     * 获取wifi列表请求
     * */
    public static String packWifiListRequest() {
        HashMap<String, Object> commandMap = getBaseCommandMap();
        commandMap.put(COMMAND_KEY, COMMAND_WIFI_LIST);
        return mapToJson(commandMap);
    }

    /**
     * 发送wifi
     * */
    public static String packSendWifiRequest(String ssid, String passwd, String capibilities) {
        HashMap<String, Object> commandMap = getBaseCommandMap();
        commandMap.put(COMMAND_KEY, COMMAND_WIFI_SEND);
        commandMap.put("ssid", ssid);
        commandMap.put("passwd", passwd);
        commandMap.put("cap", capibilities);

        return mapToJson(commandMap);
    }

    public static CommonRsp unpackResponse(String wifiJsonRsp) {
        CommonRsp commonRsp = new CommonRsp();
        try{
            JSONObject jsonObject = new JSONObject(wifiJsonRsp);
            commonRsp.setCommand(jsonObject.getString(COMMAND_KEY));
            if(jsonObject.has(RESPONSE_CODE)) {
                commonRsp.setResponseCode(jsonObject.getString(RESPONSE_CODE));
            }
            if(jsonObject.has(WIFI_LIST_KEY)) {
                JSONArray jsonArray = new JSONArray(jsonObject.getString(WIFI_LIST_KEY));
                int size = jsonArray.length();
                commonRsp.apList = new ArrayList<>();
                for(int i=0; i<size; i++) {
                    JSONObject childObj = (JSONObject)jsonArray.get(i);
                    WifiInfo wifiInfo = new WifiInfo();
                    wifiInfo.setLevel(childObj.getInt(LEVEL_KEY));
                    wifiInfo.setCapabilities(childObj.getString(CAPABLITIES_KEY));
                    wifiInfo.setSSID(childObj.getString(SSID_KEY));

                    commonRsp.apList.add(wifiInfo);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return commonRsp;
    }

    private static final HashMap<String, Object> getBaseCommandMap() {
        HashMap<String, Object> commandMap = new HashMap<String, Object>();
        commandMap.put(COMMAND_VERSION_KEY, COMMAND_VERSION);
        commandMap.put(COMMAND_VERSION_CODE_KEY, COMMAND_VERSION_CODE);
        commandMap.put(CLIENT_TYPE_KEY, CLIENT_TYPE);
        return commandMap;
    }

    private static String mapToJson(HashMap<String, Object> commandMap) {
        StringBuilder sb = new StringBuilder();

        Iterator<Map.Entry<String, Object>> it = commandMap.entrySet().iterator();

        boolean first = true;
        sb.append("{");
        while(it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Object obj = entry.getValue();

            if(!first) {
                sb.append(",");
            }

            String value;
            if(obj instanceof HashMap) {
                value = mapToJson((HashMap)obj);
                sb.append("\"" + key + "\":" + "\"[" + value + "]\"");
            } else {
                value = obj.toString();
                sb.append("\"" + key + "\":\"" + value + "\"");
            }

            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    public static class CommonRsp{
        private String command;
        private String client;
        private List<WifiInfo> apList;
        private String responseCode;

        public void setCommand(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getClient() {
            return client;
        }

        public void setAPList(List<WifiInfo> apList) {
            this.apList = apList;
        }

        public List<WifiInfo> getAPList() {
            return apList;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getResponseCode() {
            return responseCode;
        }
    }

    public static class WifiInfo{
        private int level;
        private String SSID;
        private String cap;

        public WifiInfo(){}

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public void setSSID(String SSID) {
            this.SSID = SSID;
        }

        public String getSSID() {
            return SSID;
        }

        public void setCapabilities(String capabilities) {
            cap = capabilities;
        }

        public String getCapabilities(){
            return cap;
        }
    }
}
