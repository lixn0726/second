package com.notme.second.architecture;

import java.util.Properties;

/**
 * @author monstaxl
 **/
public interface ConfigurationReader {

    // 用于
    Properties readAsProperties( // todo 抽象一层，比如说，即使是简单的读取配置文件，也按文件类型会有不同的逻辑实现
            String path);

}
