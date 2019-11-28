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

public class MonitoringStubMessageHandler extends WrapperStubMessageHandlerImpl {

	final private String nodeId;

	public MonitoringStubMessageHandler(StubMessageHandler delegate, String nodeId) {
		super(delegate);
		this.nodeId = nodeId;

	}

	private static final Path monitoringOutputFilePath;
	private static final FileWriter writer;
	static {
		try {
			if (Strings.isNullOrEmpty(System.getenv("MONITORING"))) {
				monitoringOutputFilePath = Paths.get("./monitoring.csv");
			} else {
				monitoringOutputFilePath = Paths.get(System.getenv("MONITORING"));
				if (!Files.exists(monitoringOutputFilePath)) {
					if (!Files.exists(monitoringOutputFilePath.getParent())) {
						Files.createDirectories(monitoringOutputFilePath.getParent());
					}
				}

			}

			writer = new FileWriter(monitoringOutputFilePath.toFile(), true);

		} catch (IOException e) {
			throw new RuntimeException("Failed to create monitoring file", e);

		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringStubMessageHandler.class);

	@Override
	protected void preHandleAction() {
		this.delegate.getMessage().pushMonitoring(nodeId);
		Node n = this.delegate.getMessage().getNodeFromId(nodeId);

		try {

			String deploymentMode = StubMessageHandlerBuilder.getNextHopClass().getSimpleName();

			long initialEpoch = this.delegate.getMessage().getMonitoringInfo().get(0).getEpoch();

			MonitoringInfo info = this.delegate.getMessage().getLastMonitoringInfo();
			writer.write(String.format("%d,%s,%s,%s,%s,%d,%d%n", Instant.now().toEpochMilli(),
					this.delegate.getMessage().getId(), info.getNodeName(), n.getNodeType().toString(), deploymentMode,
					info.getEpoch(), info.getEpoch() - initialEpoch));
			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
