package fr.labri.services;

import javax.inject.Inject;
import javax.inject.Named;

import fr.labri.model.StubMessage;

public class ParallelStubMessageHandler implements StubMessageHandler {

	@Inject
	@Named("nextHop")
	StubMessageHandler nestedHandler;

	@Override
	public void handleStubMessage(StubMessage message) {
		message.getNextMessages().parallelStream().forEach(s -> nestedHandler.handleStubMessage(s));

	}

}
