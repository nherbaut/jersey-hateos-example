package fr.pantheonsorbonne.cri.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.ContextAutomaton;
import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class ParallelStubMessageHandler extends StubMessageHandlerImpl {

	public ParallelStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ParallelStubMessageHandler.class);

	StubMessageHandler nestedHandler;

	@Override
	public void handleStubMessage() {

		Collection<Node> nextNodes = this.getMessage().next(this.getMyNode());
		ContextAutomaton randomContext = ContextAutomaton.getRandom(nextNodes.size(), this.getMessage().getContext());
		this.getMessage().setContext(randomContext);
		ParallelStubMessageHolder.submitMessageOnHold(this.getMessage());
		nextNodes.parallelStream()//
				// .peek(n -> LOGGER.info("{} -> {}", myIdentity, n.getId()))//
				.map(n -> new RestClientStubMessageHandler(this.getMessage(), n.getId()))//
				.forEach(RestClientStubMessageHandler::handleStubMessage);

	}

}
