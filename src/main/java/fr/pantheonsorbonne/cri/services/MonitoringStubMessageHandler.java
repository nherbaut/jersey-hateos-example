package fr.pantheonsorbonne.cri.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import com.google.common.base.Strings;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.MonitoringInfo;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.Node.NodeType;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class MonitoringStubMessageHandler extends StubMessageHandlerImpl {

	private StubMessageHandler delegate;
	private final Path monitoringOutputFilePath;

	public MonitoringStubMessageHandler(StubMessage message, String nodeIdentifier, StubMessageHandler delegate) {
		super(message, nodeIdentifier);
		this.delegate = delegate;
		if (Strings.isNullOrEmpty(System.getenv("MONITORING"))) {
			this.monitoringOutputFilePath = Paths.get("./monitoring.csv");
		} else {
			this.monitoringOutputFilePath = Paths.get(System.getenv("MONITORING"));
			if (!Files.exists(this.monitoringOutputFilePath)) {
				if (!Files.exists(this.monitoringOutputFilePath.getParent())) {
					try {
						Files.createDirectories(this.monitoringOutputFilePath.getParent());
					} catch (IOException e) {
						throw new RuntimeException("Failed to create monitoring file", e);
					}
				}
			}

		}

	}

	@Override
	public void handleStubMessage() {

		this.message.pushMonitoring(this.nodeIdentifier);
		Node n = this.message.getNodeFromId(nodeIdentifier);

		if (n.getNodeType().equals(NodeType.SINK)) {
			try {
				File csvOutputFile = monitoringOutputFilePath.toFile();
				String deploymentMode = StubMessageHandlerBuilder.getNextHopClass().getSimpleName();

				long initialEpoch = this.message.getMonitoringInfo().get(0).getEpoch();
				try (FileWriter writer = new FileWriter(csvOutputFile, true)) {
					for (MonitoringInfo info : this.message.getMonitoringInfo()) {
						writer.write(String.format("%s,%s,%s,%d,%d%n", info.getNodeName(), n.getNodeType().toString(),
								deploymentMode, info.getEpoch(), info.getEpoch() - initialEpoch));
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		delegate.handleStubMessage();

	}

}
