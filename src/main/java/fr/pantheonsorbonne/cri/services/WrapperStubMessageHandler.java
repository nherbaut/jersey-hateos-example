package fr.pantheonsorbonne.cri.services;

public class WrapperStubMessageHandler implements StubMessageHandler {

	private StubMessageHandler delegate;
	private String nodeIdentified;

	public WrapperStubMessageHandler(StubMessageHandler delegate, String nodeIdentified) {
		this.delegate = delegate;
		this.nodeIdentified = nodeIdentified;
	}

	@Override
	public void handleStubMessage() {
		delegate.handleStubMessage();

	}

}
