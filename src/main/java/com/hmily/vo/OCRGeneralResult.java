package com.hmily.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OCRGeneralResult {
    private int errorcode;	//返回状态值
    private String errormsg;	//返回错误消息
    private ArrayList<OCRGeneralItem> items;	//识别出的所有字段信息每个字段包括
    private String session_id;	//保留字段，目前不使用
}
