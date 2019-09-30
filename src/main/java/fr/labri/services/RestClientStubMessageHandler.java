package fr.labri.services;

import java.net.URI;

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

import fr.labri.model.Node;
import fr.labri.model.StubMessage;

public class RestClientStubMessageHandler extends StubMessageHandler {

	private final Client client = ClientBuilder.newClient();
	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientStubMessageHandler.class);

	@Override
	public void handleStubMessage(StubMessage message, String myIdentity) {

		WebTarget target = client.target(UriBuilder.fromUri(this.getMyNode(message).getUrl()).path(myIdentity).build());
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(message, MediaType.APPLICATION_JSON));
		LOGGER.info("Rest Call Returned {}",response.getStatus());
		

	}

}
