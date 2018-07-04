package com.hmily.util;

import com.hmily.common.YouTuConfigConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
    public CheckUtil(){

    }

    public static String getIdcardocr(String values) {
        String str = "\\d{6}((19|20)\\d{2})((0[0-9])|(1[0-2]))(((0|1|2)[0-9])|(3[0,1]))\\d{3}[xX\\d]";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(StringUtils.deleteWhitespace(values));
        if (matcher.find()) {
            return matcher.group();
        }
        return YouTuConfigConstant.FOUNT_RESULT_FALSE;
    }

    public static void main(String[] args) {

        String test = "asdsdfdsf sf dsfdsfsdf safasf 撒旦撒旦写个你的身份证发射的5125 011975   0604517x佛挡杀佛速度斯蒂芬的萨芬的萨芬斯蒂芬发实打实的发";
        System.out.println(getIdcardocr(test));

    }
}