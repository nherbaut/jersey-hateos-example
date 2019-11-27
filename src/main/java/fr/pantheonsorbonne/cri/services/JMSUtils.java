package fr.pantheonsorbonne.cri.services;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;

import fr.pantheonsorbonne.cri.endpoints.JMSEndoint;
import fr.pantheonsorbonne.cri.model.ClusterConfig;
import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class JMSUtils {// fake JNDI context to create object

	private static Map<String, ActiveMQConnectionFactory> connectionFactoryMap = new HashMap<String, ActiveMQConnectionFactory>();
	private static JAXBContext jaxbContext;

	public static JAXBContext getJaxbContext() {
		return jaxbContext;
	}

	static {
		try {
			jaxbContext = JAXBContext.newInstance(StubMessage.class);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public static String marshallMessage(StubMessage message) {
		StringWriter writer = new StringWriter();
		try {
			jaxbContext.createMarshaller().marshal(message, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

	}

	public static synchronized void sendTextMessage(final String nodeId, final String message) {
		try (Connection connection = connectionFactoryMap.get(nodeId).createConnection()) {
			try (Session session = connection.createSession()) {
				try (MessageProducer producer = session.createProducer(new ActiveMQQueue(nodeId))) {
					Message textMessage = session.createTextMessage(message);
					textMessage.setStringProperty("identifier", nodeId);
					producer.send(textMessage);
				}

			}

		} catch (JMSException e) {
			throw new RuntimeException("Failed to create Text Message", e);
		}
	}

	public static void initQueue(ClusterConfig config) {

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(config.getBrokerURL());
		connectionFactoryMap.put(config.getHost(), connectionFactory);

		Queue queue = new ActiveMQQueue(config.getHost());

		new Thread(new Runnable() {

			@Override
			public void run() {
				try (Connection con = connectionFactory.createConnection("nicolas", "nicolas")) {
					try (Session session = con.createSession()) {

						try (MessageConsumer consumer = session.createConsumer(queue)) {
							while (true) {
								Message message = consumer.receive();
								JMSEndoint.onMsalt/ethereum/ip_list.jsonessageReceived(message);
							}
						}

					}
				} catch (JMSException e) {
					throw new RuntimeException(e);
				}

			}
		}, "JMSListener." + config.getHost()).start();

	}
}
