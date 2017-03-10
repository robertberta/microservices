/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.visma.approval.facade.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import com.google.gson.Gson;
import com.visma.approval.ruleengine.Process;

import com.visma.approval.TestUtils;
import com.visma.approval.facade.Document;
import com.visma.approval.facade.DocumentRepository;
import com.visma.approval.ruleengine.dto.ProcessStatus;
import com.visma.approval.ruleengine.dto.ProcessStep;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DocumentRepository documentRepository;

    @MockBean
    private RestTemplate ruleEngine;

    private static Gson gson=new Gson();

    @Before
    public void deleteDocuments(){
        documentRepository.deleteAll();
    }

    private ObjectMapper objectMapper;

    @Test
    public void resolvedDocumentShouldReturnPending() throws Exception {

        //given
        Process process = new Process();
        process.setStatus(ProcessStatus.PENDING);
        process.setSteps(new ArrayList<ProcessStep>());
        String strProcess = gson.toJson(process);

        //when
        List<String> xmls = TestUtils.resourceFolderFileContents("document/valid");
        when(ruleEngine.postForObject(anyString(),anyLong(),any()))
                .thenReturn(strProcess);

        for (String xml : xmls) {
            this.mockMvc.perform(post("/document").content(xml)).andDo(print()).andExpect(status().isOk());
        }

        //than
        assertEquals(Iterators.size(documentRepository.findAll().iterator()),xmls.size());
    }

    @Ignore
    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {

        this.mockMvc.perform(get("/greeting").param("name", "Spring Community"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello, Spring Community!"));
    }

}
