package org.ulpgc.dacd.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class JMSEventSupplier implements EventSupplier{
    private final String url;
    public JMSEventSupplier(String url) {
        this.url = url;
    }

    public void read(List<String> topics, String baseSubscriptionName){
        SQLiteEventStore sqLiteEventStore = new SQLiteEventStore();
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("hotel_recommendation");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            for (String topicName : topics) {
                Topic topic = session.createTopic(topicName);
                MessageConsumer subscriber = session.createDurableSubscriber(topic, baseSubscriptionName + topicName);
                subscriber.setMessageListener(m -> {
                    try {
                        sqLiteEventStore.write(((TextMessage) m).getText(), topicName);
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
