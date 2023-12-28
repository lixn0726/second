package com.notme.second.netty.controlling;

import lombok.Data;

/**
 * @author listen
 **/
@Data
public class NettyBootstrapConfig {

    private String bootstrapId;

    private Integer expectedListenPort;

    private String expectedBoundAddress;

    private Boolean allowRandomPort;

    private String protocol;

}
