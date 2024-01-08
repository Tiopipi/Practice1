package org.ulpgc.dacd;


import org.ulpgc.dacd.control.JMSEventSupplier;
import org.ulpgc.dacd.control.SQLiteDataProvider;
import org.ulpgc.dacd.view.SwingHotelRecommendationView;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        JMSEventSupplier jmsEventSupplier = new JMSEventSupplier("tcp://localhost:61616");
        SQLiteDataProvider dataProvider = new SQLiteDataProvider();
        SwingHotelRecommendationView swingHotelRecommendationView = new SwingHotelRecommendationView(dataProvider);
        List<String> topics = List.of("prediction.Weather", "data.Hotel");
        String baseSubscriptionName = "hotel_recommender_";
        jmsEventSupplier.read(topics, baseSubscriptionName);
        swingHotelRecommendationView.execute();
    }
}