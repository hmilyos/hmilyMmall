/**     
 * @文件名称: CheckInfo.java  
 * @类路径: com.minstone.hardware.ocr.model  
 * @描述: TODO  
 * @作者：ousm  
 * @时间：2018年5月29日 上午9:25:10  
 * @版本：V1.0     
 */ 
package com.hmily.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**  
 * @类功能说明：  要对比的数据
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：明动软件  
 * @作者：ousm  
 * @创建时间：2018年5月29日 上午9:25:10  
 * @版本：V1.0  
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckInfo {

	private String inputContents;
	private String generalResult;
	private List<OCRGeneralResult> contrastData;
	
/*	public String getInputContents() {
		return inputContents;
	}
	public void setInputContents(String inputContents) {
		this.inputContents = inputContents;
	}
	public String getGeneralResult() {
		return generalResult;
	}
	public void setGeneralResult(String generalResult) {
		this.generalResult = generalResult;
	}
	public List<OCRGeneralResult> getContrastData() {
		return contrastData;
	}
	public void setContrastData(List<OCRGeneralResult> contrastData) {
		this.contrastData = contrastData;
	}
	public CheckInfo(String inputContents, String generalResult,
                     List<OCRGeneralResult> contrastData) {
		super();
		this.inputContents = inputContents;
		this.generalResult = generalResult;
		this.contrastData = contrastData;
	}*/
	
	

	
}
