package org.ulpgc.dacd.control;

import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce the check in with the format: YYYY-MM-DD");
        String checkIn = scanner.nextLine();
        System.out.println("Introduce the check out with the format: YYYY-MM-DD");
        String checkOut = scanner.nextLine();
        scanner.close();
        HotelControl hotelControl = new HotelControl();
        List<List<String>> hotels = loadHotelsData();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                hotelControl.execute(hotels, checkIn, checkOut);
            }
        }, 0, 6 * 60 * 60 * 1000);
    }

    private static List<List<String>> loadHotelsData(){
        return List.of(
                List.of("Hotel Riu Gran Canaria", "GranCanaria", "g230095-d530762"),
                List.of("Bull Reina Isabel & Spa", "GranCanaria", "g187472-d231610"),
                List.of("Lopesan Villa del Conde Resort & Thalasso", "GranCanaria", "g2089121-d1218129"),
                List.of("Marina Suites Hotel", "GranCanaria", "g635887-d530806"),
                List.of("Hard Rock Hotel Tenerife", "Tenerife", "g315919-d4341700"),
                List.of("Barceló Tenerife", "Tenerife", "g672806-d1234433"),
                List.of("Sol Arona Tenerife", "Tenerife", "g659661-d248469"),
                List.of("Hotel Best Tenerife", "Tenerife", "g562820-d498662"),
                List.of("Parador de El Hierro", "ElHierro", "g187474-d277394"),
                List.of("Hotel Ida Inés", "ElHierro", "g2139290-d603283"),
                List.of("Balneario Pozo de la Salud", "ElHierro", "g1189149-d1193818"),
                List.of("Apartamentos Los Verodes", "ElHierro", "g2139290-d2010097"),
                List.of("La Palma Princess", "LaPalma", "g1175543-d638034"),
                List.of("Parador de la Palma", "LaPalma", "g642213-d482745"),
                List.of("Sol La Palma Hotel by Melia", "LaPalma", "g675093-d249518"),
                List.of("Esencia de La Palma by Princess", "LaPalma", "g1175543-d21175914"),
                List.of("Parador de la Gomera", "LaGomera", "g187470-d190895"),
                List.of("Apartamentos Bellavista Gomera", "LaGomera", "g1187912-d1100330"),
                List.of("Hotel Jardín Tecina", "LaGomera", "g1187912-d324473"),
                List.of("Hotel Villa Gomera", "LaGomera", "g187470-d614341"),
                List.of("Barceló Fuerteventura Castillo", "Fuerteventura", "g658907-d237059"),
                List.of("Alua Suites Fuerteventura", "Fuerteventura", "g580322-d573425"),
                List.of("Fuerteventura Princess", "Fuerteventura", "g673234-d500267"),
                List.of("TUI MAGIC LIFE Fuerteventura", "Fuerteventura", "g562817-d1586766"),
                List.of("Barceló Lanzarote Active Resort", "Lanzarote", "g659633-d234400"),
                List.of("H10 Lanzarote Princess", "Lanzarote", "g652121-d289256"),
                List.of("Secrets Lanzarote Resort & Spa", "Lanzarote", "g580321-d282759"),
                List.of("Dreams Lanzarote Playa Dorada Resort & Spa", "Lanzarote", "g652121-d262432"),
                List.of("Evita Beach Aptos y Suites Exclusivas", "LaGraciosa", "g1190272-d2645782"),
                List.of("Apartamentos GraciosaMar", "LaGraciosa", "g1190272-d945848")
        );
    }
}
