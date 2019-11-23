package fr.pantheonsorbonne.cri.endpoints;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import fr.pantheonsorbonne.cri.model.StubMessage;
import fr.pantheonsorbonne.cri.services.JMSUtils;
import fr.pantheonsorbonne.cri.services.StubMessageHandlerBuilder;

public class JMSEndoint {

	static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("MSProducerThread-%d").build();
	static ExecutorService executor = Executors.newFixedThreadPool(10, namedThreadFactory);

	public static void onMessageReceived(Message incomingMessage) {

		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					TextMessage txtMessage = (TextMessage) incomingMessage;
					StringReader reader = new StringReader(txtMessage.getText());
					StubMessage message = (StubMessage) JMSUtils.getJaxbContext().createUnmarshaller()
							.unmarshal(reader);
					String id = txtMessage.getStringProperty("identifier");
					StubMessageHandlerBuilder.of(message, message.getNodeFromId(id)).handleStubMessage();

				} catch (JMSException | JAXBException e) {
					throw new RuntimeException(e);
				}

			}
		});

	}

}
