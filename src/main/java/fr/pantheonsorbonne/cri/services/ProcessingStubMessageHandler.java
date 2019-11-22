package fr.pantheonsorbonne.cri.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class ProcessingStubMessageHandler extends StubMessageHandlerImpl {

	public ProcessingStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingStubMessageHandler.class);

	@Override
	public ExecutionTrace handleStubMessage() {
		
		
		ExecutionTrace et = new ExecutionTrace();
		et.setPayload(this.getMyNode().getPayload());
		return et;

	}

}
