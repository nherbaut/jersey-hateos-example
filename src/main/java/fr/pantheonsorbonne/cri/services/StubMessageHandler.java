package fr.pantheonsorbonne.cri.services;

import fr.pantheonsorbonne.cri.model.StubMessage;

public interface StubMessageHandler {

	void handleStubMessage();

	String getNodeIdentifier();

	StubMessage getMessage();

	

}