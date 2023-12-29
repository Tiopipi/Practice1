package org.ulpgc.dacd.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class JMSEventSupplier implements EventSupplier {
    private final String url;

    public JMSEventSupplier(String url) {
        this.url = url;
    }

    public void read(List<String> topics, String baseSubscriptionName, String rootDirectory) {
        FileEventStore fileEventStore = new FileEventStore();
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("dataLake_builder");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            for (int i = 0; i < topics.size(); i++){
                String topicName = topics.get(i);
                Topic topic = session.createTopic(topicName);
                MessageConsumer subscriber = session.createDurableSubscriber(topic, baseSubscriptionName + topicName);
                subscriber.setMessageListener(m-> {
                    try {
                        fileEventStore.write(((TextMessage) m).getText(), "datalake/" + topicName + "/", rootDirectory);
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
