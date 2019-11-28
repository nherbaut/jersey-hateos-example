package fr.pantheonsorbonne.cri.services;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.ContextAutomaton;
import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class ParallelStubMessageHolder extends StubMessageHandlerImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParallelStubMessageHolder.class);
	private static final Map<String, StubMessage> onHoldMessages = new HashMap<>();
	private static Object lock = new Object();

	public static void submitMessageOnHold(StubMessage message) {
		onHoldMessages.put(message.getContext().getToken(), message);
	}

	public ParallelStubMessageHolder(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	@Override
	public void handleStubMessage() {

		final String token = this.getMessage().getContext().getToken();
		synchronized (lock) {
			if (onHoldMessages.containsKey(token)) {
				StubMessage message = onHoldMessages.get(token);
				ContextAutomaton context = message.getContext();
				context.setCount(context.getCount() - 1);

				if (context.getCount() == 0) {
					message.setContext(context.pop());
					onHoldMessages.remove(token);
					Node nextNode = message.firstNext(this.getMyNode());
					StubMessageHandler nextHandler = StubMessageHandlerBuilder.of(message, nextNode);

					nextHandler.handleStubMessage();

				}

			}
		}

		
	}

}
