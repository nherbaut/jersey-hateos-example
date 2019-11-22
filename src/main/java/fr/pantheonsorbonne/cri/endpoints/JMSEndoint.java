package fr.pantheonsorbonne.cri.endpoints;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import fr.pantheonsorbonne.cri.model.StubMessage;
import fr.pantheonsorbonne.cri.services.JMSUtils;
import fr.pantheonsorbonne.cri.services.StubMessageHandlerBuilder;

public class JMSEndoint {

	ExecutorService executor = Executors.newWorkStealingPool(10);

	public JMSEndoint() {

		JMSUtils.getConsumerMap().values().stream().map(c -> JMSEndoint.createComsumerThread(c, executor))
				.forEach(Thread::start);
	}

	private static Thread createComsumerThread(MessageConsumer c, ExecutorService executor) {
		return new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					TextMessage txtMessage = (TextMessage) c.receive();
					StringReader reader = new StringReader(txtMessage.getText());
					StubMessage message = (StubMessage) JMSUtils.getJaxbContext().createUnmarshaller()
							.unmarshal(reader);
					String id = txtMessage.getStringProperty("identifier");
					executor.execute(new Runnable() {

						@Override
						public void run() {
							StubMessageHandlerBuilder.of(message, message.getNodeFromId(id)).handleStubMessage();

						}
					});

				} catch (JMSException | JAXBException e) {
					throw new RuntimeException(e);
				}

			}
		});
	}

}
