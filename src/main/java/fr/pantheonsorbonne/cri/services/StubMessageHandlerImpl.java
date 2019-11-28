package fr.pantheonsorbonne.cri.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Link;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.Payload;
import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class StubMessageHandlerImpl implements StubMessageHandler {

	private StubMessage message;

	protected StubMessage getMessage() {
		return message;
	}

	protected String nodeIdentifier;

	public StubMessageHandlerImpl(StubMessage message, String nodeIdentifier) {
		this.message = message;
		this.nodeIdentifier = nodeIdentifier;
	}

	protected Collection<Node> getOutgoingNodes(StubMessage message, String nodeIdentifier) {
		return message.getLinks().stream()//
				.filter(l -> l.getSource().equals(nodeIdentifier))//
				.map(Link::getTarget)//
				.map(s -> message.getNodes().stream().filter(n -> n.getId().equals(s)).findFirst().orElseThrow())
				.collect(Collectors.toSet());

	}

	protected Node getMyNode() {
		return message.getNodes().stream().filter(m -> m.getId().equals(this.nodeIdentifier)).findFirst().orElseThrow();

	}

}
