/*
 * Copyright (c) 2018 Matteo Baccan
 * Distributed under the MIT software license, see the accompanying
 * file COPYING or http://www.opensource.org/licenses/mit-license.php.
 */
package it.baccan.blockchain.controller;

import it.baccan.blockchain.chain.pojo.Transaction;
import it.baccan.blockchain.service.Block;
import it.baccan.blockchain.service.Blockchain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Matteo Baccan
 */
@RestController
@RequestMapping("/chain")
@Slf4j
public class BlockchainController {

    @Autowired Blockchain blockchain;

    /**
     * Return the blockchain list
     *
     * @return
     */
    @GetMapping(value = "/list", produces = "application/json; charset=utf-8")
    public ArrayList<Block> chain() {
        return blockchain.getChain();
    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/lastblock", produces = "application/json; charset=utf-8")
    public Block lastblock() {
        return blockchain.getLastBlock();
    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/resolveConflicts", produces = "application/json; charset=utf-8")
    public Map<String, String> resolveConflicts() {
        blockchain.resolveConflicts();
        // Return
        Map<String, String> ret = new HashMap(2);
        ret.put("result", "ok");

        return ret;
    }

    /**
     * Add a Fake transaction to the blockchain.
     *
     * TODO: remove when blockchain in finish.
     *
     * @return
     */
    @GetMapping(value = "/addFakeTransaction", produces = "application/json; charset=utf-8")
    public Map<String, String> addFakeTransaction() {
        // Transaction to add
        Transaction t = new Transaction();
        t.setSender("Matteo");
        t.setReceiver("Giovanni");
        t.setAmount(new Random().nextInt());

        // Chain        
        Block block = blockchain.createBlock(t, blockchain.getLastBlock() );
        blockchain.addBlock(block);

        // Return
        Map<String, String> ret = new HashMap(2);
        ret.put("result", "ok");

        return ret;
    }

}
