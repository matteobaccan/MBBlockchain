/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.baccan.blockchain;

import it.baccan.blockchain.chain.pojo.Peerdata;
import it.baccan.blockchain.service.Peer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Matteo
 */
@RestController
@RequestMapping("/peer")
@Slf4j
public class PeerController {

    @Autowired Peer peers;

    /**
     * Add a peer to the network.
     *
     * @param ip
     * @param port
     * @return
     */
    @GetMapping(value = "/add/{ip}/{port}", produces = "application/json; charset=utf-8")
    public Map<String, String> addPeer(@PathVariable String ip, @PathVariable String port) {
        // Add to peerList
        peers.addPeer(ip, port);

        Map<String, String> ret = new HashMap(2);
        ret.put("result", "ok");

        return ret;
    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/list", produces = "application/json; charset=utf-8")
    public ArrayList<Peerdata> list() {
        return peers.list();
    }

}
