/*
 * Copyright (c) 2018 Matteo Baccan
 * Distributed under the MIT software license, see the accompanying
 * file COPYING or http://www.opensource.org/licenses/mit-license.php.
 */
package it.baccan.blockchain.chain.pojo;

import lombok.Data;

/**
 *
 * @author Matteo Baccan
 */
@Data
public class Chaindata {

    private String previousHash;
    private long timestamp;
    private String payload;
    private long nonce;
    private String hash;

}
