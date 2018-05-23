package com.dong.noah.card;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dong.noah.utils.GetDate;

import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class CreateSignature2 {

    private static String TAG = "CreateSignature2";

    public static HashMap<String, Object> EncryptUtil(HashMap<String, Object> map, String uri) {


        map.put("requestId", "QWERASDF1234DONG");
        map.put("timestamp", GetDate.getStamp() + "");
        map.put("version", "1.0");
        map.put("clientId", Constant.CARD_CLIENT_ID);

        // 先排序
        Map<String, Object> sortMap = sort(map);


        for (String key : sortMap.keySet()) {
            Log.i(TAG, "排序之后——" + key + " ****** " + sortMap.get(key));
        }

        // 再按格式key1value1key2value2…拼接以上参数
        String tempStr = groupStringParam(sortMap);

        Log.i(TAG, "拼接 = " + tempStr);

        // URL + 参数键值对 + 秘钥
        String str = uri + tempStr + Constant.CARD_SECRET;
        Log.i(TAG, "URL和密钥拼接" + str);


        // HMAC加密
        String sign = encryptHMAC(Constant.CARD_SECRET, str);

        map.put("sign", sign);

        return map;
    }


    /**
     * 按照红黑树（Red-Black tree）的 NavigableMap 实现 按照字母大小排序
     */
    public static Map<String, Object> sort(Map<String, Object> map) {
        if (map == null)
            return null;
        Map<String, Object> result = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        result.putAll(map);
        return result;
    }

    /**
     * 组合参数
     *
     * @param map
     * @return 如：key1Value1Key2Value2....
     */
    public static String groupStringParam(Map<String, Object> map) {
        if (map == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            if (item.getValue() != null) {
                sb.append(item.getKey());
                if (item.getValue() instanceof List) {
                    sb.append(JSON.toJSONString(item.getValue()));
                } else {
                    sb.append(item.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * HMAC加密
     *
     * @param key     私钥
     * @param content 加密内容
     * @return
     * @throws Exception
     */
    public static String encryptHMAC(String key, String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] data = mac.doFinal(content.getBytes("UTF-8"));
            String base64Result = new BASE64Encoder().encode(data);
            return URLEncoder.encode(base64Result, "UTF-8");
        } catch (Exception e) {
            return content;
        }
    }

}
