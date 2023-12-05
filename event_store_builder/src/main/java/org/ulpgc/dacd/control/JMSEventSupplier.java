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

    public List<String> read(String topic, String subscriptionName) {
        FileEventStore fileEventStore = new FileEventStore();
        List<String> eventList = new ArrayList<>();
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("event_store_buider");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic destination = session.createTopic(topic);
            MessageConsumer subscriber = session.createDurableSubscriber(destination, subscriptionName);
            subscriber.setMessageListener(m-> {
                try {
                    fileEventStore.write(((TextMessage) m).getText(), "eventstore/prediction.Weather/");
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        return eventList;
    }
}
