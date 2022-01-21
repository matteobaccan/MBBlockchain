/*
 * Copyright (c) 2018 Matteo Baccan
 * Distributed under the MIT software license, see the accompanying
 * file COPYING or http://www.opensource.org/licenses/mit-license.php.
 */
package it.baccan.blockchain.service;

import it.baccan.blockchain.chain.pojo.Peerdata;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Matteo Baccan
 */
@Service
@Slf4j
public class Peer {

    // PeerList
    private final ArrayList<Peerdata> peers = new ArrayList<>();

    /**
     *
     * @param ip
     * @param port
     */
    public void addPeer(String ip, String port) {
        Peerdata peer = new Peerdata();
        peer.setIp(ip);
        peer.setPort(port);
        peers.add(peer);
    }

    /**
     *
     * @return
     */
    public List<Peerdata> list() {
        return peers;
    }
}
