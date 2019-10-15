package fr.pantheonsorbonne.cri.model;

import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.annotations.GwtCompatible;


public class Payload {

	private long instructions;

	private long inByteCount;
	private long outByteCount;
	private long dummyPaddings;

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

}
