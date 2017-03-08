package com.visma.approval.facade;

import com.visma.approval.facade.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by robert on 13.02.2017.
 */
public interface DocumentRepository extends CrudRepository<Document, Long> {
    List<Document> findByProcessingApplicationName(String applicationName);

    @Query(value = "SELECT * FROM document WHERE application_name = :appName LIMIT 1", nativeQuery = true)
    Document findOneByProcessingApplicationName(@Param("appName")String appName);

}
