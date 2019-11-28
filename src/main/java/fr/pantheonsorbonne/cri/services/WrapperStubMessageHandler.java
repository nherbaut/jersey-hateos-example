package fr.pantheonsorbonne.cri.services;

import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class WrapperStubMessageHandler implements StubMessageHandler {

	protected StubMessageHandler delegate;

	public WrapperStubMessageHandler(StubMessageHandler delegate) {
		this.delegate = delegate;

	}

	@Override
	public void handleStubMessage() {
		this.preHandleAction();
		delegate.handleStubMessage();
		this.postHandleAction();

	}

	protected abstract void postHandleAction();

	protected abstract void preHandleAction();

	@Override
	public String getNodeIdentifier() {
		return this.delegate.getNodeIdentifier();
	}

	@Override
	public StubMessage getMessage() {
		return this.delegate.getMessage();
	}

}
