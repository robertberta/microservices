package com.visma.approval.ruleengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.visma.approval.facade.Document;
import com.visma.approval.facade.DocumentRepository;
import com.visma.approval.ruleengine.model.ProcessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.visma.approval.ruleengine.Process;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProcessController {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    ProcessHandler processHandler;

    @RequestMapping(value = "/process", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addProcess(@RequestBody String documentId) {
        ResponseEntity<String> result;
        try {
            Document document = documentRepository.findOne(Long.decode(documentId));

            Process process = processHandler.newProcess(document);
            String jsonProcess = objectMapper.writeValueAsString(process);
            result = new ResponseEntity<String>(jsonProcess, HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }
}
