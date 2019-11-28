package fr.pantheonsorbonne.cri.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.StubMessage;

public class RestClientStubMessageHandler extends StubMessageHandlerImpl {

	public RestClientStubMessageHandler(StubMessage message, String nodeIdentifier) {
		super(message, nodeIdentifier);

	}

	private final Client client = ClientBuilder.newClient();
	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientStubMessageHandler.class);

	@Override
	public void handleStubMessage() {

		String nextNodeId = this.getMessage().firstNext(this.getMessage().getNodeFromId(this.nodeIdentifier)).getId();
		WebTarget target = client.target(UriBuilder.fromUri(this.getMyNode().getUrl()).path(nextNodeId).build());
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(this.getMessage(), MediaType.APPLICATION_JSON));
		if (response.getStatus() != 200) {
			throw new RuntimeException("response is " + response.getStatus());
		}

	}

}
