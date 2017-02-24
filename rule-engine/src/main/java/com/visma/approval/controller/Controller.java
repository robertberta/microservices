package com.visma.approval.controller;

import com.google.gson.Gson;
import com.visma.approval.facade.Document;
import com.visma.approval.model.ProcessProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.visma.approval.ruleengine.Process;

@RestController
public class Controller {
    private static Gson gson = new Gson();

    @RequestMapping(value = "/process", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addProcess(@RequestBody String bodyContent) {
        ResponseEntity<String> result;
        try {
            Document document = gson.fromJson(bodyContent,Document.class);
            ProcessProvider processProvider = new ProcessProvider(document);
            Process process = processProvider.getProcess();
            result = new ResponseEntity<String>(process.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }
}
