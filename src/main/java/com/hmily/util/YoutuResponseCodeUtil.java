package com.hmily.util;

import com.hmily.common.YoutuResponseCodeConstant;

public class YoutuResponseCodeUtil {

    public static String getErrorZhMsgByCode(int code) {

        String statusText = null;

        switch (code) {
            case -1100:
                statusText = YoutuResponseCodeConstant.SDK_DISTANCE_ERROR.getErrorZhMsg();
                break;
            case -1101:
                statusText = YoutuResponseCodeConstant.SDK_IMAGE_FACEDETECT_FAILED.getErrorZhMsg();
                break;
            case -1102:
                statusText = YoutuResponseCodeConstant.SDK_IMAGE_DECODE_FAILED.getErrorZhMsg();
                break;
            case -1103:
                statusText = YoutuResponseCodeConstant.SDK_FEAT_PROCESSFAILED.getErrorZhMsg();
                break;
            case -1104:
                statusText = YoutuResponseCodeConstant.SDK_FACE_SHAPE_FAILED.getErrorZhMsg();
                break;
            case -1105:
                statusText = YoutuResponseCodeConstant.SDK_FACE_GENDER_FAILED.getErrorZhMsg();
                break;
            case -1106:
                statusText = YoutuResponseCodeConstant.SDK_FACE_EXPRESSION_FAILED.getErrorZhMsg();
                break;
            case -1207:
                statusText = YoutuResponseCodeConstant.SDK_FACE_AGE_FAILED.getErrorZhMsg();
                break;
            case -1108:
                statusText = YoutuResponseCodeConstant.SDK_FACE_POSE_FAILED.getErrorZhMsg();
                break;
            case -1109:
                statusText = YoutuResponseCodeConstant.SDK_FACE_GLASS_FAILED.getErrorZhMsg();
                break;
            case -1110:
                statusText = YoutuResponseCodeConstant.SDK_FACE_BEAUTY_FAILED.getErrorZhMsg();
                break;
            case -1001:
                statusText = YoutuResponseCodeConstant.TTS_PROCESS_FAILED.getErrorZhMsg();
                break;


            case -1200:
                statusText = YoutuResponseCodeConstant.STORAGE_ERROR.getErrorZhMsg();
                break;
            case -1201:
                statusText = YoutuResponseCodeConstant.CACHE_ERROR.getErrorZhMsg();
                break;

            case -1300:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_EMPTY.getErrorZhMsg();
                break;
            case -1301:
                statusText = YoutuResponseCodeConstant.ERROR_PARAMETER_EMPTY.getErrorZhMsg();
                break;
            case -1302:
                statusText = YoutuResponseCodeConstant.ERROR_PERSON_EXISTED.getErrorZhMsg();
                break;
            case -1303:
                statusText = YoutuResponseCodeConstant.ERROR_PERSON_NOT_EXISTED.getErrorZhMsg();
                break;
            case -1304:
                statusText = YoutuResponseCodeConstant.ERROR_PARAMETER_TOO_LONG.getErrorZhMsg();
                break;
            case -1305:
                statusText = YoutuResponseCodeConstant.ERROR_FACE_NOT_EXISTED.getErrorZhMsg();
                break;
            case -1306:
                statusText = YoutuResponseCodeConstant.ERROR_GROUP_NOT_EXISTED.getErrorZhMsg();
                break;
            case -1307:
                statusText = YoutuResponseCodeConstant.ERROR_GROUPLIST_NOT_EXISTED.getErrorZhMsg();
                break;
            case -1308:
                statusText = YoutuResponseCodeConstant.ERROR_DOWNLOAD_IMAGE_FAILED.getErrorZhMsg();
                break;
            case -1309:
                statusText = YoutuResponseCodeConstant.ERROR_FACE_NUM_EXCEED.getErrorZhMsg();
                break;
            case -1310:
                statusText = YoutuResponseCodeConstant.ERROR_PERSON_NUM_EXCEED.getErrorZhMsg();
                break;
            case -1311:
                statusText = YoutuResponseCodeConstant.ERROR_GROUP_NUM_EXCEED.getErrorZhMsg();
                break;
            case -1312:
                statusText = YoutuResponseCodeConstant.ERROR_SAME_FACE_ADDED.getErrorZhMsg();
                break;
            case -1313:
                statusText = YoutuResponseCodeConstant.ERROR_PARAMETER_INVALID.getErrorZhMsg();
                break;
            case -1400:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_FORMAT_INVALID.getErrorZhMsg();
                break;
            case -1401:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_FUZZY_DETECT_FAILED.getErrorZhMsg();
                break;
            case -1402:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_FOOD_DETECT_FAILED.getErrorZhMsg();
                break;
            case -1403:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_DOWNLOAD_FAILED.getErrorZhMsg();
                break;
            case -1404:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_CLASSIFY_FAILED.getErrorZhMsg();
                break;
            case -1405:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_FINGERPRINT_FAILED.getErrorZhMsg();
                break;
            case -1406:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_COMPARE_FAILED.getErrorZhMsg();
                break;
            case -1408:
                statusText = YoutuResponseCodeConstant.ERROR_DOWNLOAD_IMAGE_SIZE_EXCEED.getErrorZhMsg();
                break;
            case -1409:
                statusText = YoutuResponseCodeConstant.ERROR_IMAGE_UNSATISFACTORY.getErrorZhMsg();
                break;

            case -1505:
                statusText = YoutuResponseCodeConstant.ERROR_IVADLID_URL_FROMAT.getErrorZhMsg();
                break;
            case -1506:
                statusText = YoutuResponseCodeConstant.ERROR_DOWNLOAD_TIMEOUT.getErrorZhMsg();
                break;
            case -1507:
                statusText = YoutuResponseCodeConstant.ERROR_CONNECT_DOWNLOAD_SERVER.getErrorZhMsg();
                break;

            case -2000:
                statusText = YoutuResponseCodeConstant.TEXT_EMPTY.getErrorZhMsg();
                break;
            case -2001:
                statusText = YoutuResponseCodeConstant.TEXT_TOO_LONG.getErrorZhMsg();
                break;

            case -5101:
                statusText = YoutuResponseCodeConstant.OCR_BUF_EMPTY.getErrorZhMsg();
                break;
            case -5103:
                statusText = YoutuResponseCodeConstant.OCR_RECOG_FAILED.getErrorZhMsg();
                break;
            case -5106:
                statusText = YoutuResponseCodeConstant.OCR_CORNER_INCOMPLETE.getErrorZhMsg();
                break;
            case -5107:
                statusText = YoutuResponseCodeConstant.OCR_NOT_IDCARD.getErrorZhMsg();
                break;
            case -5108:
                statusText = YoutuResponseCodeConstant.OCR_IDCARD_ILLEGAL.getErrorZhMsg();
                break;
            case -5109:
                statusText = YoutuResponseCodeConstant.OCR_IMAGE_BLUR.getErrorZhMsg();
                break;

            case -5201:
                statusText = YoutuResponseCodeConstant.OCR_NOT_ENOUGH_TEXTLINES.getErrorZhMsg();
                break;
            case -5202:
                statusText = YoutuResponseCodeConstant.OCR_TEXTLINES_SKEWED.getErrorZhMsg();
                break;
            case -5203:
                statusText = YoutuResponseCodeConstant.OCR_TEXTLINES_FUZZY.getErrorZhMsg();
                break;
            case -5204:
                statusText = YoutuResponseCodeConstant.OCR_UNRECOG_NAME.getErrorZhMsg();
                break;
            case -5205:
                statusText = YoutuResponseCodeConstant.OCR_UNRECOG_TEL.getErrorZhMsg();
                break;
            case -5206:
                statusText = YoutuResponseCodeConstant.OCR_NOT_A_NAMECARD.getErrorZhMsg();
                break;
            case -5207:
                statusText = YoutuResponseCodeConstant.DETECT_AND_RECONG_FAILED.getErrorZhMsg();
                break;
            case -5208:
                statusText = YoutuResponseCodeConstant.OCR_SERVER_INTERN_ERROR.getErrorZhMsg();
                break;

            case -7001:
                statusText = YoutuResponseCodeConstant.NOT_CARD.getErrorZhMsg();
                break;
            case -7002:
                statusText = YoutuResponseCodeConstant.NOT_SECOND_IDCARD.getErrorZhMsg();
                break;
            case -7003:
                statusText = YoutuResponseCodeConstant.NOT_FRONT_IDCARD.getErrorZhMsg();
                break;
            case -7004:
                statusText = YoutuResponseCodeConstant.NOT_BACK_IDCARD.getErrorZhMsg();
                break;
            case -7005:
                statusText = YoutuResponseCodeConstant.IDCARD_FUZZY.getErrorZhMsg();
                break;
            case -7006:
                statusText = YoutuResponseCodeConstant.IDCARD_LIGHT_NOT_BLANCE.getErrorZhMsg();
                break;

            case -9001:
                statusText = YoutuResponseCodeConstant.DLOCR_WRONG_TYPE_INPUT.getErrorZhMsg();
                break;
            case -9002:
                statusText = YoutuResponseCodeConstant.DLOCR_RECONG_FAILED.getErrorZhMsg();
                break;
            case -9003:
                statusText = YoutuResponseCodeConstant.GLOCR_RECONG_FAILED.getErrorZhMsg();
                break;

            case -9010:
                statusText = YoutuResponseCodeConstant.CREDITCARD_OCR_PREPROCESS_ERROR.getErrorZhMsg();
                break;
            case -9011:
                statusText = YoutuResponseCodeConstant.CREDITCARD_OCR_RECOG_FAILED.getErrorZhMsg();
                break;
            case -9012:
                statusText = YoutuResponseCodeConstant.CREDITCARD_OCR_IMAGE_BLUR.getErrorZhMsg();
                break;
            case -9013:
                statusText = YoutuResponseCodeConstant.ERROR_NOT_A_CREDITCARD.getErrorZhMsg();
                break;
            case -9014:
                statusText = YoutuResponseCodeConstant.ERROR_CREDITCARD_ILLEGAL.getErrorZhMsg();
                break;
            case -9016:
                statusText = YoutuResponseCodeConstant.JAPOCR_RECONG_FAILED.getErrorZhMsg();
                break;

            case -9101:
                statusText = YoutuResponseCodeConstant.HANDAR_NO_HANDS.getErrorZhMsg();
                break;
            case -9102:
                statusText = YoutuResponseCodeConstant.HANDAR_HAND_CLS_FAILED.getErrorZhMsg();
                break;

            case -9501:
                statusText = YoutuResponseCodeConstant.BIZLICENSE_OCR_PREPROCESS_FAILED.getErrorZhMsg();
                break;
            case -9502:
                statusText = YoutuResponseCodeConstant.BIZLICENSE_OCR_RECOG_FAILED.getErrorZhMsg();
                break;

            case -9701:
                statusText = YoutuResponseCodeConstant.PLATE_OCR_PREPROCESS_ERROR.getErrorZhMsg();
                break;
            case -9702:
                statusText = YoutuResponseCodeConstant.PLATE_OCR_RECOG_FAILED.getErrorZhMsg();
                break;
        }

        return statusText == null ? "识别出错" : statusText;
    }


}

