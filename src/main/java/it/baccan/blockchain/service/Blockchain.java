/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Matteo
 */
@Service
@Slf4j
public class Blockchain {

    @Autowired Peer peers;

    // Blockchain
    private final ArrayList<Chaindata> blockchain = new ArrayList<>();

    /**
     * Blockcain constructor.
     */
    public Blockchain() {
    }

    /**
     *
     * @param cd
     * @return
     */
    public boolean addBlock(Chaindata cd) {
        blockchain.add(cd);
        return true;
    }

    /**
     * Create a block, with the given transaction, and add to the lastBlock
     *
     * @param t
     * @param lastBlock
     * @return
     */
    public Chaindata createBlock(Transaction t, Chaindata lastBlock) {
        // Chain
        Chaindata cd = new Chaindata();
        cd.setTimestamp(System.currentTimeMillis());

        // GSOn serializer
        Gson gson = new GsonBuilder().create();
        String payload = gson.toJson(t);        
        cd.setPayload(payload);
        
        // If lastBlock exists
        if (lastBlock != null) {
            cd.setPreviousHash(lastBlock.getHash());
            cd.setIndex(lastBlock.getIndex() + 1);
        } else {
            cd.setPreviousHash("");
            cd.setIndex(1);
        }

        // Figest SHA256
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((payload + cd.getPreviousHash()).getBytes(StandardCharsets.UTF_8));
            String hashString = Base64.getEncoder().encodeToString(hash);
            cd.setHash(hashString);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("Error creating hash", noSuchAlgorithmException);
        }

        return cd;
    }

    /**
     * Get last blockchain block.
     *
     * @return
     */
    public Chaindata getLastBlock() {
        Chaindata ret = null;
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
    public ArrayList<Chaindata> getChain() {
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

                ArrayList<Chaindata> peerBlockchain = new ArrayList<>();
                for (int n = 0; n < ja.length(); n++) {
                    JSONObject jo = (JSONObject) ja.get(n);
                    Chaindata node = new Chaindata();
                    node.setHash((String) jo.get("hash"));
                    node.setIndex((Integer) jo.get("index"));
                    node.setPayload((String) jo.get("payload"));
                    node.setPreviousHash((String) jo.get("previousHash"));
                    node.setTimestamp((Long) jo.get("timestamp"));
                    peerBlockchain.add(node);
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
