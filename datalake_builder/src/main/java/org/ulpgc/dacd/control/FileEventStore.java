package org.ulpgc.dacd.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileEventStore implements EventStore {
    public void write(String event, String basePath, String rootDirectory) {
        List<String> tsAndSsList;
        tsAndSsList = getSsAndTs(event);
        String directoryPath = rootDirectory + "/" + basePath + tsAndSsList.get(1) + "/" + tsAndSsList.get(0) + ".events";
        try {
            Path directoryPathObj = Path.of(directoryPath);
            Files.createDirectories(directoryPathObj.getParent());
            event = event + System.lineSeparator();
            Files.write(Path.of(directoryPath), event.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<String> getSsAndTs(String event) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> tsAndSsList = new ArrayList<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(event);
            String ss = jsonNode.get("ss").asText();
            String ts = jsonNode.get("ts").asText();
            String tsFormatted = getFormattedDate(ts);
            tsAndSsList.add(tsFormatted);
            tsAndSsList.add(ss);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsAndSsList;
    }

    private String getFormattedDate(String ts) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
            return outputFormat.format(inputFormat.parse(ts));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
