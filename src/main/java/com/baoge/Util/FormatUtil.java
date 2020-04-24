package com.baoge.Util;

import java.text.DecimalFormat;

public class FormatUtil {

    public static String fillZero(int number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }

    public static int decodeCompany(String company) {
        try {
            int $length = company.length();
            int $index = Integer.parseInt(company.substring($length - 2, $length - 1));
            int _index = Integer.parseInt(company.substring($length - 1));

            return Integer.parseInt(company.substring($index, $index + _index));
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public static String encodeCompany(int company) {
        return String.format("%d%d%d%d%d", new Object[]{Integer.valueOf((int) ((Math.random() * 9.0D + 1.0D) * 100.0D)), Integer.valueOf(company), Integer.valueOf((int) ((Math.random() * 9.0D + 1.0D) * 10.0D)), Integer.valueOf(3), Integer.valueOf(String.valueOf(company).length())});
    }

    public static void main(String[] args) {
        System.out.println(fillZero(3, "0000"));
        System.out.println(encodeCompany(11));
        System.out.println(decodeCompany(String.valueOf(encodeCompany(6))));
    }
}
