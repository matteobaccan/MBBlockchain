/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.baccan.blockchain.chain.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Matteo
 */
@Setter @Getter
public class Peerdata {

    private String ip;
    private String port;
    
}
