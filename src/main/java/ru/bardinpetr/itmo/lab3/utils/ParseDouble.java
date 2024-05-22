package ru.bardinpetr.itmo.lab3.utils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ParseDouble {
    private static final Predicate<String> CHECK_PATTERN = Pattern.compile("^-?\\d+(\\.\\d{1,8})?$").asMatchPredicate();


    public static Double safeParseDouble(String s) {
        if(s == null || !CHECK_PATTERN.test(s))
            return null;
        return Double.parseDouble(s);
    }
}
