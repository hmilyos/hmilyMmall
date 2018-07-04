/**     
 * @文件名称: TrustAnyTrustManager.java  
 * @类路径: com.minstone.hardware.ocr.utils  
 * @描述: TODO  
 * @作者：ousm  
 * @时间：2018年5月22日 下午8:32:05  
 * @版本：V1.0     
 */ 
package com.hmily.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**  
 * @类功能说明：  
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @作者：ousm  
 * @创建时间：2018年5月22日 下午8:32:05  
 * @版本：V1.0  
 */
public class TrustAnyTrustManager implements X509TrustManager {
	
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }

}
