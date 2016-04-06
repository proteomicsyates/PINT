package edu.scripps.yates.annotations.biogrid;

import java.io.StringReader;
import java.net.URI;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class BiogridClient {
	private final static String bioGridKey = "85f79b4fdd3dabc91afe9ba97a53b40d";

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://webservice.thebiogrid.org/").build();
	}

	public static String getResponse() {
		ClientConfig config = new ClientConfig();

		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target(getBaseURI());

		String response = target.path("interactions").path("103").queryParam("accessKey", bioGridKey)
				.queryParam("format", "json").request().accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
		final JsonParser jsonParser = Json.createParser(new StringReader(response));
		while (jsonParser.hasNext()) {
			JsonParser.Event event = jsonParser.next();
			switch (event) {
			case START_ARRAY:
			case END_ARRAY:
			case START_OBJECT:
			case END_OBJECT:
			case VALUE_FALSE:
			case VALUE_NULL:
			case VALUE_TRUE:
				System.out.println(event.toString());
				break;
			case KEY_NAME:
				System.out.print(event.toString() + " " + jsonParser.getString() + " - ");
				break;
			case VALUE_STRING:
			case VALUE_NUMBER:
				System.out.println(event.toString() + " " + jsonParser.getString());
				break;
			}
		}
		return response;
	}

	public static void main(String[] args) {
		System.out.println(BiogridClient.getResponse());
	}
}
