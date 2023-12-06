package org.ulpgc.dacd.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSEventSupplier implements EventSupplier {
    private final String url;

    public JMSEventSupplier(String url) {
        this.url = url;
    }

    public void read(String topicName, String subscriptionName) {
        FileEventStore fileEventStore = new FileEventStore();
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("event_store_builder");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageConsumer subscriber = session.createDurableSubscriber(topic, subscriptionName);
            subscriber.setMessageListener(m-> {
                try {
                    fileEventStore.write(((TextMessage) m).getText(), "eventstore/" + topicName + "/");
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
