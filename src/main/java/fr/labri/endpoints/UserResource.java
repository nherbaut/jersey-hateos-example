package fr.labri.endpoints;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.labri.model.User;
import fr.labri.utils.Utils;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("users")
public class UserResource {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Path("/{userId}")
	@Produces(value = { MediaType.APPLICATION_XML })
	public User get(@PathParam("userId") String userId) {
		User u = new User();
		u.setId(userId);
		return u;

	}

	@GET
	@Produces(value = { MediaType.APPLICATION_XML })
	public List<User> getAll() {
		User u = new User();
		u.setId(UUID.randomUUID().toString());
		u.setFirstName(Utils.getRandomString());
		u.setLastName(Utils.getRandomString());
		User u1 = new User();
		u1.setId(UUID.randomUUID().toString());
		u1.setFirstName(Utils.getRandomString());
		u1.setLastName(Utils.getRandomString());

		return Arrays.asList(u, u1);

	}

	@POST
	@Consumes(value = { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, })
	public Response createUser(User u) {

		
		return Response.ok().build();
	}

}
