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

public abstract class StubMessageHandler {

	@Inject
	@Named("nodeIdentifier")
	protected String nodeIdentifier;

	private Node getNodeFromId(StubMessage mess, String id) {
		return Arrays.stream(mess.getNodes()).filter(n -> n.getId().equals(id)).findAny().orElseThrow();
	}

	protected Collection<Node> getOutgoingNodesSkippingGateways(StubMessage message, String nodeIdentifier) {

		Collection<Node> res = new ArrayList<>();
		Queue<Node> tentativeNextNodes = new LinkedList<>();
		tentativeNextNodes.add(getNodeFromId(message, nodeIdentifier));
		while (!tentativeNextNodes.isEmpty()) {

			for (Node node : this.getOutgoingNodes(message, tentativeNextNodes.poll().getId())) {
				if (res.contains(node)) {
					break;
				}
				if (node.getType().contains("Gateway")) {
					tentativeNextNodes.addAll((this.getOutgoingNodes(message, node.getId())));
				} else {
					res.add(node);
				}
			}

		}

		return res;

	}

	protected Collection<Node> getOutgoingNodes(StubMessage message, String nodeIdentifier) {
		return Arrays.stream(message.getLinks())//
				.filter(l -> l.getSource().equals(nodeIdentifier))//
				.map(Link::getTarget)//
				.map(s -> Arrays.stream(message.getNodes())//
						.filter(n -> n.getId().equals(s))//
						.findFirst().orElseThrow())
				.collect(Collectors.toList());

	}

	protected Node getMyNode(StubMessage message) {
		return this.getMyNode(message, this.nodeIdentifier);

	}

	protected Node getMyNode(StubMessage message, String myIdentity) {
		return Arrays.stream(message.getNodes()).filter(m -> m.getId().equals(myIdentity)).findFirst().orElseThrow();

	}

	public ExecutionTrace handleStubMessage(StubMessage message) {
		return this.handleStubMessage(message, this.nodeIdentifier);
	}

	public abstract ExecutionTrace handleStubMessage(StubMessage message, String myIdentity);

}
