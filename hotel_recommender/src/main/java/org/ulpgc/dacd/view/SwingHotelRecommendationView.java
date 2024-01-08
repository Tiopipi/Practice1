package org.ulpgc.dacd.view;


import org.ulpgc.dacd.control.DataProvider;
import org.ulpgc.dacd.model.Hotel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SwingHotelRecommendationView implements View {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private JFrame frame;
    private JComboBox<String> checkInComboBox;
    private JComboBox<String> checkOutComboBox;
    private JTextArea resultTextArea;
    private int preferredTemperature;
    private DataProvider dataProvider;
    private List<String> coldAnswers = List.of(
            " degrees, so I recommend that you bring warm clothes, and if you don't like the cold, then choose another destination.\n\n",
            " degrees. Discover the cozy atmosphere of a city with mild winters.\n\n",
            " degrees. Traveling to a cool region will allow you to enjoy the local culture and gastronomy.\n\n",
            " degrees. Discover the magic of a destination with pleasant temperatures and fresh breezes.\n\n"

    );
    private List<String> hotAnswers = List.of(
            " degrees, so I recommend that you wear summer clothes to face this weather, and if you hate the heat, then choose another destination.\n\n",
            " degrees. If you travel here, remember to drink plenty of water.\n\n",
            " degrees. Remember to use sunscreen.\n\n");
    private List<String> moderateAnswers = List.of(
            " degrees. Enjoy the architecture and culture in a city with pleasant temperatures.\n\n",
            " degrees. The weather is nice so if you feel like visiting this place, you should take the opportunity.\n\n",
            " degrees. A destination with a mild climate that offers a variety of outdoor activities.\n\n",
            " degrees. Traveling to a place with a moderate climate is perfect for exploring on foot.\n\n"
    );

    public SwingHotelRecommendationView(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void execute() {
        Locale.setDefault(Locale.ENGLISH);
        SwingUtilities.invokeLater(() -> {
            try {
                showPreferencesDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showPreferencesDialog() {

        JFrame preferencesFrame = new JFrame("Set Preferences");
        preferencesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        preferencesFrame.setSize(300, 150);
        preferencesFrame.setLocationRelativeTo(null);

        JPanel preferencesPanel = new JPanel(new GridLayout(4, 1));

        JLabel instructionLabel = new JLabel("Select your preferred temperature:");
        ButtonGroup temperatureGroup = new ButtonGroup();
        JRadioButton hotRadioButton = new JRadioButton("Hot");
        JRadioButton coldRadioButton = new JRadioButton("Cold");
        JRadioButton moderateRadioButton = new JRadioButton("Moderate");

        JButton continueButton = new JButton("Continue");

        temperatureGroup.add(hotRadioButton);
        temperatureGroup.add(coldRadioButton);
        temperatureGroup.add(moderateRadioButton);

        continueButton.addActionListener(e -> {
            if (hotRadioButton.isSelected()) {
                preferredTemperature = 21;
            } else if (coldRadioButton.isSelected()) {
                preferredTemperature = 9;
            } else if (moderateRadioButton.isSelected()) {
                preferredTemperature = 15;
            } else {
                JOptionPane.showMessageDialog(preferencesFrame, "You must select one option", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            preferencesFrame.dispose();
            showHotelRecommendation();
        });

        preferencesPanel.add(instructionLabel);
        preferencesPanel.add(hotRadioButton);
        preferencesPanel.add(coldRadioButton);
        preferencesPanel.add(moderateRadioButton);

        preferencesFrame.getContentPane().add(preferencesPanel, BorderLayout.CENTER);
        preferencesFrame.getContentPane().add(continueButton, BorderLayout.SOUTH);

        preferencesFrame.setVisible(true);
    }


    public void showHotelRecommendation() {
        frame = new JFrame("Hotel recommendation");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel checkInLabel = new JLabel("Check-In:");
        checkInLabel.setBounds(30, 30, 80, 20);
        frame.getContentPane().add(checkInLabel);

        checkInComboBox = new JComboBox<>(allPossibleCheckIn());
        checkInComboBox.setBounds(120, 30, 150, 20);
        frame.getContentPane().add(checkInComboBox);

        JLabel checkOutLabel = new JLabel("Check-Out:");
        checkOutLabel.setBounds(30, 60, 80, 20);
        frame.getContentPane().add(checkOutLabel);

        checkOutComboBox = new JComboBox<>(allPossibleCheckOut());
        checkOutComboBox.setBounds(120, 60, 150, 20);
        frame.getContentPane().add(checkOutComboBox);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(120, 90, 100, 30);
        frame.getContentPane().add(btnSearch);

        JButton btnChangePreferences = new JButton("Change Preferences");
        btnChangePreferences.setBounds(240, 90, 200, 30);
        frame.getContentPane().add(btnChangePreferences);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        Font font = new Font("Arial", Font.BOLD, 14);
        resultTextArea.setFont(font);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setBounds(30, 130, 1850, 800);
        frame.getContentPane().add(scrollPane);

        btnChangePreferences.addActionListener(e -> {
            frame.dispose();
            showPreferencesDialog();
        });
        btnSearch.addActionListener(e -> processSearch());

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }


    private void processSearch() {
        String checkIn = (String) checkInComboBox.getSelectedItem();
        String checkOut = (String) checkOutComboBox.getSelectedItem();

        try {
            List<Hotel> hotels = dataProvider.searchAvailableHotels(checkIn, checkOut);
            displaySearchResults(hotels, checkIn, checkOut);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Data is being loaded. Please wait for us to get the data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displaySearchResults(List<Hotel> hotels, String checkIn, String checkOut) {
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        if (checkIn.equals(checkOut)) {
            JOptionPane.showMessageDialog(frame, "Check-in and check-out dates cannot be the same.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }else if (checkInDate.isAfter(checkOutDate)){
            JOptionPane.showMessageDialog(frame, "Check-in date cannot be later than check-out date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Hotel> filteredHotels = dataProvider.searchRecommendedHotels(hotels, preferredTemperature);
        if (filteredHotels.isEmpty()){
            handleNoAvailableHotels();
        }else {
            showHotelInfo(filteredHotels);
        }
    }

    private void handleNoAvailableHotels() {
        int option = JOptionPane.showConfirmDialog(frame, "No hotels available with the current preference. Would you like to change the temperature preference?", "Change Preference", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            frame.dispose();
            showPreferencesDialog();
        }
    }

    private void showHotelInfo(List<Hotel> hotels) {
        StringBuilder hotelInfo = new StringBuilder();
        for (Hotel hotel : hotels) {
            hotelInfo.append(hotel.getName()).append(" for ").append(hotel.getRate()).append(" in ").append(hotel.getLocation()).append(". You can book it on ").append(hotel.getWebPage());

            if (preferredTemperature > 20) {
                appendTemperatureInfo(hotelInfo, hotel.averageTemperature(), hotAnswers);
            } else if (preferredTemperature < 10) {
                appendTemperatureInfo(hotelInfo, hotel.averageTemperature(), coldAnswers);
            } else {
                appendTemperatureInfo(hotelInfo, hotel.averageTemperature(), moderateAnswers);
            }
        }
        resultTextArea.setText("Available hotels:\n\n" + hotelInfo);
    }

    private void appendTemperatureInfo(StringBuilder hotelInfo, double averageTemperature, List<String> temperatureAnswers) {
        hotelInfo.append(". It's an average temperature of ").append(Math.round(averageTemperature))
                .append(temperatureAnswers.get(new Random().nextInt(temperatureAnswers.size())));
    }

    public String[] allPossibleCheckIn() {
        LocalDate today = LocalDate.now();
        List<String> checkInDates = new ArrayList<>();
        if (ZonedDateTime.now(ZoneOffset.UTC).toLocalTime().isAfter(LocalTime.of(12, 0))) {
            for (int i = 1; i < 5; i++) {
                checkInDates.add(today.plusDays(i).format(dateFormatter));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                checkInDates.add(today.plusDays(i).format(dateFormatter));
            }
        }
        return checkInDates.toArray(new String[0]);
    }

    public String[] allPossibleCheckOut() {
        LocalDate today = LocalDate.now();
        List<String> checkOutDates = new ArrayList<>();
        if (ZonedDateTime.now(ZoneOffset.UTC).toLocalTime().isAfter(LocalTime.of(12, 0))) {
            for (int i = 2; i < 5; i++) {
                checkOutDates.add(today.plusDays(i).format(dateFormatter));
            }
        } else {
            for (int i = 1; i < 5; i++) {
                checkOutDates.add(today.plusDays(i).format(dateFormatter));
            }
        }
        return checkOutDates.toArray(new String[0]);
    }


}