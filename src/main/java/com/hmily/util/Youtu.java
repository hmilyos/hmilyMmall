package com.hmily.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import com.youtu.sign.*;

import java.io.IOException;
import java.io.DataOutputStream;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * @author tyronetao
 */
public class Youtu {

    /**
     * 根据返回的status判断是哪种类型错误
     * @param status
     * @return
     */
    public String StatusText(int status) {

        String statusText = "UNKOWN";

        switch (status) {
            case 0:
                statusText = "CONNECT_FAIL";
                break;
            case 200:
                statusText = "HTTP OK";
                break;
            case 400:
                statusText = "BAD_REQUEST";
                break;
            case 401:
                statusText = "UNAUTHORIZED";
                break;
            case 403:
                statusText = "FORBIDDEN";
                break;
            case 404:
                statusText = "NOTFOUND";
                break;
            case 411:
                statusText = "REQ_NOLENGTH";
                break;
            case 423:
                statusText = "SERVER_NOTFOUND";
                break;
            case 424:
                statusText = "METHOD_NOTFOUND";
                break;
            case 425:
                statusText = "REQUEST_OVERFLOW";
                break;
            case 500:
                statusText = "INTERNAL_SERVER_ERROR";
                break;
            case 503:
                statusText = "SERVICE_UNAVAILABLE";
                break;
            case 504:
                statusText = "GATEWAY_TIME_OUT";
                break;
        }
        return statusText;
    }














}
