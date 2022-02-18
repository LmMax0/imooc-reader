package com.imooc.reader.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    /**
     *
     * @param source 原始字符串
     * @param salt 盐值
     * @return 密文
     */
    public static String md5Digest(String source,Integer salt){
        char[] ca = source.toCharArray(); // 拆分成字符串
        for(int i = 0; i < ca.length; i++){
            ca[i] = (char) (ca[i] + salt);
        }
        String target = new String(ca); // 将字符数组转换为字符串
        String md5 = DigestUtils.md5Hex(target);
        return md5;
    }
}
