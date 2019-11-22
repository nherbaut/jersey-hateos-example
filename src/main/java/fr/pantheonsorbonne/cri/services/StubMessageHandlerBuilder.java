package fr.pantheonsorbonne.cri.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.endpoints.StubMessageResource;
import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public abstract class StubMessageHandlerBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(StubMessageHandlerBuilder.class);

	public static StubMessageHandler of(StubMessage message, Node n) {

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
			return new WrapperStubMessageHandler(StubMessageHandlerBuilder.of(message, message.firstNext(n)),
					n.getId());
		case SINK:
			return new WrapperStubMessageHandler(new ProcessingStubMessageHandler(message, n.getId()), n.getId());
		case TASK:
			return new WrapperStubMessageHandler(new TaskStubMessageHandler(message, n.getId()), n.getId());

		}

		throw new RuntimeException("Invalid NodeType/Gateway");

	}

}
