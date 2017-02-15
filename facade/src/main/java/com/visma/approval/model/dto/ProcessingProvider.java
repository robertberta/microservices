package com.visma.approval.model.dto;

import com.visma.approval.controller.exceptions.ParserException;
import com.visma.approval.model.dto.Processing;
import org.springframework.expression.ParseException;

import javax.persistence.*;
import java.beans.Transient;

/**
 * Created by robert on 06.02.2017.
 */
public class ProcessingProvider {
    public Integer id;

    public class ClientApplicationType {
        String applicationTypeName;
        Integer applicationMajorVersion;
        Integer applicationMinorVersion;
    }

    public class TaskEventSubscription {
        Boolean enable;
    }

    private ClientApplicationType clientApplicationType;
    private TaskEventSubscription taskEventSubscription;

    public String getApplicationFullName() throws ParserException{

        if (clientApplicationType == null) {
            throw new ParserException("Processing mandatory fields missing");
        }

        String name = clientApplicationType.applicationTypeName;
        Integer majorVersion = clientApplicationType.applicationMajorVersion;
        Integer minorVersion = clientApplicationType.applicationMinorVersion;
        return name + " " + majorVersion + "." + minorVersion;
    }

    public Boolean getTaskEventSubscription() {
        if (taskEventSubscription == null) {
            return false;
        }
        return taskEventSubscription.enable;
    }

    public Processing get() throws ParserException {
        Processing processing = new Processing();
        processing.eventSubscription = getTaskEventSubscription();
        processing.applicationName = getApplicationFullName();
        return processing;
    }
}
