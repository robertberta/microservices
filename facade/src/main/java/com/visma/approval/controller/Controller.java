package com.visma.approval.controller;

import com.visma.approval.facade.Document;
import com.visma.approval.model.DocumentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @Autowired
    DocumentProvider documentProvider;

    @RequestMapping(value = "/document", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addDocument(@RequestBody String bodyContent) {
        ResponseEntity<String> result;
        try {
            Document document = documentProvider.parseXml(bodyContent);
            result = new ResponseEntity<>(document.toString(), HttpStatus.OK);
        } catch (Exception ex) {
            result = new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return result;
    }
}
