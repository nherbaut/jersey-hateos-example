package fr.pantheonsorbonne.cri.services;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import javax.inject.Inject;
import javax.inject.Named;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Payload;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class TaskStubMessageHandler extends StubMessageHandlerImpl {

	public TaskStubMessageHandler(StubMessage message, String nodeIdentifier,
			Class<? extends StubMessageHandler> messagingClass) {
		super(message, nodeIdentifier);

		processingHandler = new ProcessingStubMessageHandler(message, nodeIdentifier);

		try {
			nextHopHandler = (StubMessageHandler) messagingClass.getConstructors()[0].newInstance(message,
					nodeIdentifier);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {

			throw new RuntimeException("can't build nextHophandler From " + messagingClass.getName());
		}

	}

	public TaskStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

		processingHandler = new ProcessingStubMessageHandler(message, nodeIdentifier);

		nextHopHandler = new RestClientStubMessageHandler(message, nodeIdentifier);

	}

	private final StubMessageHandler processingHandler;
	private final StubMessageHandler nextHopHandler;

	@Override
	public ExecutionTrace handleStubMessage() {
		ExecutionTrace trace = processingHandler.handleStubMessage();
		trace.add(nextHopHandler.handleStubMessage());

		return trace;

	}

}
