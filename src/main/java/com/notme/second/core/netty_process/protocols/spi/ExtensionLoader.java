package com.notme.second.core.netty_process.protocols.spi;

/**
 * @author monstaxl
 **/
public class ExtensionLoader {

    public static Class<?> loadExtendImplClasses(Class<?> interfaceClass) {
//        if (interfaceClass.getModifiers())
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("Only support interface implementation extensions.");
        }
        return interfaceClass;
    }

}
