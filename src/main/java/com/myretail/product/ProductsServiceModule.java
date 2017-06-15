package com.myretail.product;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;
import com.google.common.net.HostAndPort;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Guice module for injection.
 */
public class ProductsServiceModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(ProductsServiceApplication.class).asEagerSingleton();
    }

    @Provides
    public AerospikeClient getAerospikeClient(final ProductsServiceConfiguration configuration) {

        String aerospikeConnectString = configuration.getAerospikeConnectionString();
        String [] aerospikeHostPorts = aerospikeConnectString.split(",");
        Host[] hosts = new Host[aerospikeHostPorts.length];
        int i = 0;
        for (String hpString : aerospikeHostPorts) {
            HostAndPort hp = HostAndPort.fromString(hpString);
            hosts[i] = new Host(hp.getHostText(), hp.getPortOrDefault(3000));
        }
        return new AerospikeClient(new ClientPolicy(), hosts);
    }
}
