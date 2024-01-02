package com.notme.second.namingsrv;

import com.notme.second.architecture.NetworkAddress;
import com.notme.second.namingsrv.enums.RuntimeModel;
import com.sun.corba.se.impl.oa.toa.TOA;

import java.util.ArrayList;
import java.util.List;

/**
 * @author monstaxl
 **/
public class NamingSrv {

    private final RuntimeModel model = RuntimeModel.STAND_ALONE;

    private List<NetworkAddress> theOnline = new ArrayList<>();

    public static boolean standalone(NamingSrv srvRef) {
        return RuntimeModel.STAND_ALONE.equals(srvRef.model);
    }

    // todo: 定个格式
    public static int replyOrder(NamingSrv srvRef, NetworkAddress remoteAddr) {
        List<NetworkAddress> copy = srvRef.theOnline;
        if (copy.contains(remoteAddr)) {
            return (copy.indexOf(remoteAddr));
        }
        copy.add(remoteAddr);
        if (casRefreshTheOnline(srvRef, copy)) {
            return copy.size();
        }
        return -1;
    }

    public static boolean casRefreshTheOnline(NamingSrv srvRef, List<NetworkAddress> addrs) {
        // todo: cas
        srvRef.theOnline = addrs;
        return true;
    }

}
