package com.dong.noah.card;


import android.util.Log;

import com.dong.noah.utils.GetDate;
import com.googlecode.openbeans.BeanInfo;
import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

/**
 * @author dong
 */
public class CreateSignature {

    private String TAG = "CreateSignature";

    /**
     * JSON请求参数生成
     *
     * @param uri     接口地址
     * @param mapData data数据
     * @return
     */
    public String verifySign(String uri, HashMap<String, Object> mapData) {


        JsonRequest jsonRequest = new JsonRequest();

        jsonRequest.setRequestId("asasqwer1234asdf");
        jsonRequest.setTimestamp(GetDate.getStamp() + "");
//        jsonRequest.setRequestId("qwer1234asdf");
//        jsonRequest.setTimestamp("1527164676000");
        jsonRequest.setVersion("1.0");
        jsonRequest.setClientId(Constant.CARD_CLIENT_ID);
        jsonRequest.setData(mapData);


        // jsonRequest 转为 map
        Map<String, Object> paramMap = bean2Map(jsonRequest);
        paramMap.remove("sign");
        // 第一步：参数排序
        Map<String, Object> sortedMap = sort(paramMap);

        // 第二步：拼接参数：key1Value1key2Value2
        String urlParams = groupStringParam(sortedMap);

        // 第三部：拼接 URI 和 AppSecret：stringURI + stringParams + AppSecret
        StringBuffer sb = new StringBuffer();
        sb.append(uri).append(urlParams).append(Constant.CARD_SECRET);
        String signContent = sb.toString();

        Log.i(TAG, "拼接字符串 = " + signContent);

        // 第四步，第五步：签名
        String signResult = encryptHMAC(Constant.CARD_SECRET, signContent);
        Log.i(TAG, "签名生成 = " + signResult);

        jsonRequest.setSign(signResult);

        Log.i(TAG, "JSON请求参数—————— = " + jsonRequest.toString());

        return jsonRequest.toString();
    }

    // Bean --> Map 1: 利用 Introspector 和 PropertyDescriptor 将 Bean --> Map
    private Map<String, Object> bean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤 class 属性
                if (!key.equals("class")) {
                    // 得到 property 对应的 getter 方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value == null || "".equals(value)) {
                        continue;
                    }
                    if (key.equals("data")) {
                        Map<String, Object> dataMap = (Map<String, Object>) value;
                        Set<String> keySet = dataMap.keySet();
                        for (String dataKey : keySet) {
                            map.put(dataKey, dataMap.get(dataKey));
                        }
                    } else if (key.equals("page")) {
                        Page page = (Page) value;
                        if (page.getCurrentPage() != null) {
                            map.put("currentPage", page.getCurrentPage());
                        }
                        if (page.getPageSize() != null) {
                            map.put("pageSize", page.getPageSize());
                        }
                    } else {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "bean2Map 抛出异常了 ");
        }
        return map;
    }


    /**
     * 按照红黑树（Red-Black tree）的 NavigableMap 实现 按照字母大小排序
     */
    private Map<String, Object> sort(Map<String, Object> map) {
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
    private String groupStringParam(Map<String, Object> map) {
        if (map == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            if (item.getValue() != null) {
                sb.append(item.getKey());
                sb.append(item.getValue());
            }
        }
        return sb.toString();
    }


    private String encryptHMAC(String key, String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKey secretKey = new SecretKeySpec(key.getBytes("utf-8"), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] data = mac.doFinal(content.getBytes("utf-8"));
            String base64Result = new BASE64Encoder().encode(data);
            String sURLEncoder = URLEncoder.encode(base64Result, "utf-8");
            return sURLEncoder;
        } catch (Exception e) {
            return content;
        }
    }

    public static String encryptHMAC2(String key, String content) {
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
