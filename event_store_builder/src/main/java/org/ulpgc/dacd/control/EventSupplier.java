package org.ulpgc.dacd.control;

import java.util.List;

public interface EventSupplier {
    List<String> read(String topic, String subscriptionName);
}
