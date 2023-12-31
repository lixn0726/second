/**
 * @author monstaxl
 **/
/*
 After method: channelRead0 invoked, msg reference will be decreased by 1
 But if we implemented Handler classes by implementing interface: ChannelInbound/OutboundHandler,
      reference count will not be decreased automatically.
 */
package com.notme.second.netty;