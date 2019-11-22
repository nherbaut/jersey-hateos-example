package fr.pantheonsorbonne.cri.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class StubMessageHandlerBuilder {

	private StubMessageHandlerBuilder() {
	};

	private static final Logger LOGGER = LoggerFactory.getLogger(StubMessageHandlerBuilder.class);
	private static Class<? extends StubMessageHandler> nextHopClass = RestClientStubMessageHandler.class;

	public static Class<? extends StubMessageHandler> getNextHopClass() {
		return nextHopClass;
	}

	public static void setNextHopClass(Class<? extends StubMessageHandler> nextHopClass) {
		StubMessageHandlerBuilder.nextHopClass = nextHopClass;
	}

	public static StubMessageHandler of(StubMessage message, Node n,
			Class<? extends StubMessageHandler> nextHopHandler) {

		StubMessageHandler res = null;
		switch (n.getNodeType()) {
		case GATEWAY:
			switch (n.getGatewayNature()) {
			case EXCLUSIVE_CONVERGING:
				res = StubMessageHandlerBuilder.of(message, message.firstNext(n));
				break;
			case EXCLUSIVE_DIVERGING:
				res = StubMessageHandlerBuilder.of(message, message.randNext(n));
				break;
			case PARALLEL_CONVERGING:
				res = new ParallelStubMessageHolder(message, n.getId());
				break;
			case PARALLEL_DIVERGING:
				res = new ParallelStubMessageHandler(message, n.getId());
				break;
			}
			break;
		case SOURCE:

			res = StubMessageHandlerBuilder.of(message, message.firstNext(n));
			break;
		case SINK:
			res = new ProcessingStubMessageHandler(message, n.getId());
			break;
		case TASK:
			res = new TaskStubMessageHandler(message, n.getId(), nextHopHandler);
			break;

		}

		if (res == null) {
			throw new RuntimeException("Invalid NodeType/Gateway");
		}

		return new Monitoring(message, n.getId(), res);

	}

	public static StubMessageHandler of(StubMessage message, Node n) {
		return of(message, n, nextHopClass);
	}

}
