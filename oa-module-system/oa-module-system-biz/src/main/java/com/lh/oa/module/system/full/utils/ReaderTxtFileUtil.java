package com.lh.oa.module.system.full.utils;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: YanChen
 * @date: 读取本地txt文件工具类
 */
public class ReaderTxtFileUtil {

    public static List<Map<String,Object>> readerTxt(String filePath){
        List<Map<String,Object>> mapList = new ArrayList<>();
        try {
            //本地
//            String s1 = readTxt(new File("D:\\yearsDate.txt"));
            //测试服
            String s1 = readTxt(new File(filePath));
            List list =  JSON.parseObject(s1,List.class);
            for (Object o : list) {
                Map<String,Object> stringObjectMap = (Map<String, Object>) o;
                mapList.add(stringObjectMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }


    public static String readTxt(File file) throws IOException, IOException {
        String s = "";
        InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        StringBuffer content = new StringBuffer();
        while ((s = br.readLine()) != null) {
            content = content.append(s);
        }
        return content.toString();
    }
}
