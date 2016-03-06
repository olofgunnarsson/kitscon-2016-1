package se.kits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.autocom.mocktools.AdocFileMappingsLoader;

import java.io.File;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EdgeServiceApplication.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class EdgeServiceApplicationTests {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    @Value("${local.server.port}")
    protected String serverPort;

    @Before
    public void setup() throws JsonProcessingException {
        wireMockRule.loadMappingsUsing(new AdocFileMappingsLoader(new SingleRootFileSource(new File("target/stubs"))));
        wireMockRule.start();
    }

    //@formatter:off
    @Test
    public void shouldRequestDataFromDbService() throws Exception {
        when().
                get(edgeService()).
        then().
                statusCode(200).
                body("text", equalTo("db-service said: test"));

    }
    //@formatter:on

    private String edgeService() {
        return "http://localhost:" + serverPort;
    }


}
