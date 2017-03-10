package com.visma.approval.ruleengine;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by robert on 28.02.2017.
 */
public interface WorkflowRepository extends CrudRepository<Workflow, Long> {

    List<Workflow> findByApplicationName(String applicationName);

    @Query(value = "SELECT * FROM workflow WHERE application_name = :appName LIMIT 1", nativeQuery = true)
    Workflow findOneByApplicationName(@Param("appName")String appName);

    List<Workflow> findByApplicationNameAndActive(String applicationName, Boolean active);

    @Query(value = "SELECT * FROM workflow WHERE application_name = :appName AND active = true LIMIT 1", nativeQuery = true)
    Workflow getActiveWorkflow(@Param("appName") String appName);
}
