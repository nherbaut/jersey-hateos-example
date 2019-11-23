package fr.pantheonsorbonne.cri.services;

import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory;

import fr.pantheonsorbonne.cri.endpoints.JMSEndoint;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class JMSUtils {// fake JNDI context to create object
	private static Context JNDI_CONTEXT;

	private static ConnectionFactory connectionFactory;
	private static JAXBContext jaxbContext;
	private static boolean intialized = false;
	private static Connection connection;

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

	public static void initQueues(StubMessage message, String messageQueueURL) {
		if (!intialized) {
			Hashtable<String, String> jndiBindings = new Hashtable<>();
			jndiBindings.put(Context.INITIAL_CONTEXT_FACTORY, ActiveMQInitialContextFactory.class.getName());
			jndiBindings.put("connectionFactory.ConnectionFactory", messageQueueURL);

			message.getNodes().stream().map(Node::getId).forEach(t -> jndiBindings.put("queue." + t, t));

			try {
				JNDI_CONTEXT = new InitialContext(jndiBindings);
				connectionFactory = (ConnectionFactory) JNDI_CONTEXT.lookup("ConnectionFactory");

				connection = connectionFactory.createConnection("nicolas", "nicolas");
				message.getNodes().parallelStream().map(Node::getId).forEach(s -> new Thread(new Runnable() {

					@Override
					public void run() {
						listenTo(s);

					}

				}, "Queue-" + s + "-listener").start());

			} catch (NamingException | JMSException e) {
				throw new RuntimeException("Failed to initialize Queues ", e);
			}
			intialized = true;
		}

	}

	public static ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public static void setConnectionFactory(ConnectionFactory connectionFactory) {
		JMSUtils.connectionFactory = connectionFactory;
	}

	private static void listenTo(String queueName) {
		try {
			try (Session session = connection.createSession()) {
				Destination dest = (Queue) JNDI_CONTEXT.lookup(queueName);
				try (MessageConsumer consumer = session.createConsumer(dest)) {
					while (intialized) {
						Message message = consumer.receive();
						JMSEndoint.onMessageReceived(message);
					}
				}

			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to create Message Consummer ", e);
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
		try {
			try (Session session = connection.createSession()) {
				try (MessageProducer producer = session.createProducer((Queue) JNDI_CONTEXT.lookup(nodeId))) {
					Message textMessage = session.createTextMessage(message);
					textMessage.setStringProperty("identifier", nodeId);
					producer.send(textMessage);
				}

			}

		} catch (JMSException | NamingException e) {
			throw new RuntimeException("Failed to create Text Message", e);
		}
	}
}
