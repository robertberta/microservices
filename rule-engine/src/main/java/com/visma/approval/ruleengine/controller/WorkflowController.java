package com.visma.approval.ruleengine.controller;

import com.google.gson.Gson;
import com.visma.approval.ruleengine.Workflow;
import com.visma.approval.ruleengine.WorkflowRepository;
import com.visma.approval.ruleengine.model.WorkflowHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
            Workflow workflow = gson.fromJson(bodyContent,Workflow.class);

            workflowHandler.save(workflow);

            result = new ResponseEntity<String>("workflow saved", HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }

    @RequestMapping(value = "/workflow/activate", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> activateWorkflow(@RequestBody Long bodyContent) {
        ResponseEntity<String> result;
        try {
            Long workflowId = bodyContent;

            result = new ResponseEntity<String>("workflow activated", HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }

}
