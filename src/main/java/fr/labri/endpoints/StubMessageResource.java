package fr.labri.endpoints;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.labri.model.StubMessage;
import fr.labri.model.StubMessagePayload;
import fr.labri.services.StubMessageHandler;

@Path("/")
public class StubMessageResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(StubMessageResource.class);

	@Inject
	@Named("composite")
	StubMessageHandler handler;

	@POST
	@Consumes(value = MediaType.APPLICATION_JSON)
	public Response welcome(StubMessage message) {
		handler.handleStubMessage(message);
		return Response.ok().build();

	}

	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public StubMessage dummy() {
		StubMessage mess = new StubMessage();
		mess.setNextHop("nexthop");
		StubMessagePayload payload = new StubMessagePayload();
		payload.setInstructions(1000);
		payload.setLoadedByteCount(9999);
		payload.setSavedByteCount(8888);
		mess.setPayload(payload);

		StubMessage mess2 = new StubMessage();
		mess2.setNextHop("nexthop2");
		mess.setNextMessages(Arrays.asList(mess2));
		return mess;
	}

}
