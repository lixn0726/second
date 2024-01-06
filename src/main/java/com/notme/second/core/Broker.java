package com.notme.second.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author monstaxl
 **/
public class Broker {

    private final List<MessageQueue> mqs = new ArrayList<>();

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("公众号");
        list.add("why技术");
        System.out.println("before remove list = " + list);
        Iterator<String> ite = list.iterator();
        for (String item : list) {
            if ("why技术".equals(item)) {
                list.remove(item);
            }
        }
        System.out.println("after remove list = " + list);
    }

}
