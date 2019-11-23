package fr.pantheonsorbonne.cri.services;

import fr.pantheonsorbonne.cri.model.StubMessage;

public class JMSClientStubMessageHandler extends StubMessageHandlerImpl {

	public JMSClientStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	@Override
	public synchronized void handleStubMessage() {

		String nextNodeId = this.message.firstNext(this.message.getNodeFromId(this.nodeIdentifier)).getId();
		JMSUtils.sendTextMessage(nextNodeId, JMSUtils.marshallMessage(this.message));

	}

}
