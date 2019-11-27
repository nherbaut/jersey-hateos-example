package fr.pantheonsorbonne.cri.endpoints;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.google.common.collect.Streams;

import fr.pantheonsorbonne.cri.model.ContextAutomaton;
import fr.pantheonsorbonne.cri.model.ExecutionTrace;
import fr.pantheonsorbonne.cri.model.Link;
import fr.pantheonsorbonne.cri.model.Node;
import fr.pantheonsorbonne.cri.model.Payload;
import fr.pantheonsorbonne.cri.model.StubMessage;
import fr.pantheonsorbonne.cri.services.StubMessageHandlerImpl;
import fr.pantheonsorbonne.cri.services.StubMessageHandlerBuilder;

@Path("/")
public class StubMessageResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(StubMessageResource.class);

	@Path("/{identity}")
	@POST()
	@Consumes(value = MediaType.APPLICATION_JSON)
	public Response welcome(StubMessage message, @PathParam("identity") String id) {
		LOGGER.info("New Message to {}" , id);
		LOGGER.debug("received for {} with context {}", id, message.getContext());
		StubMessageHandlerBuilder.of(message, message.getNodeFromId(id)).handleStubMessage();
		return Response.ok().build();

	}

	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public StubMessage dummy() throws InterruptedException {

		Thread.sleep(100);
		LOGGER.debug("I am a message");
		StubMessage mess = new StubMessage();
		mess.setDirected(false);
		mess.setMultigraph(false);
		Link l = new Link();
		l.setSource("source");
		l.setTarget("target");
		mess.setLinks(Stream.of(l, l, l).collect(Collectors.toSet()));

		Node n = new Node();
		n.setId("id");
		n.setType("type");
		Payload payload = new Payload();
		payload.setDummyPaddings(0);
		payload.setInByteCount(1);
		payload.setInstructions(2);
		payload.setOutByteCount(3);
		n.setPayload(payload);

		mess.setNodes(Stream.of(n, n, n).collect(Collectors.toSet()));

		mess.setContext(ContextAutomaton.getRandom(3, ContextAutomaton.getRandom(5, null)));

		return mess;
	}

	@GET
	@Path("dummy")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ExecutionTrace dummy2() throws InterruptedException {

		ExecutionTrace et = new ExecutionTrace();

		et.getNodes().add("toto");
		et.getNodes().add("titi");
		et.getPayload().setDummyPaddings(100);
		return et;
	}

}
