package org.ulpgc.dacd.control;

import org.ulpgc.dacd.view.SwingHotelRecommendationView;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        JMSEventSupplier jmsEventSupplier = new JMSEventSupplier("tcp://localhost:61616");
        SwingHotelRecommendationView swingHotelRecommendationView = new SwingHotelRecommendationView();
        List<String> topics = List.of("prediction.Weather", "data.Hotel");
        String baseSubscriptionName = "hotel_recommender_";
        jmsEventSupplier.read(topics, baseSubscriptionName);
        swingHotelRecommendationView.execute();
    }
}