package guru.nidi.ramltester.uc.spring;

import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.TestRaml;
import guru.nidi.ramltester.spring.RamlRestTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 *
 */
public class TestWithRestTemplate {

    private RamlRestTemplate restTemplate;

    @Before
    public void setup() {
        Application.main();
        final RamlDefinition api = TestRaml.load("api.yaml").fromClasspath(getClass());
        restTemplate = api.createRestTemplate(new HttpComponentsClientHttpRequestFactory()).withBaseUri("http://nidi.guru/raml/simple/v1");
    }

    @Test
    public void testGreetingWithRestTemplate() {
        final Greeting greeting = restTemplate.getForObject("http://localhost:8080/greeting", Greeting.class);
        Assert.assertTrue(restTemplate.getLastReport().isEmpty());
    }


}