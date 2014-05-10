package guru.nidi.ramltester.uc.spring;

import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.RamlReport;
import guru.nidi.ramltester.TestRaml;
import guru.nidi.ramltester.spring.RequestResponseMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
public class TestInMockContext {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private RamlDefinition api;
    private RequestResponseMatchers requestResponse;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        api = TestRaml.load("api.yaml").fromClasspath(getClass());
        requestResponse = requestResponse().withServletUri("http://nidi.guru/raml/simple/v1");
    }

    @Test
    public void testGreetingWithMatcher() throws Exception {
        this.mockMvc.perform(get("/greeting").accept(MediaType.parseMediaType("application/json")))
                .andExpect(requestResponse.matchesRaml(api))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }

    @Test
    public void testGreetingWithMvcResult() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(get("/greeting").accept(MediaType.parseMediaType("application/json"))).andReturn();
        final RamlReport report = api.testAgainst(mvcResult, "http://nidi.guru/raml/simple/v1");
        Assert.assertTrue(report.isEmpty());
    }


}