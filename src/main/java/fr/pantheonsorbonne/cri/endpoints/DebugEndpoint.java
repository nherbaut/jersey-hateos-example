package fr.pantheonsorbonne.cri.endpoints;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import fr.pantheonsorbonne.cri.model.StubMessage;
import fr.pantheonsorbonne.cri.services.ParallelStubMessageHolder;

@Path("/debug")
public class DebugEndpoint {

	@GET
	@Path("/onHoldMessages")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<StubMessage> getMessageOnHold() {
		List<StubMessage> res = new ArrayList<>(ParallelStubMessageHolder.getOnholdmessages().values());

		try {
			StringWriter writer = new StringWriter();
			JAXBContext.newInstance(StubMessage.class).createMarshaller().marshal(res, writer);
			System.out.println(writer.toString());
		} catch (JAXBException e) {
			System.out.println(e);
		}
		return res;

	}

}
