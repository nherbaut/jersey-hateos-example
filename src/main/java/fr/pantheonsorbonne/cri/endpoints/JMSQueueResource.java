package fr.pantheonsorbonne.cri.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.research.ws.wadl.Application;

import fr.pantheonsorbonne.cri.model.StubMessage;
import fr.pantheonsorbonne.cri.services.JMSClientStubMessageHandler;
import fr.pantheonsorbonne.cri.services.JMSUtils;
import fr.pantheonsorbonne.cri.services.StubMessageHandlerBuilder;

@Path("/jms")
public class JMSQueueResource {

	@Path("/{nodeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public Response init(StubMessage message,@PathParam("nodeId") String nodeId) {

		JMSUtils.initQueues(message);
		new JMSEndoint();

		StubMessageHandlerBuilder.setNextHopClass(JMSClientStubMessageHandler.class);
		
		StubMessageHandlerBuilder.of(message, message.getNodeFromId(nodeId)).handleStubMessage();
		return Response.ok().build();

	}
}
