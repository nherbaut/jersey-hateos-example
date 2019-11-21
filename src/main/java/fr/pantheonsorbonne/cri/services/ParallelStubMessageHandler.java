package fr.pantheonsorbonne.cri.services;

import java.util.function.BinaryOperator;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
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
	public ExecutionTrace handleStubMessage(StubMessage message, String myIdentity) {

		
		return this.getOutgoingNodesSkippingGateways(message, myIdentity).parallelStream()//
				//.peek(n -> LOGGER.info("{} -> {}", myIdentity, n.getId()))//
				.map(n -> this.nestedHandler.handleStubMessage(message, n.getId()))//
				.reduce((t, u) -> {

					System.out.println("combining " + t + " and " + u);
					ExecutionTrace et = new ExecutionTrace();
					
					et.getNodes().add(myIdentity);
					et.getNodes().addAll(t.getNodes());
					et.getNodes().addAll(u.getNodes());
					et.setPayload(t.getPayload().add(u.getPayload()));

					return et;
				}).orElse(new ExecutionTrace());

	}

}
