package com.visma.approval.ruleengine.model;

import com.google.gson.Gson;
import com.visma.approval.TestUtils;
import com.visma.approval.ruleengine.Workflow;
import com.visma.approval.ruleengine.WorkflowRepository;
import com.visma.approval.ruleengine.dto.WorkflowStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by robert on 06.03.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkflowTest {
    @Autowired
    WorkflowRepository workflowRepository;
    private static Gson gson = new Gson();
    @Test
    public void generateWorkflowJson(){
        WorkflowStep step1 = new WorkflowStep(null,null,1,1,1002L,null);
        WorkflowStep step2 = new WorkflowStep(null,null,1,2,null,"amount below");

        Workflow workflow = new Workflow(null,new HashSet<>(Arrays.asList(step1,step2)),"Visma.net Cost Request 1.0",true);
        String result = gson.toJson(workflow);
        System.out.println(result);
    }

    @Test
    public void saveWorkflowFromJson() throws IOException{
        List<String> jsons = TestUtils.resourceFolderFileContents("workflow/valid");

        for (String json : jsons) {

            Workflow workflow = gson.fromJson(json,Workflow.class);
            workflow.prepareForSave();
            workflowRepository.save(workflow);
        }
    }

}
