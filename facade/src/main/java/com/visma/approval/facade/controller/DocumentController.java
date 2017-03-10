package com.visma.approval.facade.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.approval.facade.Document;
import com.visma.approval.facade.DocumentRepository;
import com.visma.approval.facade.model.DocumentProvider;
import com.visma.approval.ruleengine.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class DocumentController {
    @Autowired
    DocumentProvider documentProvider;
    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    RestTemplate ruleEngine;

    @RequestMapping(value = "/document", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addDocument(@RequestBody String bodyContent) {
        ResponseEntity<String> result;
        try {
            Document document = documentProvider.parseXml(bodyContent);
            documentRepository.save(document);

            String processStr = ruleEngine.postForObject("http://ruleengine/process",document.getId(),String.class);

            ObjectMapper mapper = new ObjectMapper();
            Process process = mapper.readValue(processStr,Process.class);

            result = new ResponseEntity<>(process.getStatus().toString(), HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }
}
