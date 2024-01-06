package com.notme.second.core.netty_process.protocols.test;

import com.notme.second.core.netty_process.protocols.Command;

import java.lang.reflect.Field;

/**
 * @author monstaxl
 **/
public class ReflectionWriteMagicNumber {

    public static void main(String[] args) throws Exception {
        Class<Command> cmd = Command.class;
        Field magicNumber = cmd.getDeclaredField("magicNumber");
        magicNumber.setAccessible(true);

    }

}
