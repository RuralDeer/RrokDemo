package com.cn.demo.utils;

public class TestParam {
    private static final TestParam ourInstance = new TestParam();

    public static TestParam getInstance() {
        return ourInstance;
    }

    private TestParam() {
    }

    private String param;

    public static String param2;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
