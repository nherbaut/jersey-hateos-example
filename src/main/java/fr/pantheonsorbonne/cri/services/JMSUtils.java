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

import com.google.common.base.Strings;

import fr.pantheonsorbonne.cri.endpoints.JMSEndoint;
import fr.pantheonsorbonne.cri.model.ClusterConfig;
import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class JMSUtils {// fake JNDI context to create object

	private static Map<String, ActiveMQConnectionFactory> connectionFactoryMap = new HashMap<String, ActiveMQConnectionFactory>();
	private static JAXBContext jaxbContext;
	private static String brokerUser;
	private static String brokerPWD;
	private static final String DEFAULT_BROKER_CREDENTIAL = "nicolas";

	public static JAXBContext getJaxbContext() {
		return jaxbContext;
	}

	static {
		try {
			jaxbContext = JAXBContext.newInstance(StubMessage.class);
			brokerUser = System.getenv("ARTEMIS_USERNAME");
			if (Strings.isNullOrEmpty(brokerUser)) {
				brokerUser = DEFAULT_BROKER_CREDENTIAL;
			}
			brokerPWD = System.getenv("ARTEMIS_PASSWORD");
			if (Strings.isNullOrEmpty(brokerPWD)) {
				brokerPWD = DEFAULT_BROKER_CREDENTIAL;
			}

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

	public static synchronized void sendTextMessage(final String queueId, final String nodeId, final String message) {
		try (Connection connection = connectionFactoryMap.get(queueId).createConnection(brokerUser, brokerPWD)) {
			try (Session session = connection.createSession()) {
				try (MessageProducer producer = session.createProducer(new ActiveMQQueue(queueId))) {
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
				try (Connection con = connectionFactory.createConnection(brokerUser, brokerPWD)) {
					con.start();
					try (Session session = con.createSession()) {
						try (MessageConsumer consumer = session.createConsumer(queue)) {
							while (true) {
								Message message = consumer.receive();
								JMSEndoint.onMessageReceived(message);
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
