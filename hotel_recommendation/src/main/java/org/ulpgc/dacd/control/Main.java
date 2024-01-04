package org.ulpgc.dacd.control;

import org.ulpgc.dacd.view.HotelRecommendationView;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        JMSEventSupplier jmsEventSupplier = new JMSEventSupplier("tcp://localhost:61616");
        HotelRecommendationView hotelRecommendationView = new HotelRecommendationView();
        List<String> topics = List.of("prediction.Weather", "data.Hotel");
        String baseSubscriptionName = "hotel_recommendation_";
        jmsEventSupplier.read(topics, baseSubscriptionName);
        hotelRecommendationView.execute();
    }
}