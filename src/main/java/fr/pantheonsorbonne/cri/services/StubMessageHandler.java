package fr.pantheonsorbonne.cri.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import fr.pantheonsorbonne.cri.model.Node;
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

	private Collection<Node> getOutgoingNodes(StubMessage message) {
		return this.getOutgoingNodesSkippingGateways(message, this.nodeIdentifier);

	}

	protected Collection<Node> getOutgoingNodes(StubMessage message, String nodeIdentifier) {
		return Arrays.stream(message.getLinks())//
				.filter(l -> l.getSource().equals(nodeIdentifier))//
				.map(l -> l.getTarget()).map(s -> Arrays.stream(message.getNodes())//
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

	public void handleStubMessage(StubMessage message) {
		this.handleStubMessage(message, this.nodeIdentifier);
	}

	public abstract void handleStubMessage(StubMessage message, String myIdentity);

}
