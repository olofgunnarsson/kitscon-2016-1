package se.kits;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class DbServiceApi {

    @RequestMapping(path = "/", method = GET, produces = "application/json")
    public String getFromDb() {
        return "{\"text\": \"test\"}";
    }
}
