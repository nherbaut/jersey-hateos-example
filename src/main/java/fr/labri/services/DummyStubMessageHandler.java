package fr.labri.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.labri.model.StubMessage;

public class DummyStubMessageHandler extends StubMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyStubMessageHandler.class);

	@Override
	public void handleStubMessage(StubMessage message, String myIdentity) {
		

	}

}
