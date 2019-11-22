package fr.pantheonsorbonne.cri.model;

import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.annotations.GwtCompatible;

public class Payload {

	@Override
	public String toString() {
		return "Payload [instructions=" + instructions + ", inByteCount=" + inByteCount + ", outByteCount="
				+ outByteCount + ", dummyPaddings=" + dummyPaddings + "]";
	}

	private long instructions;

	private long inByteCount;
	private long outByteCount;
	private long dummyPaddings;
	
	
	public final static Payload EMPTY = new Payload();

	@XmlAttribute(name = "instructions")
	public long getInstructions() {
		return instructions;
	}

	public void setInstructions(long instructions) {
		this.instructions = instructions;
	}

	@XmlAttribute(name = "in_bytes_count")
	public long getInByteCount() {
		return inByteCount;
	}

	public void setInByteCount(long inByteCount) {
		this.inByteCount = inByteCount;
	}

	@XmlAttribute(name = "out_bytes_count")
	public long getOutByteCount() {
		return outByteCount;
	}

	public void setOutByteCount(long outByteCount) {
		this.outByteCount = outByteCount;
	}

	@XmlAttribute(name = "dummy_padding")
	public long getDummyPaddings() {
		return dummyPaddings;
	}

	public void setDummyPaddings(long dummyPaddings) {
		this.dummyPaddings = dummyPaddings;
	}

	public void add(Payload p1) {
		Payload.add(this, p1);
	}

	public static Payload add(Payload p1, Payload p2) {
		
		p1.setDummyPaddings(p1.getDummyPaddings() + p2.getDummyPaddings());
		p1.setInByteCount(p1.getInByteCount() + p1.getInByteCount());
		p1.setInstructions(p1.getInstructions() + p2.getInstructions());
		p1.setOutByteCount(p1.getOutByteCount() + p2.getOutByteCount());
		return p1;
		
	}

}
