package fr.pantheonsorbonne.cri.endpoints;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.pantheonsorbonne.cri.model.ClusterConfig;
import fr.pantheonsorbonne.cri.model.ClusterConfigWrapper;
import fr.pantheonsorbonne.cri.services.JMSClientStubMessageHandler;
import fr.pantheonsorbonne.cri.services.JMSUtils;
import fr.pantheonsorbonne.cri.services.RestClientStubMessageHandler;
import fr.pantheonsorbonne.cri.services.StubMessageHandlerBuilder;

@Path("/jms")
public class JMSQueueResource {

	@POST
	public void enableJMS() {
		StubMessageHandlerBuilder.setNextHopClass(JMSClientStubMessageHandler.class);
	}

	@DELETE
	public void disableJMS() {
		StubMessageHandlerBuilder.setNextHopClass(RestClientStubMessageHandler.class);
	}

	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<ClusterConfig> dummy() {
		ClusterConfigWrapper wrapper = new ClusterConfigWrapper();
		for (int i = 0; i < 3; i++) {
			ClusterConfig config = new ClusterConfig();
			config.setBrokerURL("tcp://toto:61616");
			config.setHost("h0");
			config.setIp("192.168.0.1");
			config.setUser("root");
			wrapper.getClusterConfig().add(config);
		}

		return wrapper.getClusterConfig();

	}

	@Path("/queues")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void initQueues(List<ClusterConfig> configs) {
		for (ClusterConfig config : configs)
			JMSUtils.initQueue(config);

	}

	@Path("/queue")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void initQueue(ClusterConfig config) {

		JMSUtils.initQueue(config);

	}

}
