package ru.hse.guidehelper.utils;

import okhttp3.OkHttpClient;

public class ClientUtils {
    public static final String url = "http://192.168.0.62:8080";
    public static final String suffTours = "/tours/all";
    public static final String suffUsers = "/users";
    public static final String suffMessages = "/messages";
    public static final String suffChat = "/chat";

    public static OkHttpClient httpClient = new OkHttpClient();
}