package com.visma.approval.model;

import com.visma.approval.facade.dto.Processing;

import java.text.ParseException;

/**
 * Created by robert on 06.02.2017.
 */
public class ProcessingProvider {

    private class ClientApplicationType {
        String applicationTypeName;
        Integer applicationMajorVersion;
        Integer applicationMinorVersion;
    }

    private class TaskEventSubscription {
        Boolean enable;
    }

    private ClientApplicationType clientApplicationType;
    private TaskEventSubscription taskEventSubscription;

    public String getApplicationFullName() throws ParseException {

        if (clientApplicationType == null) {
            throw new ParseException("Processing mandatory fields missing",0);
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

    public Processing get() throws ParseException {
        return new Processing(getApplicationFullName(),getTaskEventSubscription());
    }
}
