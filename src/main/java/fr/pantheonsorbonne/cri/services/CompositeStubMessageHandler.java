package fr.pantheonsorbonne.cri.services;

import javax.inject.Inject;
import javax.inject.Named;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Payload;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class CompositeStubMessageHandler extends StubMessageHandler {

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
	public ExecutionTrace handleStubMessage(StubMessage message, String myIdentity) {
		long instructions = processingHandler.handleStubMessage(message, myIdentity).getPayload().getInstructions();

		long ioS = saveHandler.handleStubMessage(message, myIdentity).getPayload().getInByteCount();
		long ooS = loadHandler.handleStubMessage(message, myIdentity).getPayload().getOutByteCount();
		ExecutionTrace trace = nextHopHandler.handleStubMessage(message, myIdentity);

		trace.getPayload().setInByteCount(trace.getPayload().getInByteCount() + ioS);
		trace.getPayload().setInstructions(trace.getPayload().getInByteCount() + instructions + instructions);
		trace.getPayload().setOutByteCount(trace.getPayload().getOutByteCount() + ooS);
		trace.getNodes().add(myIdentity);
		
		System.out.println("***" + trace);
		return trace;

	}

}
