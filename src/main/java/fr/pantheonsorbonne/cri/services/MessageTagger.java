package fr.pantheonsorbonne.cri.services;

import java.util.UUID;

public class MessageTagger extends WrapperStubMessageHandlerImpl {

	public MessageTagger(StubMessageHandlerImpl delegate) {
		super(delegate);

	}

	@Override
	protected void preHandleAction() {
		this.delegate.getMessage().setId(UUID.randomUUID().toString());
	}

}
