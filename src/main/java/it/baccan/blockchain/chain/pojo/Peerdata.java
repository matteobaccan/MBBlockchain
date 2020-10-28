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
public class Peerdata {

    private String ip;
    private String port;

}
