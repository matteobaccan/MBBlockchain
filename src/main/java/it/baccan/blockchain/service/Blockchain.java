/*
 * Copyright (c) 2018 Matteo Baccan
 * Distributed under the MIT software license, see the accompanying
 * file COPYING or http://www.opensource.org/licenses/mit-license.php.
 */
package it.baccan.blockchain.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import it.baccan.blockchain.chain.pojo.Chaindata;
import it.baccan.blockchain.chain.pojo.Peerdata;
import it.baccan.blockchain.chain.pojo.Transaction;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Matteo Baccan
 */
@Service
@Slf4j
public class Blockchain {

    @Autowired
    private Peer peers;

    /**
     *
     */
    public final static int CHAIN_DIFFICULTY = 5;

    // Blockchain
    private final ArrayList<Block> blockchain = new ArrayList<>();

    /**
     *
     * @param block
     * @return
     */
    public boolean addBlock(Block block) {
        blockchain.add(block);
        return true;
    }

    /**
     * Create a block, with the given transaction, and add to the lastBlock
     *
     * @param t
     * @param lastBlock
     * @return
     */
    public Block createBlock(Transaction t, Block lastBlock) {
        // Chain
        Chaindata cd = new Chaindata();
        cd.setTimestamp(System.currentTimeMillis());

        // GSOn serializer
        Gson gson = new GsonBuilder().create();
        String payload = gson.toJson(t);
        cd.setPayload(payload);

        // If lastBlock exists
        if (lastBlock != null) {
            cd.setPreviousHash(lastBlock.hash());
        } else {
            cd.setPreviousHash("");
        }

        Block ret = new Block(cd);
        ret.mineBlock(CHAIN_DIFFICULTY);
        return ret;
    }

    /**
     * Get last blockchain block.
     *
     * @return
     */
    public Block getLastBlock() {
        Block ret = null;
        if (blockchain.size() > 0) {
            ret = blockchain.get(blockchain.size() - 1);
        }
        return ret;
    }

    /**
     * Return all blockchain.
     *
     * @return
     */
    public ArrayList<Block> getChain() {
        return blockchain;
    }

    /**
     * Resolve BlockChain conflicts using the bigger blockchain.
     */
    public void resolveConflicts() {
        ArrayList<Peerdata> apd = peers.list();
        for (Peerdata pd : apd) {
            HttpResponse<JsonNode> chainDataResponse;
            try {
                // TODO ottimizzazione passando prima dall'ultimo blocco
                chainDataResponse = Unirest.get("http://" + pd.getIp() + ":" + pd.getPort() + "/chain/list")
                        .header("accept", "application/json")
                        .asJson();

                JsonNode body = chainDataResponse.getBody();
                JSONArray ja = body.getArray();

                ArrayList<Block> peerBlockchain = new ArrayList<>();
                for (int n = 0; n < ja.length(); n++) {
                    JSONObject jo = (JSONObject) ja.get(n);
                    Chaindata node = new Chaindata();
                    node.setHash((String) jo.get("hash"));
                    node.setPayload((String) jo.get("payload"));
                    node.setPreviousHash((String) jo.get("previousHash"));
                    node.setTimestamp((Long) jo.get("timestamp"));
                    peerBlockchain.add(new Block(node));
                }

                // TODO validazione blockchainpeer
                // Sostituisco sempre
                if (peerBlockchain.size() > blockchain.size()) {
                    blockchain.clear();
                    blockchain.addAll(peerBlockchain);
                }

            } catch (UnirestException ex) {
                log.error("Error readming chain", ex);
            }
        }
    }

}
