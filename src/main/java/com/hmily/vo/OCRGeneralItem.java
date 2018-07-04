package com.hmily.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OCRGeneralItem {
    private String itemstring;  //字段字符串

    private Object itemcoord;		//字段在图像中的像素坐标，包括左上角坐标x,y，以及宽、高width, height

    private ArrayList<OCRGeneralWord> words;	    //字段识别出来的每个字的信息，包括具体的字character，以及字对应的置信度confidence


}
