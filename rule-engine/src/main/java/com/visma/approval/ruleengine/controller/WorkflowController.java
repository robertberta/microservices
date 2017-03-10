package com.visma.approval.ruleengine.controller;

import com.google.gson.Gson;
import com.visma.approval.ruleengine.Workflow;
import com.visma.approval.ruleengine.WorkflowRepository;
import com.visma.approval.ruleengine.controller.exceptions.WorkflowException;
import com.visma.approval.ruleengine.model.WorkflowHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by robert on 02.03.2017.
 */



@RestController
public class WorkflowController {
    private static Gson gson = new Gson();
    @Autowired
    WorkflowHandler workflowHandler;



    @RequestMapping(value = "/workflow", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addNewWorkflow(@RequestBody String bodyContent) {
        ResponseEntity<String> result;
        try {
            Workflow workflow = gson.fromJson(bodyContent, Workflow.class);

            if (workflow.getActive()!= null && workflow.getActive()){
                throw new WorkflowException("Workflow active: true is not permitted");
            }
            workflow.setActive(false);

            workflowHandler.save(workflow);

            result = new ResponseEntity<String>(workflow.getId().toString(), HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }

    @RequestMapping(value = "/workflow/activ", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> activateWorkflow(@RequestParam Long workflowId) {
        ResponseEntity<String> result;
        try {
            workflowHandler.activateWorkflow(workflowId);
            result = new ResponseEntity<String>("workflow activated", HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }

}
