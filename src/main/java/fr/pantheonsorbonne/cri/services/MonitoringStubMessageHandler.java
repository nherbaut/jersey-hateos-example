package fr.pantheonsorbonne.cri.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.MonitoringInfo;
import fr.pantheonsorbonne.cri.model.Node.NodeType;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class MonitoringStubMessageHandler extends StubMessageHandlerImpl {

	private StubMessageHandler delegate;

	public MonitoringStubMessageHandler(StubMessage message, String nodeIdentifier, StubMessageHandler delegate) {
		super(message, nodeIdentifier);
		this.delegate = delegate;

	}

	@Override
	public void handleStubMessage() {

		this.message.pushMonitoring(this.nodeIdentifier);
		if (this.message.getNodeFromId(nodeIdentifier).getNodeType().equals(NodeType.SINK)) {
			try {
				File csvOutputFile = new File("monitoring.csv");
				String deploymentMode = StubMessageHandlerBuilder.getNextHopClass().getSimpleName();
						

				long initialEpoch = this.message.getMonitoringInfo().get(0).getEpoch();
				try (FileWriter writer = new FileWriter(csvOutputFile, true)) {
					for (MonitoringInfo info : this.message.getMonitoringInfo()) {
						writer.write(String.format("%s,%s,%d,%d%n", info.getNodeName(), deploymentMode, info.getEpoch(),
								info.getEpoch() - initialEpoch));
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		delegate.handleStubMessage();

	}

}
