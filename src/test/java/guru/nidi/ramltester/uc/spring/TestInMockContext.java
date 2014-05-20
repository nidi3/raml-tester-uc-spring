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
import guru.nidi.ramltester.RamlTester;
import guru.nidi.ramltester.core.RamlReport;
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

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        api = RamlTester.fromClasspath(getClass()).load("api.yaml").assumingServletUri("http://nidi.guru/raml/simple/v1");
    }

    @Test
    public void testGreetingWithMatcher() throws Exception {
        this.mockMvc.perform(get("/greeting").accept(MediaType.parseMediaType("application/json")))
                .andExpect(api.matches())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }

    @Test
    public void testGreetingWithMvcResult() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(get("/greeting").accept(MediaType.parseMediaType("application/json"))).andReturn();
        final RamlReport report = api.testAgainst(mvcResult);
        Assert.assertTrue(report.isEmpty());
    }


}