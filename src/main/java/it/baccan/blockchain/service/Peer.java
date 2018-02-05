/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.baccan.blockchain.service;

import it.baccan.blockchain.chain.pojo.Peerdata;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Matteo
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
    public ArrayList<Peerdata> list() {
        return peers;
    }
}
