package kr.flab.wiki.core.login.utils;

import com.github.javafaker.Faker;

import java.util.Locale;

public class StringRandomUtils {
    private static final Faker faker = new Faker(new Locale("ko"));
    private StringRandomUtils(){}
    public static Faker getFaker(){
        return faker;
    }
}
