package fr.pantheonsorbonne.cri.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Node {
	@Override
	public String toString() {
		return "Node [type=" + type + ", url=" + url + ", id=" + id + "]";
	}

	public enum NodeType {
		GATEWAY, SOURCE, SINK, TASK

	}

	public enum GatewayType {
		PARALLEL_DIVERGING, PARALLEL_CONVERGING, EXCLUSIVE_DIVERGING, EXCLUSIVE_CONVERGING
	}

	private String type;
	private String gatewayDirection;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGatewayDirection() {
		return gatewayDirection;
	}

	public void setGatewayDirection(String gatewayDirection) {
		this.gatewayDirection = gatewayDirection;
	}

	private String id;
	private Payload payload;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public boolean isGateway() {
		return this.getType().toLowerCase().contains("gateway");
	}

	private boolean isDivergingGateway() {
		return this.isGateway() && this.getGatewayDirection().equals("Diverging");
	}

	private boolean isConvergingGateway() {
		return this.isGateway() && !this.isDivergingGateway();
	}

	private boolean isSink() {
		return this.getType().equals("endEvent");
	}

	private boolean isSource() {
		return this.getType().equals("starEvent");
	}

	public NodeType getNodeType() {
		if (this.isSink()) {
			return NodeType.SINK;
		} else if (this.isGateway()) {
			return NodeType.GATEWAY;
		} else if (this.isSource()) {
			return NodeType.SOURCE;
		} else {
			return NodeType.TASK;
		}
	}

	public GatewayType getGatewayNature() {

		if (!this.isGateway()) {
			throw new RuntimeException("Not a gateway");
		}
		if (this.getType().equals("exclusiveGateway")) {
			if (!this.isDivergingGateway()) {
				return GatewayType.EXCLUSIVE_CONVERGING;
			} else {
				return GatewayType.EXCLUSIVE_DIVERGING;
			}
		}

		else {
			if (!this.isDivergingGateway()) {
				return GatewayType.PARALLEL_CONVERGING;
			} else {
				return GatewayType.PARALLEL_DIVERGING;
			}
		}

	}
}
