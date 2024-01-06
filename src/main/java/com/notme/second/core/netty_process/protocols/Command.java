package com.notme.second.core.netty_process.protocols;

/**
 * @author monstaxl
 **/
public interface Command {

    // todo: 后续看看能不能做成可以修改的，主要是我不想定义在实现类里面
    byte magicNumber = 0x26;

    default byte magicNumber() {
        return Command.magicNumber;
    }

    Id<?> id();

    CommandCode code();

    CommandHeader header();

}
