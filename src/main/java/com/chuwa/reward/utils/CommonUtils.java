package com.chuwa.reward.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CommonUtils {

    public static String encode(Object data) {
        byte[] dataBytes = data.toString().getBytes(StandardCharsets.UTF_8);

        return Base64.getEncoder().encodeToString(dataBytes);
    }
}
