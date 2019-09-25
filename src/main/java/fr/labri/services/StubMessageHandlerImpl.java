package fr.labri.services;

import javax.inject.Inject;
import javax.inject.Named;

import fr.labri.model.StubMessage;

public class StubMessageHandlerImpl implements StubMessageHandler {

//	@Inject
//	@Named("processing")
	StubMessageHandler processingHandler=new DummyStubMessageHandler();

//	@Inject
//	@Named("save")
	StubMessageHandler saveHandler=new DummyStubMessageHandler();

//	@Inject
//	@Named("load")
	StubMessageHandler loadHandler=new DummyStubMessageHandler();

//	@Inject
//	@Named("nextHop")
	StubMessageHandler nextHopHandler=new RestClientStubMessageHandler();

	@Override
	public void handleStubMessage(StubMessage message) {
		processingHandler.handleStubMessage(message);
		saveHandler.handleStubMessage(message);
		loadHandler.handleStubMessage(message);

		message.getNextMessages().parallelStream().forEach(s -> nextHopHandler.handleStubMessage(s));

	}

}
