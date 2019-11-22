package fr.pantheonsorbonne.cri.model;

import java.util.Queue;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContextAutomaton {

	@Override
	public String toString() {
		return "ContextAutomaton [token=" + token + ", count=" + count + ", previous=" + previous + ", isExclusive="
				+ isExclusive + "]";
	}

	String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	int count;
	ContextAutomaton previous;
	boolean isExclusive;

	


	public static ContextAutomaton getRandom(int expectedIncoming,ContextAutomaton previous) {
		ContextAutomaton c = new ContextAutomaton();
		c.setToken(UUID.randomUUID().toString());
		c.setCount(expectedIncoming);
		c.previous=previous;
		return c;
	}

	public ContextAutomaton pop() {
		return this.previous;
	}

}
