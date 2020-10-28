/*
 * Copyright (c) 2018 Matteo Baccan
 * Distributed under the MIT software license, see the accompanying
 * file COPYING or http://www.opensource.org/licenses/mit-license.php.
 */
package it.baccan.blockchain.service;

import it.baccan.blockchain.chain.pojo.Chaindata;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Matteo Baccan
 */
@Slf4j
public class Block {

    @Getter
    private Chaindata chainData;

    /**
     * Block constructor.
     *
     * @param chainData
     */
    public Block(Chaindata chainData) {
        this.chainData = chainData;

        calculateHash();
    }

    private void calculateHash() {
        // Figest SHA256
        try {
            StringBuilder string2Hash = new StringBuilder();
            string2Hash.append(chainData.getPreviousHash());
            string2Hash.append(Long.toString(chainData.getTimestamp()));
            string2Hash.append(chainData.getPayload());
            string2Hash.append(Long.toString(chainData.getNonce()));

            // Digest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Hash
            byte[] hash = digest.digest(string2Hash.toString().getBytes());

            // HEX
            String HEXES = "0123456789ABCDEF";
            StringBuilder hashString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                hashString.append(HEXES.charAt((b & 0xF0) >> 4))
                        .append(HEXES.charAt((b & 0x0F)));
            }

            // Setter
            chainData.setHash(hashString.toString());
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("Error creating hash", noSuchAlgorithmException);
        }
    }

    /**
     *
     * @return
     */
    public String hash() {
        return chainData.getHash();
    }

    /**
     * Idea taken from :
     * https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
     *
     * @param difficulty
     */
    public void mineBlock(int difficulty) {
        long nIni = System.nanoTime();
        log.info("Ask for mining {}", difficulty);
        chainData.setNonce(0);
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!chainData.getHash().substring(0, difficulty).equals(target)) {
            chainData.setNonce(chainData.getNonce() + 1);
            calculateHash();
        }
        long nEnd = System.nanoTime();
        log.info("Block Mined : {} : in {} ms", chainData.getHash(), (nEnd - nIni) / 1000000);
    }

    /**
     *
     * @param difficulty
     * @return
     */
    public boolean isMined(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        return chainData.getHash().substring(0, difficulty).equals(target);
    }

}
