package com.notme.second.architecture.utils;

/**
 * @author monstaxl
 **/
public final class Hooks {

    private static final Runtime VM_RUNTIME = Runtime.getRuntime();

    public static void registerJVMShutDownHook(Thread hook) {
        VM_RUNTIME.addShutdownHook(hook);
    }

}
