package com.htr.loan.Utils;

/**
 * 常量定义工具类
 * Created by dsp on 2015/8/11.
 */
public interface Constants {
    public static final String RESPONSE_CODE="code";
    public static final String RESPONSE_MSG="message";
    public static final String CODE_SUCCESS="200";
    public static final String CODE_FAIL="400";
    public static final String CODE_ERROR="500";
    public static final String MSG_DELETE_FAIL="删除失败!";
    public static final String MSG_DELETE_SUCCESS="删除成功!";


    /**
     * 默认页面显示条数
     */
    public final static Integer DEFAULT_PAGE_SIGE = 15;

    public static final String SEARCH_PREFIX = "search_";

    public static final Boolean RECORD_EXIST = true;
}
