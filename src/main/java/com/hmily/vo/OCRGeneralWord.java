package com.hmily.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OCRGeneralWord {

    private String character;	//识别出的单字字符
    private Float confidence;		//识别出的单字字符对应的置信度
}
