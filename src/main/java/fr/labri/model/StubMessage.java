package fr.labri.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties
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

	public Link[] getLinks() {
		return links;
	}

	public void setLinks(Link[] links) {
		this.links = links;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	private boolean directed;
	private boolean multigraph;
	private Link[] links;
	private Node[] nodes;
	private Object graph;

	public Object getGraph() {
		return graph;
	}

	public void setGraph(Object graph) {
		this.graph = graph;
	}
}
