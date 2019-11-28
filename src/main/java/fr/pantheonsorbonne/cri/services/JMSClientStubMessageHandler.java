package fr.pantheonsorbonne.cri.services;

import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class JMSClientStubMessageHandler extends StubMessageHandlerImpl {

	public JMSClientStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	@Override
	public synchronized void handleStubMessage() {

		Node n = this.getMessage().firstNext(this.getMessage().getNodeFromId(this.nodeIdentifier));
		String nextNodeId = n.getHost();
		String nextNodeIdentifier = n.getId();
		JMSUtils.sendTextMessage(nextNodeId, nextNodeIdentifier, JMSUtils.marshallMessage(this.getMessage()));

	}

}
