package fr.labri.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class StubMessage {

	
	private String nextHop;
	private StubMessagePayload payload;
	private List<StubMessage> nextMessages=Collections.EMPTY_LIST;

	
	@Override
	public String toString() {
		return "StubMessage [nextHop=" + nextHop + ", payload=" + payload + ", nextMessages=" + nextMessages + "]";
	}

	public String getNextHop() {
		return nextHop;
	}

	public void setNextHop(String nextHop) {
		this.nextHop = nextHop;
	}

	public StubMessagePayload getPayload() {
		return payload;
	}

	public void setPayload(StubMessagePayload payload) {
		this.payload = payload;
	}

	public List<StubMessage> getNextMessages() {
		return nextMessages;
	}

	public void setNextMessages(List<StubMessage> nextMessages) {
		this.nextMessages = nextMessages;
	}

}
