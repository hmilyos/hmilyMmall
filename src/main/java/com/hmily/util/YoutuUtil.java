/**     
 * @文件名称: YoutuUtil.java  
 * @类路径: com.minstone.hardware.ocr.utils  
 * @描述: TODO  
 * @作者：ousm  
 * @时间：2018年5月22日 下午8:07:28  
 * @版本：V1.0     
 */
package com.hmily.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import com.hmily.vo.OCRGeneralItem;
import com.hmily.vo.OCRGeneralResult;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONException;
import org.json.JSONObject;
/*import org.apache.log4j.Logger;
import com.minstone.hardware.ocr.model.OCRGeneralItem;
import com.minstone.hardware.ocr.model.OCRGeneralResult;*/
import com.youtu.sign.Base64Util;
import com.youtu.sign.YoutuSign;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @作者：ousm
 * @创建时间：2018年5月22日 下午8:07:28
 * @版本：V1.0
 */
@Slf4j
public class YoutuUtil {

//	private static final Logger log = Logger.getLogger(YoutuUtil.class);

	private static final String YouTu_APP_ID = "10132125"; // 腾讯优图应用的AppID
	private static final String YouTu_SECRET_ID = "AKIDxnQq20LGYcT6Vi9YAXgaFengdMq7NxXs"; // 腾讯优图应用的SecretID
	private static final String YouTu_SECRET_KEY = "GJ7g292h8LK5cuynOkslQq1wc2C7mCMG"; // 腾讯优图应用的SecretKey
	private static final String YouTu_USER_ID = "928968518"; // 腾讯优图应用的申请人QQ

	private final static String API_YOUTU_END_POINT = "https://api.youtu.qq.com/youtu/";
	private final static String API_YOUTU_CHARGE_END_POINT = "https://vip-api.youtu.qq.com/youtu/";

	// 30 days
	private static int EXPIRED_SECONDS = (30 * 24 * 3600); // 过期时间
	private static String m_appid = YouTu_APP_ID;
	private static String m_secret_id = YouTu_SECRET_ID;
	private static String m_secret_key = YouTu_SECRET_KEY;
	private static String m_end_point = API_YOUTU_END_POINT;
	private static String m_user_id = YouTu_USER_ID;
	private static boolean m_not_use_https = !m_end_point.startsWith("https");  // 优图api使用的不是https
	
	

	public YoutuUtil(String appid, String secret_id, String secret_key,
			String end_point, String user_id) {
		m_appid = appid;
		m_secret_id = secret_id;
		m_secret_key = secret_key;
		m_end_point = end_point;
		m_user_id = user_id;
		m_not_use_https = !end_point.startsWith("https");
	}

    public JSONObject getPlateOcr(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        StringBuffer image_data = new StringBuffer("");
        getBase64FromFile(file, image_data);

        data.put("image", image_data);

        return sendHttpsRequest(data, "ocrapi/plateocr");
    }

    public JSONObject PlateOcrUrl(String image_url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", image_url);

        return sendHttpsRequest(data, "ocrapi/plateocr");
    }

    public JSONObject getBizLicenseOcr(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        StringBuffer image_data = new StringBuffer("");
        getBase64FromFile(file, image_data);

        data.put("image", image_data);

        return sendHttpsRequest(data, "ocrapi/bizlicenseocr");
    }

    public JSONObject getBizLicenseOcrUrl(String image_url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", image_url);

        return sendHttpsRequest(data, "ocrapi/bizlicenseocr");
    }


    public JSONObject getCreditCardOcrUrl(String image_url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", image_url);

        return sendHttpsRequest(data, "ocrapi/creditcardocr");
    }

    public JSONObject getCreditCardOcr(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        StringBuffer image_data = new StringBuffer("");
        getBase64FromFile(file, image_data);

        data.put("image", image_data);

        return sendHttpsRequest(data, "ocrapi/creditcardocr");
    }

    public JSONObject getDriverLicenseOcrUrl(String image_url, int card_type) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", image_url);
        data.put("type", card_type);

        return sendHttpsRequest(data, "ocrapi/driverlicenseocr");
    }

    public JSONObject getDriverLicenseOcr(File file, int card_type) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        StringBuffer image_data = new StringBuffer("");
        getBase64FromFile(file, image_data);

        data.put("image", image_data);
        data.put("type", card_type);

        return sendHttpsRequest(data, "ocrapi/driverlicenseocr");
    }

    public JSONObject getBcOcrUrl(String image_url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", image_url);

        return sendHttpsRequest(data, "ocrapi/bcocr");
    }

    public JSONObject getBcOcr(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        StringBuffer image_data = new StringBuffer("");
        getBase64FromFile(file, image_data);

        data.put("image", image_data);

        return sendHttpsRequest(data, "ocrapi/bcocr");
    }

    public JSONObject getValidateIdcard(String idcard_number, String idcard_name) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("idcard_number", idcard_number);
        data.put("idcard_name", idcard_name);

        return sendHttpsRequest(data, "openliveapi/validateidcard ");
    }

    public JSONObject getIdcardFaceCompare(String idcard_number, String idcard_name, File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("idcard_number", idcard_number);
        data.put("idcard_name", idcard_name);
        StringBuffer image_data = new StringBuffer("");
        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());

        return sendHttpsRequest(data, "openliveapi/idcardfacecompare");
    }

    public JSONObject getIdcardLiveDetectfour(String idcard_number, String idcard_name, String validate_data, File video_file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("idcard_number", idcard_number);
        data.put("idcard_name", idcard_name);
        data.put("validate_data", validate_data);

        StringBuffer video_data = new StringBuffer("");
        getBase64FromFile(video_file, video_data);
        data.put("video", video_data.toString());

        return sendHttpsRequest(data, "openliveapi/idcardlivedetectfour");
    }

    public JSONObject LiveDetectFour(String validate_data, File video_file, File card_file, boolean compare_card) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("validate_data", validate_data);

        StringBuffer video_data = new StringBuffer("");
        getBase64FromFile(video_file, video_data);
        data.put("video", video_data.toString());

        if (compare_card) {
            StringBuffer card_data = new StringBuffer("");
            getBase64FromFile(card_file, card_data);
            data.put("card", card_data.toString());
            data.put("compare_flag", true);
        } else {
            data.put("compare_flag", false);
        }


        return sendHttpsRequest(data, "openliveapi/livedetectfour");
    }

    public JSONObject getLiveGetFour() throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        return sendHttpsRequest(data, "openliveapi/livegetfour");
    }


    public JSONObject getIdCardOcrUrl(String url, int card_type) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("card_type", card_type);
        return sendHttpsRequest(data, "ocrapi/idcardocr");
    }

    public JSONObject getIdCardOcr(File file, int card_type) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());
        data.put("card_type", card_type);
        return sendHttpsRequest(data, "ocrapi/idcardocr");
    }

    //	车辆属性识别
    public JSONObject getCarClassifyUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        return sendHttpsRequest(data, "carapi/carclassify");
    }

    public JSONObject getCarClassify(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());
        return sendHttpsRequest(data, "carapi/carclassify");
    }

    public JSONObject getImageTerrorismUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        return sendHttpsRequest(data, "imageapi/imageterrorism");
    }

    public JSONObject getImageTerrorism(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());
        return sendHttpsRequest(data, "imageapi/imageterrorism");
    }

    public JSONObject getImagePorn(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());
        return sendHttpsRequest(data, "imageapi/imageporn");
    }

    public JSONObject getImagePornUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        return sendHttpsRequest(data, "imageapi/imageporn");
    }

    public JSONObject getImageTag(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());
        return sendHttpsRequest(data, "imageapi/imagetag");
    }

    public JSONObject getImageTagUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        return sendHttpsRequest(data, "imageapi/imagetag");
    }

    public JSONObject getFoodDetect(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());
        return sendHttpsRequest(data, "imageapi/fooddetect");
    }

    public JSONObject getFoodDetectUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", url);
        return sendHttpsRequest(data, "imageapi/fooddetect");
    }

    public JSONObject getFuzzyDetect(File file) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(file, image_data);
        data.put("image", image_data.toString());

        return sendHttpsRequest(data, "imageapi/fuzzydetect");
    }

    public JSONObject getFuzzyDetectUrl(String url) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("url", url);
        return sendHttpsRequest(data, "imageapi/fuzzydetect");
    }

    public JSONObject addFace(String person_id, List<File> files)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();
        List<String> images = new ArrayList<String>();
        for (File imageFile : files) {
            image_data.setLength(0);
            getBase64FromFile(imageFile, image_data);
            images.add(image_data.toString());
        }

        data.put("images", images);
        image_data.setLength(0);

        data.put("person_id", person_id);

        return sendHttpsRequest(data, "api/addface");
    }

    public JSONObject addFaceUrl(String person_id, List<String> url_arr)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("urls", url_arr);
        data.put("person_id", person_id);

        return sendHttpsRequest(data, "api/addface");
    }

    public JSONObject delFace(String person_id, List<String> face_id_arr)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("face_ids", face_id_arr);
        data.put("person_id", person_id);
        return sendHttpsRequest(data, "api/delface");
    }

    public JSONObject getInfo(String person_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("person_id", person_id);
        return sendHttpsRequest(data, "api/getinfo");
    }

    public JSONObject getGroupIds() throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        return sendHttpsRequest(data, "api/getgroupids");
    }


    public JSONObject getPersonIds(String group_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("group_id", group_id);
        return sendHttpsRequest(data, "api/getpersonids");
    }

    public JSONObject getFaceInfo(String face_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("face_id", face_id);
        return sendHttpsRequest(data, "api/getfaceinfo");
    }

    public JSONObject getFaceIds(String person_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("person_id", person_id);
        return sendHttpsRequest(data, "api/getfaceids");
    }

    public JSONObject setInfo(String person_name, String person_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();

        data.put("person_name", person_name);
        data.put("person_id", person_id);
        return sendHttpsRequest(data, "api/setinfo");
    }
	
    public JSONObject delPersonById(String person_id) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("person_id", person_id);

        return sendHttpsRequest(data, "api/delperson");
    }

    public JSONObject getNewPersonByFile(File imageFile, String person_id,
                                         List<String> group_ids) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile, image_data);
        data.put("image", image_data.toString());
        image_data.setLength(0);

        data.put("person_id", person_id);
        data.put("group_ids", group_ids);

        return  sendHttpsRequest(data, "api/newperson");
    }

    public JSONObject getNewPersonByUrl(String url, String person_id,
                                   List<String> group_ids) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);

        data.put("person_id", person_id);
        data.put("group_ids", group_ids);

        return sendHttpsRequest(data, "api/newperson");
    }
    
    public JSONObject getFaceCompareByFile(File imageFile_a, File imageFile_b)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile_a, image_data);
        data.put("imageA", image_data.toString());
        image_data.setLength(0);

        getBase64FromFile(imageFile_b, image_data);
        data.put("imageB", image_data.toString());

        return sendHttpsRequest(data, "api/facecompare");

    }

    public JSONObject getFaceCompareByUrl(String urlA, String urlB)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("urlA", urlA);
        data.put("urlB", urlB);

        return sendHttpsRequest(data, "api/facecompare");
    }

    public JSONObject getFaceVerifyByFile(File imageFile, String person_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile, image_data);
        data.put("image", image_data.toString());
        image_data.setLength(0);

        data.put("person_id", person_id);

        return sendHttpsRequest(data, "api/faceverify");
    }

    public JSONObject getFaceVerifyByUrl(String url, String person_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();

        data.put("url", url);

        data.put("person_id", person_id);

        return sendHttpsRequest(data, "api/faceverify");
    }

    public JSONObject getFaceIdentifyByFile(File imageFile, String group_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile, image_data);
        data.put("image", image_data.toString());
        image_data.setLength(0);

        data.put("group_id", group_id);

        return sendHttpsRequest(data, "api/faceidentify");
    }

    public JSONObject getFaceIdentifyByUrl(String url, String group_id)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("group_id", group_id);

        return sendHttpsRequest(data, "api/faceidentify");
    }

    public JSONObject getMultiFaceIdentifyByFile(File imageFile, String group_id, List<String> group_ids)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile, image_data);
        data.put("image", image_data.toString());
        image_data.setLength(0);
        if (!group_id.isEmpty()) {
            data.put("group_id", group_id);
        } else {
            data.put("group_ids", group_ids);
        }

        return sendHttpsRequest(data, "api/multifaceidentify");
    }

    public JSONObject getMultiFaceIdentifyByUrl(String url, String group_id, List<String> group_ids)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);

        if (!group_id.isEmpty()) {
            data.put("group_id", group_id);
        } else {
            data.put("group_ids", group_ids);
        }
        return sendHttpsRequest(data, "api/multifaceidentify");
    }

    public JSONObject getMultiFaceIdentifyByFile(File imageFile, String group_id, List<String> group_ids, int topn, int min_size)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile, image_data);
        data.put("image", image_data.toString());
        image_data.setLength(0);
        if (!group_id.isEmpty()) {
            data.put("group_id", group_id);
        } else {
            data.put("group_ids", group_ids);
        }
        data.put("topn", topn);
        data.put("min_size", min_size);

        return sendHttpsRequest(data, "api/multifaceidentify");
    }

    public JSONObject getMultiFaceIdentifyByUrl(String url, String group_id, List<String> group_ids, int topn, int min_size)
            throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);

        if (!group_id.isEmpty()) {
            data.put("group_id", group_id);
        } else {
            data.put("group_ids", group_ids);
        }
        data.put("topn", topn);
        data.put("min_size", min_size);

        return sendHttpsRequest(data, "api/multifaceidentify");
    }

    public JSONObject getFaceShapeByFile(File imageFile, int mode) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile, image_data);
        data.put("image", image_data.toString());
        data.put("mode", mode);
        return sendHttpsRequest(data, "api/faceshape");
    }


    public JSONObject getFaceShapeByUrl(String url, int mode) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("mode", mode);
        return sendHttpsRequest(data, "api/faceshape");
    }

    /**
     * 人脸检测（传Base64文件方式）
     * @param image_path
     * @param mode
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public JSONObject getDetectFaceByFile(File imageFile, int mode) throws IOException,
            JSONException, KeyManagementException, NoSuchAlgorithmException {

        StringBuffer image_data = new StringBuffer("");
        JSONObject data = new JSONObject();

        getBase64FromFile(imageFile, image_data);
        data.put("image", image_data.toString());
        data.put("mode", mode);
        JSONObject respose = sendHttpsRequest(data, "api/detectface");

        return respose;
    }



    /**
     * 人脸检测（传url方式）
     * @param url
     * @param mode
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public JSONObject getDetectFaceByUrl(String url, int mode)
            throws IOException, JSONException, KeyManagementException,
            NoSuchAlgorithmException {
        JSONObject data = new JSONObject();
        data.put("url", url);
        data.put("mode", mode);
        return sendHttpsRequest(data, "api/detectface");
    }

	public static JSONObject getGeneralOcrByFile(File imageFile) throws IOException,
			JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();

		StringBuffer image_data = new StringBuffer("");
		getBase64FromFile(imageFile, image_data);
		data.put("image", image_data.toString());

        return sendHttpsRequest(data,"ocrapi/generalocr");
	}

	public static JSONObject getGeneralOcrByUrl(String image_url) throws IOException,
			JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", image_url);
		JSONObject response = sendHttpsRequest(data,"ocrapi/generalocr");
		return response;
	}

	private static void getBase64FromFile(File imageFile, StringBuffer base64)
			throws IOException {
		InputStream in = new FileInputStream(imageFile);
		byte data[] = new byte[(int) imageFile.length()]; // 创建合适文件大小的数组
		in.read(data); // 读取文件中的内容到b[]数组
		in.close();
		base64.append(Base64Util.encode(data));
	}



	private static JSONObject sendHttpsRequest(JSONObject postData, String mothod)
			throws NoSuchAlgorithmException, KeyManagementException,
			IOException, JSONException {
        SSLContext sc = null;
        if(!m_not_use_https){
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());
        }

		StringBuffer mySign = new StringBuffer("");
		YoutuSign.appSign(m_appid, m_secret_id, m_secret_key,
				System.currentTimeMillis() / 1000 + EXPIRED_SECONDS, m_user_id,
				mySign);

		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");

		URL url = new URL(m_end_point + mothod);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        if(!m_not_use_https){
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
        }

		// set header
		connection.setRequestMethod("POST");
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("user-agent", "youtu-java-sdk");
		connection.setRequestProperty("Authorization", mySign.toString());

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "text/json");
		connection.connect();

		// POST请求
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());

		postData.put("app_id", m_appid);
		out.write(postData.toString().getBytes("utf-8"));
		// 刷新、关闭
		out.flush();
		out.close();

		// 读取响应
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "utf-8"));
		String lines;
		StringBuffer resposeBuffer = new StringBuffer("");
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes("utf-8"), "utf-8");
			resposeBuffer.append(lines);
		}
		// System.out.println(resposeBuffer+"\n");
		reader.close();
		// 断开连接
		connection.disconnect();

		JSONObject respose = new JSONObject(resposeBuffer.toString());

		return respose;
	}
	
	
	public static void main(String[] args) {
		JSONObject response = null;
		try {
			response = YoutuUtil.getGeneralOcrByUrl("http://youtu.qq.com/app/img/experience/char_general/icon_id_01.jpg");
		} catch (KeyManagementException e) {
			log.error("getGeneralOcrByUrls KeyManagementException error: " + e);
		} catch (NoSuchAlgorithmException e) {
			log.error("getGeneralOcrByUrls NoSuchAlgorithmException error: {}" + e);
		} catch (IOException e) {
			log.error("getGeneralOcrByUrls IOException error: {}" + e);
		} catch (JSONException e) {
			log.error("getGeneralOcrByUrls JSONException error: {}" + e);
		}
		log.info("response.toString: " + response.toString());
        OCRGeneralResult result =  JsonUtil.string2Obj(response.toString(), OCRGeneralResult.class);
        log.info("result: " + JsonUtil.obj2String(result));
        if(result != null){
        	StringBuffer content = null;
        	log.info(result.getItems().size() + "");
        	log.info(result.getItems().toString());
        	for (OCRGeneralItem item : result.getItems()) {
				if(item != null) {
					//content.append(item.getItemstring());
					log.info("item: " + item.toString());
				}
			}
        	//log.info("content: " + content.toString());
        }
	}

}
