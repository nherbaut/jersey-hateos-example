package fr.labri.services;

import javax.inject.Inject;
import javax.inject.Named;

import fr.labri.model.StubMessage;

public class StubMessageHandlerImpl implements StubMessageHandler {

	@Inject
	@Named("processing")
	StubMessageHandler processingHandler;

	@Inject
	@Named("save")
	StubMessageHandler saveHandler;

	@Inject
	@Named("load")
	StubMessageHandler loadHandler;

	@Inject
	@Named("parallel")
	StubMessageHandler nextHopHandler;

	@Override
	public void handleStubMessage(StubMessage message) {
		processingHandler.handleStubMessage(message);
		saveHandler.handleStubMessage(message);
		loadHandler.handleStubMessage(message);
		nextHopHandler.handleStubMessage(message);

		

	}

}
