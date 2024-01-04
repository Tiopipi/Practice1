package org.ulpgc.dacd.control;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String rootDirectory = args[0];
        JMSEventSupplier jmsEventSupplier = new JMSEventSupplier("tcp://localhost:61616");
        List<String> topics = List.of("prediction.Weather", "data.Hotel");
        String baseSubscriptionName = "datalake_builder_";
        jmsEventSupplier.read(topics, baseSubscriptionName, rootDirectory);

    }
}