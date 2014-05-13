/*
 * Copyright (C) ${project.inceptionYear} Stefan Niederhauser (nidin@gmx.ch)
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

import guru.nidi.ramltester.RamlDefinition;
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
        restTemplate = RamlDefinition.load("api.yaml").fromClasspath(getClass())
                .createRestTemplate(new HttpComponentsClientHttpRequestFactory()).assumingBaseUri("http://nidi.guru/raml/simple/v1");
    }

    @Test
    public void testGreetingWithRestTemplate() {
        final Greeting greeting = restTemplate.getForObject("http://localhost:8080/greeting", Greeting.class);
        Assert.assertTrue(restTemplate.getLastReport().isEmpty());
    }


}