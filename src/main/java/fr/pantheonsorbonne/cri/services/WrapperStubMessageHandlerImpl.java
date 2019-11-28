package fr.pantheonsorbonne.cri.services;

public class WrapperStubMessageHandlerImpl extends WrapperStubMessageHandler {

	public WrapperStubMessageHandlerImpl(StubMessageHandlerImpl delegate) {
		super(delegate);

	}

	@Override
	protected void postHandleAction() {
		// nothing

	}

	@Override
	protected void preHandleAction() {
		// nothing

	}

}
