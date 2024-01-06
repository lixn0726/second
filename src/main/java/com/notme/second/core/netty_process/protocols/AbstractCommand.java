package com.notme.second.core.netty_process.protocols;

import com.notme.second.core.netty_process.protocols.exceptions.CommandNotInitializedException;

/**
 * @author monstaxl
 **/
public abstract class AbstractCommand implements Command {

    // codec secret. unmodifiable
    private final byte magicNumber = magicNumber();

    protected final CommandCode code;

    protected final CommandHeader header;

    protected volatile boolean headerInitialized = false;

    public AbstractCommand(CommandCode code, CommandHeader header) {
        this.code = code;
        this.header = header;
    }

    @Override
    public CommandCode code() {
        return code;
    }

    @Override
    public CommandHeader header() {
        if (headerInitialized) {
            return header;
        }
        throw new CommandNotInitializedException();
    }

    protected void buildHeader() {
        doBuildHeader();
        headerInitialized = true;
    }

    protected abstract void doBuildHeader();

}
