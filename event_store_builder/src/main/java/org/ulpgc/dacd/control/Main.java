package org.ulpgc.dacd.control;

public class Main {

    public static void main(String[] args) {
        JMSEventSupplier jmsEventSupplier = new JMSEventSupplier("tcp://localhost:61616");
        jmsEventSupplier.read("prediction.Weather", "event_store_buider_prediction.Weather");
    }
}