package fr.labri.endpoints;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.labri.utils.Utils;
import fr.labri.model.Box;

@Path("boxes")
public class BoxResources {

	private final static Random rand = new Random();

	@Inject
	UserResource ur;

	@GET
	@Produces(value = { MediaType.APPLICATION_XML })
	public List<Box> getAll() {
		Box b = new Box();
		b.setId(UUID.randomUUID().toString());
		b.setName(Utils.getRandomString());
		b.setUsers(ur.getAll());
		b.setIp(rand.nextInt(256) + "." + rand.nextInt(256) + "."
				+ rand.nextInt(256) + "." + rand.nextInt(256));
		Box b1 = new Box();
		b1.setId(UUID.randomUUID().toString());
		b1.setUsers(ur.getAll());
		b1.setName(Utils.getRandomString());
		b1.setIp(rand.nextInt(256) + "." + rand.nextInt(256) + "."
				+ rand.nextInt(256) + "." + rand.nextInt(256));

		return Arrays.asList(b, b1);

	}

	@GET
	@Path("/{boxId}")
	@Produces(value = { MediaType.APPLICATION_XML })
	public Box get(@PathParam("boxId") String boxId) {
		Box b = new Box();
		b.setId(boxId);
		b.setName(Utils.getRandomString());
		b.setIp(rand.nextInt(256) + "." + rand.nextInt(256) + "."
				+ rand.nextInt(256) + "." + rand.nextInt(256));
		return b;

	}
}
