package com.visma.approval.ruleengine.model;

import com.visma.approval.ruleengine.Workflow;
import com.visma.approval.ruleengine.WorkflowRepository;
import com.visma.approval.ruleengine.controller.exceptions.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by robert on 08.03.2017.
 */
@Component
public class WorkflowHandler {
    @Autowired
    private WorkflowRepository workflowRepository;

    public void save(Workflow workflow){
        workflow.prepareForSave();
        workflowRepository.save(workflow);
    }

    public void activateWorkflow(Long workflowId) throws WorkflowException{
        Workflow workflow= workflowRepository.findOne(workflowId);
        if (workflow == null){
            throw new WorkflowException("Workflow " + workflowId + " does not exist");
        }

        Workflow activeWorkflow= workflowRepository.getActiveWorkflow(workflow.getApplicationName());

        if (activeWorkflow!=null && workflow != activeWorkflow){
            activeWorkflow.setActive(false);
            workflowRepository.save(activeWorkflow);
        }

        workflow.setActive(true);
        workflowRepository.save(workflow);
    }
}
