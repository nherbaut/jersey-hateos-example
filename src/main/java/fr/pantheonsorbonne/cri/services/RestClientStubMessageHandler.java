package fr.pantheonsorbonne.cri.services;

import java.io.StringReader;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.StubMessage;

public class RestClientStubMessageHandler extends StubMessageHandler {

	private final Client client = ClientBuilder.newClient();
	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientStubMessageHandler.class);

	@Override
	public ExecutionTrace handleStubMessage(StubMessage message, String myIdentity) {

		LOGGER.debug("sending request to " + myIdentity);
		WebTarget target = client.target(UriBuilder.fromUri(this.getMyNode(message).getUrl()).path(myIdentity).build());
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(message, MediaType.APPLICATION_JSON));

		ExecutionTrace t = response.readEntity(ExecutionTrace.class);
		LOGGER.debug("got" + t);
		return t;

	}

}
