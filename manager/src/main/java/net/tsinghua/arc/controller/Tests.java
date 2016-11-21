package net.tsinghua.arc.controller;

/**
 * Created by ji on 16-11-21.
 */
public class Tests {
    public static void main(String[] args) {

        String str2 = "0 0 0 * * ?";
        System.out.println(str2.matches("[0-9]{1,2}\\s[0-9]{1,2}\\s[0-9]{1,2}\\s\\*{1}\\s\\*{1}\\s\\?"));

    }
}
