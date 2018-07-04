package com.hmily.service.impl;

import com.google.common.collect.Lists;
import com.hmily.service.IFileService;
import com.hmily.util.*;
import com.hmily.vo.CheckInfo;
import com.hmily.vo.OCRGeneralItem;
import com.hmily.vo.OCRGeneralResult;
import com.hmily.vo.OCRGeneralWord;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    @Override
    public String upload(MultipartFile file, String path) {
        String filename = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = filename.substring(filename.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", filename, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        StringBuffer content = new StringBuffer();
        try {
            file.transferTo(targetFile);
            //文件已经上传成功了
            boolean image = ImageCheckUtil.isImage(targetFile);
            log.info("ImageCheckUtil: " + image);
            String picType = ImageCheckUtil.getPicType(Lists.newArrayList(targetFile));
            log.info("picType: " + picType);

//            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            /*JSONObject info = getInfo(targetFile);
            log.info("info: " + info);*/
/*            int errorcode = -1;
            String errormsg = null;
            ArrayList<OCRGeneralItem> items = null;
            String session_id = null;
            try {
                errorcode = info.getInt("errorcode");
                errormsg = info.getString("errormsg");
                JSONArray jsonArray = info.getJSONArray("items");
                session_id = info.getString("session_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

          /*  String infoStr = info.toString();
            log.info("infoStr: " + infoStr);
            OCRGeneralResult temp =  JsonUtil.string2Obj(infoStr, OCRGeneralResult.class);
            log.info("obj2String: " + JsonUtil.obj2String(temp));*/



          /*
            OCRGeneralResult ocrGeneralResult = getGeneralOcrByFile(targetFile);
            log.info(ocrGeneralResult.toString());

            List<OCRGeneralResult> list = Lists.newArrayList();
            list.add(ocrGeneralResult);
            CheckInfo checkInfo = new CheckInfo("输入了XXXX", "总的内容在此" , list);
           log.info("-=============--");
           log.info(JsonUtil.obj2String(checkInfo));


            if(ocrGeneralResult != null && ocrGeneralResult.getErrorcode() == 0){

                for (OCRGeneralItem item: ocrGeneralResult.getItems()) {
                    if(item != null){
                        content.append(item.getItemstring());
                    }
                }
            }
*/
            //已经上传到ftp服务器上

            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常", e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return content.toString();
//        return targetFile.getName();
    }


    public OCRGeneralResult getGeneralOcrByFile(File imageFile){
        JSONObject response = null;
//        StringBuffer content = new StringBuffer();
        try {
            response = YoutuUtil.getGeneralOcrByFile(imageFile);
     /*       int errorcode = -1;
            String errormsg = null;
            JSONArray items = null;
            if(response != null){
                errorcode = response.getInt("errorcode");
                errormsg = response.getString("errormsg");
                items = response.getJSONArray("items");
            }
            if(errorcode == 0 && items != null){
                //response.getJSONArray("face").getJSONObject(0).get("yaw");
                for (int i = 0; i < items.length(); i++) {
                    if(items.getJSONObject(i) != null){
                        Object o = response.getJSONArray("items").getJSONObject(i).get("itemstring");
                        String str = (String)o;
                        log.info(o.toString());
                        log.info(str);
                        content.append(str);
                    }
                }
            }*/
        } catch (KeyManagementException e) {
            log.error("getGeneralOcrByFiles KeyManagementException error: {}", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("getGeneralOcrByFiles NoSuchAlgorithmException error: {}", e);
        } catch (IOException e) {
            log.error("getGeneralOcrByFiles IOException error: {}", e);
        } catch (JSONException e) {
            log.error("getGeneralOcrByFiles JSONException error: {}", e);
        }

//        log.info("content: " + content.toString());
        log.info("response.toString: " + response.toString());
        OCRGeneralResult result =  JsonUtil.string2Obj(response.toString(), OCRGeneralResult.class);
        log.info("result: " + JsonUtil.obj2String(result));
        return result;
    }

}
