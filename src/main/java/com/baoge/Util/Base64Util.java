package com.baoge.Util;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {
    public static String Base64UtilEncoder(String str) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(str.getBytes());
        return encode;
    }

    public static String Base64UtilDecoder(String str) {
        BASE64Decoder decoder = new BASE64Decoder();
        String decode = null;
        try {
            decode = new String(decoder.decodeBuffer(str));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decode;
    }

    public static void main(String[] args) {
        System.out.println(Base64UtilEncoder("qwer%1.9safsdf"));
        System.out.println(Base64UtilDecoder("MTIzNDU2"));
    }
}
