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
		switch (n.getNodeType()) {
		case GATEWAY:
			switch (n.getGatewayNature()) {
			case EXCLUSIVE_CONVERGING:
				return new WrapperStubMessageHandler(StubMessageHandlerBuilder.of(message, message.firstNext(n)),
						n.getId());
			case EXCLUSIVE_DIVERGING:
				return new WrapperStubMessageHandler(StubMessageHandlerBuilder.of(message, message.randNext(n)),
						n.getId());
			case PARALLEL_CONVERGING:
				return new WrapperStubMessageHandler(new ParallelStubMessageHolder(message, n.getId()), n.getId());
			case PARALLEL_DIVERGING:
				return new WrapperStubMessageHandler(new ParallelStubMessageHandler(message, n.getId()), n.getId());
			}
			break;
		case SOURCE:
			message.setStartEpoch(Instant.now().toEpochMilli());
			return new WrapperStubMessageHandler(StubMessageHandlerBuilder.of(message, message.firstNext(n)),
					n.getId());
		case SINK:
			message.setEndEpoch((Instant.now().toEpochMilli()));

			return new WrapperStubMessageHandler(new CompositeStubMessageHandler(message, n.getId(),
					ProcessingStubMessageHandler.class, Monitoring.class), n.getId());
		case TASK:
			return new WrapperStubMessageHandler(new TaskStubMessageHandler(message, n.getId(), nextHopHandler),
					n.getId());

		}

		throw new RuntimeException("Invalid NodeType/Gateway");

	}

	public static StubMessageHandler of(StubMessage message, Node n) {
		return of(message, n, nextHopClass);
	}

}
