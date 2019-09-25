package fr.labri.model;

public class StubMessagePayload {
	@Override
	public String toString() {
		return "StubMessagePayload [instructions=" + instructions + ", savedByteCount=" + savedByteCount
				+ ", loadedByteCount=" + loadedByteCount + "]";
	}
	private long instructions;
	private long savedByteCount;
	private long loadedByteCount;
	public long getInstructions() {
		return instructions;
	}
	public void setInstructions(long instructions) {
		this.instructions = instructions;
	}
	public long getSavedByteCount() {
		return savedByteCount;
	}
	public void setSavedByteCount(long savedByteCount) {
		this.savedByteCount = savedByteCount;
	}
	public long getLoadedByteCount() {
		return loadedByteCount;
	}
	public void setLoadedByteCount(long loadedByteCount) {
		this.loadedByteCount = loadedByteCount;
	}
}
