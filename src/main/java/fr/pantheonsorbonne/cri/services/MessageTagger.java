package fr.pantheonsorbonne.cri.services;

import java.util.UUID;

import com.google.common.base.Strings;

public class MessageTagger extends WrapperStubMessageHandlerImpl {

	public MessageTagger(StubMessageHandler delegate) {
		super(delegate);

	}

	@Override
	protected void preHandleAction() {
		if (Strings.isNullOrEmpty(this.delegate.getMessage().getId())) {
			this.delegate.getMessage().setId(UUID.randomUUID().toString());
		}
	}

}
