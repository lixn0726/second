package com.notme.second.architecture.utils;

import java.util.Objects;

/**
 * @author monstaxl
 **/
public class Strings {

    public static boolean notBlank(final String s) {
        return !blank(s);
    }

    public static boolean blank(final String s) {
        return Objects.isNull(s) || empty(s);
    }

    public static boolean empty(final String s) {
        return s.isEmpty();
    }

}
