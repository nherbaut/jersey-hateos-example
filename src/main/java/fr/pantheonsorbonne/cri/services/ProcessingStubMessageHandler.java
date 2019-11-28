package fr.pantheonsorbonne.cri.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class ProcessingStubMessageHandler extends StubMessageHandlerImpl {

	public ProcessingStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingStubMessageHandler.class);

	@Override
	public void handleStubMessage() {

		compute(this.getMyNode().getPayload().getInstructions());

	}

	public static long compute(long n) {

		long res = 0;
		long limit = Math.round(Math.pow(10.0, n));
		for (long i = 1; i < limit; i++) {
			for (long j = 1; j < limit; j++) {
				if (i % j != 0) {
					res = i;
					break;
				}

			}

		}

		return res;
	}

}
