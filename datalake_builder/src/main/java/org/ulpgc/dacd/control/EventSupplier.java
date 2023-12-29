package org.ulpgc.dacd.control;


import java.util.List;

public interface EventSupplier {
    void read(List<String> topics, String baseSubscriptionName, String rootDirectory);
}
