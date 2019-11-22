package fr.pantheonsorbonne.cri.services;

import java.lang.reflect.InvocationTargetException;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class CompositeStubMessageHandler implements StubMessageHandler {

	private Class<? extends StubMessageHandler> klasses[];
	private StubMessage message;
	private String identity;

	public CompositeStubMessageHandler(StubMessage message, String identity,
			Class<? extends StubMessageHandler>... klasses) {
		this.klasses = klasses;
		this.message = message;
		this.identity = identity;

	}

	@Override
	public ExecutionTrace handleStubMessage() {
		ExecutionTrace trace = new ExecutionTrace();
		try {
			for (Class<? extends StubMessageHandler> klass : klasses) {
				StubMessageHandler handler;

				handler = (StubMessageHandler) klass.getConstructors()[0].newInstance(this.message, this.identity);

				trace.add(handler.handleStubMessage());
			}

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			throw new RuntimeException(e);
		}

		return trace;
	}

}
