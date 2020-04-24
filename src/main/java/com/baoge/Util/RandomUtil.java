package com.baoge.Util;

import java.util.Random;
import java.util.UUID;


public class RandomUtil {
    public static String[] chars = new String[]
            {
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            };


    public static String getShortUuid() {
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int strInteger = Integer.parseInt(str, 16);
            stringBuffer.append(chars[strInteger % 0x3E]);
        }

        return stringBuffer.toString();
    }

    public static String getLongUuid() {
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    public static int getRandom_Six() {
        int flag = new Random().nextInt(999999);
        if (flag < 100000) {
            flag += 100000;
        }
//        System.out.println(flag);
//        int i = (int)(Math.random()*900 + 1000);
//        String myStr = Integer.toString(i);
//        System.out.println(myStr);
        return flag;
    }

    public static void main(String[] args) {
        System.out.println(getShortUuid());
        System.out.println(getRandom_Six());
    }
}
