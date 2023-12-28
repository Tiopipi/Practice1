package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Hotel;

import javax.jms.*;
import java.io.IOException;
import java.time.Instant;

public class JMSHotelStore implements HotelStore {

    private final String url;

    public JMSHotelStore(String url) {
        this.url = url;
    }

    public void save(Hotel hotel) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("rate.Hotel");
            MessageProducer producer = session.createProducer(topic);
            Gson gson = prepareGson();
            String weatherSerialized = gson.toJson(hotel);
            TextMessage message = session.createTextMessage(weatherSerialized);
            producer.send(message);
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static Gson prepareGson(){
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
                    @Override
                    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
                        jsonWriter.value(instant.toString());
                    }

                    @Override
                    public Instant read(JsonReader jsonReader) throws IOException {
                        return Instant.parse(jsonReader.nextString());
                    }
                }).create();
    }
}
