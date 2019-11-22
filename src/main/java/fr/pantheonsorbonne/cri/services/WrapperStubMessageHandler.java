package fr.pantheonsorbonne.cri.services;

import java.time.Instant;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class WrapperStubMessageHandler implements StubMessageHandler {

	private StubMessageHandler delegate;
	private String nodeIdentified;

	public WrapperStubMessageHandler(StubMessageHandler delegate, String nodeIdentified) {
		this.delegate = delegate;
		this.nodeIdentified = nodeIdentified;
	}

	@Override
	public ExecutionTrace handleStubMessage() {
		ExecutionTrace et = delegate.handleStubMessage();
		et.getNodes().add(nodeIdentified+" "+Instant.now());
		return et;
	}

}
