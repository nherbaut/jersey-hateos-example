package fr.pantheonsorbonne.cri.services;

import javax.inject.Inject;
import javax.inject.Named;

import fr.pantheonsorbonne.cri.model.StubMessage;

public class StubMessageHandlerImpl extends StubMessageHandler {

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
	public void handleStubMessage(StubMessage message, String myIdentity) {
		processingHandler.handleStubMessage(message, myIdentity);
		saveHandler.handleStubMessage(message, myIdentity);
		loadHandler.handleStubMessage(message, myIdentity);
		nextHopHandler.handleStubMessage(message, myIdentity);

	}

}
