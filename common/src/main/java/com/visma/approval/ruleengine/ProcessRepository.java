package com.visma.approval.ruleengine;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by robert on 13.02.2017.
 */
public interface ProcessRepository extends CrudRepository<Process, Long> {
    Process findFirstByDocumentId(Long documentId);
}
