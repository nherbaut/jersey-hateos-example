package fr.pantheonsorbonne.cri.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.MonitoringInfo;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.Node.NodeType;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class MonitoringStubMessageHandler extends StubMessageHandlerImpl {

	private StubMessageHandler delegate;
	private final Path monitoringOutputFilePath;

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringStubMessageHandler.class);

	public MonitoringStubMessageHandler(StubMessage message, String nodeIdentifier, StubMessageHandler delegate) {
		super(message, nodeIdentifier);
		this.delegate = delegate;
		try {
			if (Strings.isNullOrEmpty(System.getenv("MONITORING"))) {
				this.monitoringOutputFilePath = Paths.get("./monitoring.csv");
			} else {
				this.monitoringOutputFilePath = Paths.get(System.getenv("MONITORING"));
				if (!Files.exists(this.monitoringOutputFilePath)) {
					if (!Files.exists(this.monitoringOutputFilePath.getParent())) {
						Files.createDirectories(this.monitoringOutputFilePath.getParent());
					}
				}

			}

		} catch (IOException e) {
			throw new RuntimeException("Failed to create monitoring file", e);

		}

	}

	@Override
	public synchronized void handleStubMessage() {

		this.getMessage().pushMonitoring(this.nodeIdentifier);
		Node n = this.getMessage().getNodeFromId(nodeIdentifier);

		try {
			File csvOutputFile = monitoringOutputFilePath.toFile();
			String deploymentMode = StubMessageHandlerBuilder.getNextHopClass().getSimpleName();

			long initialEpoch = this.getMessage().getMonitoringInfo().get(0).getEpoch();
			try (FileWriter writer = new FileWriter(csvOutputFile, true)) {
				MonitoringInfo info = this.getMessage().getLastMonitoringInfo();
				writer.write(String.format("%d,%s,%s,%s,%s,%d,%d%n", Instant.now().toEpochMilli(),
						this.getMessage().getId(), info.getNodeName(), n.getNodeType().toString(), deploymentMode,
						info.getEpoch(), info.getEpoch() - initialEpoch));

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		delegate.handleStubMessage();

	}

}
