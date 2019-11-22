package fr.pantheonsorbonne.cri.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class Monitoring extends StubMessageHandlerImpl {

	public Monitoring(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	@Override
	public ExecutionTrace handleStubMessage() {
		try {
			File csvOutputFile = new File("monitoring.csv");
			String deploymentMode = StubMessageHandlerBuilder.getNextHopClass()
					.equals(JMSClientStubMessageHandler.class) ? "JMS" : "REST";
			try (FileWriter writer = new FileWriter(csvOutputFile, true)) {
				writer.write(Instant.now().toEpochMilli() + "," + deploymentMode + ","
						+ (this.message.getEndEpoch() - this.message.getStartEpoch()) + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ExecutionTrace();
	}

}
