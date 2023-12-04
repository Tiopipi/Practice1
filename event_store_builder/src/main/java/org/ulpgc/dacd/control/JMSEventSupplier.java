package org.ulpgc.dacd.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class JMSEventSupplier implements EventSupplier {


    private final String url;

    public JMSEventSupplier(String url) {
        this.url = url;
    }

    public List<String> read() {
        List<String> eventList = new ArrayList<>();
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic("prediction.Weather");
            MessageConsumer subscriber = session.createConsumer(destination);
            Message message;
            while ((message = subscriber.receive(10000)) != null) {
                if (message instanceof TextMessage) {
                    String text = ((TextMessage) message).getText();
                    eventList.add(text);
                }
            }
            subscriber.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        return eventList;
    }
}
