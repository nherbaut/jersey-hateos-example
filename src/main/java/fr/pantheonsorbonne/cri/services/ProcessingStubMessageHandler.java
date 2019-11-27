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

	public static long compute(long limit) {

		List<Long> res = new ArrayList<>();
		List<Long> ints = LongStream.range(2L, Math.round(Math.pow(10.0, limit))).boxed().collect(Collectors.toList());
		while (!ints.isEmpty()) {
			Long first = ints.get(0);

			res.add(first);
			ints = ints.stream()//
					.filter((Long l) -> Long.remainderUnsigned(l, first) != 0L)
					.collect(Collectors.toList());
		}

		if (!res.isEmpty()) {
			return res.get(res.size() - 1);
		} else {
			return 0;
		}

	}

}
