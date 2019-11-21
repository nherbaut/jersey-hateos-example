package fr.pantheonsorbonne.cri.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecutionTrace {

	@Override
	public String toString() {
		return "ExecutionTrace [nodes=" + nodes + ", payload=" + payload + "  ]";
	}

	public static final ExecutionTrace EMPTY = new ExecutionTrace();
	Payload payload = Payload.EMPTY;


	List<String> nodes = new ArrayList<>();

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

}
