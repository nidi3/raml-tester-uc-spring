/*
 * Copyright (C) 2014 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.ramltester.uc.spring;

import guru.nidi.ramltester.RamlLoaders;
import guru.nidi.ramltester.SimpleReportAggregator;
import guru.nidi.ramltester.junit.ExpectedUsage;
import guru.nidi.ramltester.spring.RamlRestTemplate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static guru.nidi.ramltester.junit.RamlMatchers.hasNoViolations;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class RestTemplateTest {
    private static SimpleReportAggregator aggregator = new SimpleReportAggregator();

    private static RamlRestTemplate restTemplate = RamlLoaders
            .fromClasspath(RestTemplateTest.class).load("api.raml")
            .assumingBaseUri("http://nidi.guru/raml/simple/v1")
            .createRestTemplate(new HttpComponentsClientHttpRequestFactory())
            .aggregating(aggregator);

    private static ApplicationContext context;

    @ClassRule
    public static ExpectedUsage expectedUsage = new ExpectedUsage(aggregator);

    @BeforeClass
    public static void setup() {
        context = Application.start();
    }

    @AfterClass
    public static void tearDown() {
        Application.stop(context);
    }

    @Test
    public void testGreetingWithRestTemplate() {
        final Greeting greeting = restTemplate.getForObject("http://localhost:8081/greeting", Greeting.class);
        assertThat(restTemplate.getLastReport(), hasNoViolations());
    }

    @Test
    public void testNullResponseName() {
        final Greeting greeting = restTemplate.getForObject("http://localhost:8081/greeting?name=null", Greeting.class);
        assertThat(restTemplate.getLastReport(), hasNoViolations());
    }

    @Test
    public void testOnlyRequestGreetingWithRestTemplate() {
        final Greeting greeting = restTemplate.notSending().getForObject("http://localhost:8081/greeting?name=bla", Greeting.class);
        assertThat(restTemplate.getLastReport(), hasNoViolations());
    }


}