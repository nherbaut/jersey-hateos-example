package fr.pantheonsorbonne.cri.endpoints;

import javax.inject.Inject;
import javax.inject.Named;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.pantheonsorbonne.cri.model.Link;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.Payload;
import fr.pantheonsorbonne.cri.model.StubMessage;
import fr.pantheonsorbonne.cri.services.StubMessageHandler;

@Path("/")
public class StubMessageResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(StubMessageResource.class);

	@Inject
	@Named("composite")
	StubMessageHandler handler;

	@Inject
	@Named("nodeIdentifier")
	String nodeIdentifier;

	@POST
	@Consumes(value = MediaType.APPLICATION_JSON)
	public Response welcome(StubMessage message) {
		handler.handleStubMessage(message);
		return Response.ok().build();

	}

	@Path("/{identity}")
	@POST()
	@Consumes(value = MediaType.APPLICATION_JSON)
	public Response welcome(StubMessage message, @PathParam("identity") String id) {
		handler.handleStubMessage(message, id);
		return Response.ok().build();

	}

	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public StubMessage dummy() {
		LOGGER.debug("I am {}", nodeIdentifier);
		StubMessage mess = new StubMessage();
		mess.setDirected(false);
		mess.setMultigraph(false);
		Link l = new Link();
		l.setSource("source");
		l.setTarget("target");
		mess.setLinks(new Link[] { l, l, l });

		Node n = new Node();
		n.setId("id");
		n.setType("type");
		Payload payload = new Payload();
		payload.setDummyPaddings(0);
		payload.setInByteCount(1);
		payload.setInstructions(2);
		payload.setOutByteCount(3);
		n.setPayload(payload);

		mess.setNodes(new Node[] { n, n, n });

		return mess;
	}

}
