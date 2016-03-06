package se.kits;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class EdgeServiceApi {

    private static final URI DB_SERVICE_URI = URI.create("http://localhost:8080/");

    @RequestMapping(value = "/", method = GET, produces = "application/json")
    public EdgeResponse get() {
        final String forObject = new RestTemplate().getForObject(DB_SERVICE_URI, ObjectNode.class).get("text").asText();
        return new EdgeResponse("db-service said: " + forObject);
    }

    @Data
    public static class EdgeResponse {
        final String text;
    }

}
