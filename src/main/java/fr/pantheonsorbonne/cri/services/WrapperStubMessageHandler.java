package fr.pantheonsorbonne.cri.services;

public abstract class WrapperStubMessageHandler implements StubMessageHandler {

	protected StubMessageHandlerImpl delegate;

	public WrapperStubMessageHandler(StubMessageHandlerImpl delegate) {
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

}
