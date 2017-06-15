package com.myretail.product.client;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;

/**
 * Created by daichi on 15/6/17.
 */
public class Test {

    public static void main(String[] args) {

        AerospikeClient asCli = new AerospikeClient("localhost", 3000);
        Key asKey = new Key("pricetag", "price", "13860428");
        Bin currBin = new Bin("curr", "USD");
        Bin valBin = new Bin("val", 25.0);
        asCli.put(null, asKey, currBin, valBin);

        asKey = new Key("pricetag", "price", "16696652");
        currBin = new Bin("curr", "INR");
        valBin = new Bin("val", 15000.00);
        asCli.put(null, asKey, currBin, valBin);

    }

}
