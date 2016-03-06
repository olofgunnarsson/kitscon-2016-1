package se.kits;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.autocom.mocktools.WiremockStubSnippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DbServiceApplication.class)
@WebAppConfiguration
public class DbServiceApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Rule
    public RestDocumentation documentation = new RestDocumentation("target/mappings");
    private ResultActions action;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(documentationConfiguration(documentation)
                        .snippets()
                        .withDefaults(new WiremockStubSnippet()))
                .build();
    }

    @Test
    public void shouldGetTest() throws Exception {
        whenClientPerformsGET();

        thenStatusIsOKAndTextContainsStringTest();
    }

    private void thenStatusIsOKAndTextContainsStringTest() throws Exception {
        statusIsOK();
        action.andExpect(jsonPath("text").value("test"));
    }

    private void statusIsOK() throws Exception {
        action.andExpect(status().isOk());
    }

    private void whenClientPerformsGET() throws Exception {
        action = mockMvc.perform(get("/"));
    }

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            try {
                action.andDo(document(description.getMethodName()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            super.succeeded(description);
        }
    };


}
