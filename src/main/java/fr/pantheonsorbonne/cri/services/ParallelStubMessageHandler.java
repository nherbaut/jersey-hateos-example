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
	public ExecutionTrace handleStubMessage() {

		Collection<Node> nextNodes = this.message.next(this.getMyNode());
		ContextAutomaton randomContext = ContextAutomaton.getRandom(nextNodes.size(), this.message.getContext());
		this.message.setContext(randomContext);
		ParallelStubMessageHolder.submitMessageOnHold(message);
		return nextNodes.parallelStream()//
				// .peek(n -> LOGGER.info("{} -> {}", myIdentity, n.getId()))//
				.map(n -> new RestClientStubMessageHandler(this.message, n.getId()))//
				.map(RestClientStubMessageHandler::handleStubMessage)//
				.reduce((t, u) -> {

					t.add(u);

					return t;
				}).orElse(new ExecutionTrace());

	}

}
