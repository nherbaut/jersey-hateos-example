package fr.pantheonsorbonne.cri.services;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.StubMessage;

public class ParallelStubMessageHandler extends StubMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParallelStubMessageHandler.class);

	@Inject
	@Named("nextHop")
	StubMessageHandler nestedHandler;

	@Inject
	@Named("nodeIdentifier")
	String nodeIdentifier;

	@Override
	public void handleStubMessage(StubMessage message, String myIdentity) {

		this.getOutgoingNodesSkippingGateways(message, myIdentity).stream()//
				.peek(n -> LOGGER.info("{} -> {}", myIdentity, n.getId()))//
				.forEach(n -> this.nestedHandler.handleStubMessage(message, n.getId()));

	}

}
