package com.htr.loan.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class IdGenerator {

    public static String createNewId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createNewIdByPid(String pid, Integer order) {
        String count = String.valueOf(1000 + order);
        String no = count.substring(1, count.length());
        pid = StringUtils.isBlank(pid) ? "" : pid;
        return pid + no;
    }


    public static String create16Id() {

        String id = UUID.randomUUID().toString().replaceAll("-", "");

        return id.substring(0, 16);

    }

}
