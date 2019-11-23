package fr.pantheonsorbonne.cri.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@JsonIgnoreProperties
@XmlSeeAlso({LinkedHashMap.class})
public class StubMessage {

	public boolean isDirected() {
		return directed;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public boolean isMultigraph() {
		return multigraph;
	}

	public void setMultigraph(boolean multigraph) {
		this.multigraph = multigraph;
	}

	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	private boolean directed;
	private boolean multigraph;
	private Set<Link> links = new HashSet<>();
	private Set<Node> nodes = new HashSet<>();
	@XmlElement(required = false)
	private Object graph;
	@XmlElement(required = false)
	private ContextAutomaton context;

	@XmlElement(required = false)
	List<MonitoringInfo> monitoringInfo = new ArrayList<>();

	public void pushMonitoring(String nodeId) {
		MonitoringInfo info = new MonitoringInfo();
		info.setEpoch(Instant.now().toEpochMilli());
		info.setNodeName(nodeId);
		monitoringInfo.add(info);
	}

	public List<MonitoringInfo> getMonitoringInfo() {
		return monitoringInfo;
	}

	public void setMonitoringInfo(List<MonitoringInfo> monitoringInfo) {
		this.monitoringInfo = monitoringInfo;
	}

	public ContextAutomaton getContext() {
		return context;
	}

	public void setContext(ContextAutomaton context) {
		this.context = context;
	}

	public Object getGraph() {
		return graph;
	}

	public void setGraph(Object graph) {
		this.graph = graph;
	}

	public Node firstNext(Node node) {
		return this.next(node).iterator().next();
	}

	public Collection<Node> next(Node node) {

		return links.stream().filter(l -> l.getSource().equals(node.getId()))
				.map(l -> nodes.stream().filter(n -> n.getId().equals(l.getTarget())).findAny().orElseThrow())
				.collect(Collectors.toSet());

	}

	public Node randNext(Node node) {

		Collection<Node> nexts = this.next(node);
		return nexts.stream().skip((int) (nexts.size() * Math.random())).findFirst().orElseThrow();

	}

	public Collection<Node> prev(Node node) {

		return links.stream().filter(l -> l.getTarget().equals(node.getId()))
				.map(l -> nodes.stream().filter(n -> n.getId().equals(l.getSource())).findAny().orElseThrow())
				.collect(Collectors.toSet());

	}

	public Node prevGateway(final Node node) {

		Node tentativeNode = node;
		Collection<Node> previousNodes;
		while (!(previousNodes = this.prev(tentativeNode)).isEmpty()) {

			Optional<Node> gateway = nodes.stream().filter(Node::isGateway).findAny();
			if (gateway.isPresent()) {
				return gateway.get();
			}

			tentativeNode = previousNodes.iterator().next();
		}

		throw new RuntimeException("No previous Gateway");

	}

	public Node getNodeFromId(String id) {
		return this.getNodes().stream().filter(n -> n.getId().equals(id)).findAny().orElseThrow();
	}

}
