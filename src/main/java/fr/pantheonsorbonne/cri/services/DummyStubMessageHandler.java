package fr.pantheonsorbonne.cri.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class DummyStubMessageHandler extends StubMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyStubMessageHandler.class);

	@Override
	public ExecutionTrace handleStubMessage(StubMessage message, String myIdentity) {
		Node n = this.getMyNode(message);
		ExecutionTrace et = new ExecutionTrace();
		et.setPayload(n.getPayload());
		return et;

	}

}
