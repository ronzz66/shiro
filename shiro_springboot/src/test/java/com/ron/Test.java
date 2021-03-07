package com.ron;

import org.apache.shiro.crypto.hash.Md2Hash;

public class Test {
    public static void main(String[] args) {
        String zhangsan = new Md2Hash("123456", "lisi", 3).toString();
        System.out.println(zhangsan);
    }
}
