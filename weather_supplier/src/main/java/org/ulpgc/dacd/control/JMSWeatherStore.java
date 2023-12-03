package org.ulpgc.dacd.control;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class JMSWeatherStore implements WeatherStore{

    private final String url;

    public JMSWeatherStore(String url) {
        this.url = url;
    }


    public void save(String weatherSerialized) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("prediction.Weather");
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage(weatherSerialized);
            producer.send(message);
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
