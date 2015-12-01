package com.scut.gof.coordinator.main.net;

/**
 * 定义http请求的参数名
 * Created by gjz on 11/2/15.
 */
public class RequestParamName {
    public static final int ERRORCODE_DEFAULT = 10000;
    public static final int ERRORCODE_PERMISSIONDENY = 10001;
    public static final int ERRORCODE_DATABASEEXECPTION = 10002;
    public static final int ERRORCODE_QINIUERROR = 10003;
    public static final int ERRORCODE_TOKENINVALID = 10004;

    public static final String TOKEN = "token";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String GENDER = "gender";

    //project
    public static final String PROJECT_ID = "proid";
    public static final String PROJECT_NAME = "proname";
    public static final String PROJECT_AFFILIATION = "affiliation";
    public static final String PROJECT_PRINCIPALID = "principalid";
    public static final String PROJECT_PLANSTARTTIME = "planstarttime";
    public static final String PROJECT_PLANENDTIME = "planendtime";
    public static final String PROJECT_CATEGORY = "category";
    public static final String PROJECT_DESCRIPTION = "description";
}
