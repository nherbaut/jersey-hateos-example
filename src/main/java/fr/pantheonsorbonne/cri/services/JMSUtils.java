package fr.pantheonsorbonne.cri.services;

import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory;

import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class JMSUtils {// fake JNDI context to create object
	private static Context JNDI_CONTEXT;

	private static Map<String, MessageProducer> producerMap;
	private static Map<String, MessageConsumer> consumerMap;
	private static ConnectionFactory connectionFactory;
	private static Session session;
	private static JAXBContext jaxbContext;
	private static boolean intialized = false;

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

	public static void initQueues(StubMessage message) {
		if (!intialized) {
			Hashtable<String, String> jndiBindings = new Hashtable<>();
			jndiBindings.put(Context.INITIAL_CONTEXT_FACTORY, ActiveMQInitialContextFactory.class.getName());
			jndiBindings.put("connectionFactory.ConnectionFactory", "tcp://localhost:61616");

			message.getNodes().stream().map(n -> n.getId()).forEach(new Consumer<String>() {

				@Override
				public void accept(String t) {
					jndiBindings.put("queue." + t, t);

				}
			});

			try {
				JNDI_CONTEXT = new InitialContext(jndiBindings);
				connectionFactory = (ConnectionFactory) JNDI_CONTEXT.lookup("ConnectionFactory");

				Connection connection = connectionFactory.createConnection("nicolas", "nicolas");
				connection.start();
				session = connection.createSession();

				JMSUtils.producerMap = message.getNodes().stream().map(Node::getId)//
						.collect(Collectors.toMap(Function.identity(), JMSUtils::getMPSafe));

				JMSUtils.setConsumerMap(message.getNodes().stream().map(Node::getId)//
						.collect(Collectors.toMap(Function.identity(), JMSUtils::getMCSafe)));

			} catch (NamingException | JMSException e) {
				throw new RuntimeException(e);
			}
		}
		intialized = true;

	}

	public static ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public static void setConnectionFactory(ConnectionFactory connectionFactory) {
		JMSUtils.connectionFactory = connectionFactory;
	}

	public static Session getSession() {
		return session;
	}

	public static void setSession(Session session) {
		JMSUtils.session = session;
	}

	private static MessageProducer getMPSafe(String queueName) {
		try {
			return session.createProducer((Queue) JNDI_CONTEXT.lookup(queueName));
		} catch (NamingException | JMSException e) {
			throw new RuntimeException(e);
		}
	}

	private static MessageConsumer getMCSafe(String queueName) {
		try {
			return session.createConsumer((Queue) JNDI_CONTEXT.lookup(queueName));
		} catch (NamingException | JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String, MessageProducer> getProducerMap() {
		return producerMap;
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

	public static Map<String, MessageConsumer> getConsumerMap() {
		return consumerMap;
	}

	public static void setConsumerMap(Map<String, MessageConsumer> consumerMap) {
		JMSUtils.consumerMap = consumerMap;
	}
}
