package fr.pantheonsorbonne.cri.services;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class JMSClientStubMessageHandler extends StubMessageHandlerImpl {

	public JMSClientStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	@Override
	public ExecutionTrace handleStubMessage() {

		try {
			String nextNodeId = this.message.firstNext(this.message.getNodeFromId(this.nodeIdentifier)).getId();
			TextMessage message = JMSUtils.getSession().createTextMessage(JMSUtils.marshallMessage(this.message));
			message.setStringProperty("identifier", nextNodeId);
			JMSUtils.getProducerMap().get(nextNodeId).send(message);

			return new ExecutionTrace();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}
