package com.notme.second.netty.writer;

import com.notme.second.trans.Message;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author listen
 **/
public class NettyByteBufMessageWriter implements MessageWriter<ByteBuf>  {

    private static final Map<String, Method> orderedMessageFieldGetters = new LinkedHashMap<>();

    static {
        prepareAccessMethodsForMessage();
    }

    private static void prepareAccessMethodsForMessage() {
        Class<Message> msgClass = Message.class;
        List<Method> getters = Arrays.stream(msgClass.getDeclaredMethods())
                .filter(getterPredicate)
                .collect(Collectors.toList());
        getters.forEach(extractFieldNameFromMethod);
    }

    private static final String getterPrefix = "get";

    private static final int basicGetterMethodNameLength = 3;

    private static final Predicate<Method> getterPredicate = method -> {
        String methodName = method.getName();
        return methodName.startsWith(getterPrefix) && methodName.length() > basicGetterMethodNameLength;
    };

    private static final Consumer<Method> extractFieldNameFromMethod = method -> {
        String methodName = method.getName();
        String fieldName = methodName.substring(3, 4).toLowerCase(Locale.ROOT) + methodName.substring(4);
        orderedMessageFieldGetters.put(fieldName, method);
    };

    @Override
    public void writeMsgTo(Message msg, ByteBuf target) {

    }

}
