/**     
 * @文件名称: TrustAnyHostnameVerifier.java  
 * @类路径: com.minstone.hardware.ocr.utils  
 * @描述: TODO  
 * @作者：ousm  
 * @时间：2018年5月22日 下午8:34:03  
 * @版本：V1.0     
 */ 
package com.hmily.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
/**  
 * @类功能说明：  
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：adam  
 * @作者：ousm  
 * @创建时间：2018年5月22日 下午8:34:03  
 * @版本：V1.0  
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

	 public boolean verify(String hostname, SSLSession session) {
         return true;
     }
}
