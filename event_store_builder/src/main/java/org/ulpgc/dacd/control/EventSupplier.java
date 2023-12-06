package org.ulpgc.dacd.control;


public interface EventSupplier {
    void read(String topic, String subscriptionName);
}
