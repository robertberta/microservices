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
package com.visma.approval.ruleengine.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.approval.TestUtils;
import com.visma.approval.ruleengine.Workflow;
import com.visma.approval.ruleengine.WorkflowRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkflowControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WorkflowRepository workflowRepository;
    private ObjectMapper objectMapper= new ObjectMapper();

    @Test
    public void addValidWorkflows() throws Exception {

        workflowRepository.deleteAll();

        List<String> jsonWorkflows = TestUtils.resourceFolderFileContents("workflow/valid");

        for (String jsonWorkflow : jsonWorkflows) {

            MvcResult result =
                    this.mockMvc.perform(post("/workflow").content(jsonWorkflow))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String response = result.getResponse().getContentAsString();

            Workflow workflow = workflowRepository.findOne(Long.parseLong(response));
            Workflow expectedWorkflow = objectMapper.readValue(jsonWorkflow,Workflow.class);

            assertEquals(workflow.getApplicationName(),expectedWorkflow.getApplicationName());
            assertEquals(workflow.getWorkflowSteps().size(),expectedWorkflow.getWorkflowSteps().size());

            activateWorkflow();
        }
    }

    @Test
    public void addInvalidWorkflowShouldFail() throws Exception {
        List<String> jsonWorkflows = TestUtils.resourceFolderFileContents("workflow/invalid");

        for (String jsonWorkflow : jsonWorkflows) {

            this.mockMvc.perform(post("/workflow").content(jsonWorkflow)).
                    andDo(print()).
                    andExpect(status().is4xxClientError());
        }
    }

    private void activateWorkflow() throws Exception {

        Workflow workflow = workflowRepository.findByApplicationName("Visma.net Cost Request 1.0").get(0);
        workflow.setActive(false);
        workflowRepository.save(workflow);

        this.mockMvc.perform(post("/workflow/activ").param("workflowId",workflow.getId().toString())).
                    andDo(print()).
                    andExpect(status().isOk());

        workflow = workflowRepository.findOne(workflow.getId());
        assertTrue(workflow.getActive());
    }

}
