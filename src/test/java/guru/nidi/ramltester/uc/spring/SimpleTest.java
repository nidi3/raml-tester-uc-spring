package guru.nidi.ramltester.uc.spring;

import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.spring.RequestResponseMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static guru.nidi.ramltester.spring.RamlResultMatchers.requestResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class SimpleTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private RamlDefinition api;
    private RequestResponseMatchers requestResponse;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        api = RamlDefinition.fromClasspath(getClass(), "api.yaml");
        requestResponse = requestResponse().withServletUri("http://nidi.guru/raml/simple/v1");
    }

    @Test
    public void greeting() throws Exception {
        this.mockMvc.perform(get("/greeting").accept(MediaType.parseMediaType("application/json")))
                .andExpect(requestResponse.matchesRaml(api))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }

}