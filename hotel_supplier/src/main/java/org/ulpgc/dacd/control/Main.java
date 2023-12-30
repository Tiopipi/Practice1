package org.ulpgc.dacd.control;


import java.util.*;

public class Main {
    public static void main(String[] args) {
        HotelControl hotelControl = new HotelControl();
        List<List<String>> hotels = loadHotelsData();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                hotelControl.execute(hotels);
            }
        }, 0, 6 * 60 * 60 * 1000);
    }

    private static List<List<String>> loadHotelsData(){
        return List.of(
                List.of("Danubius Hotel Regents Park", "London", "g186338-d193129"),
                List.of("Hotel Riu Plaza New York Times Square", "NewYork", "g60763-d8515751"),
                List.of("Barceló Sevilla Renacimiento", "Seville", "g187443-d228627"),
                List.of("Krystal Puerto Vallarta Hotel", "VallartaPort", "g150793-d155676"),
                List.of("Moov Hotel Porto Centro", "Porto", "g189180-d2522678"),
                List.of("Paris Las Vegas", "Nevada", "g26308655-d143336"),
                List.of("Explorers Hotel Disneyland Paris", "Paris", "g664935-d1221615"),
                List.of("Bedford Hotel & Congress Centre", "Brussels", "g188644-d206696"),
                List.of("Hotel Meliá Valencia", "Valencia", "g187529-d590224"),
                List.of("Hotel Sunroute Plaza Shinjuku", "Tokio", "g14133713-d320581")
        );
    }
}
