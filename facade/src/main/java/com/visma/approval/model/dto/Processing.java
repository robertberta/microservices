package com.visma.approval.model.dto;

import javax.persistence.Embeddable;

/**
 * Created by robert on 14.02.2017.
 */
@Embeddable
public class Processing {
    public String applicationName;
    public Boolean eventSubscription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Processing that = (Processing) o;

        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        return eventSubscription != null ? eventSubscription.equals(that.eventSubscription) : that.eventSubscription == null;

    }

    @Override
    public int hashCode() {
        int result = applicationName != null ? applicationName.hashCode() : 0;
        result = 31 * result + (eventSubscription != null ? eventSubscription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Processing{" +
                "applicationName='" + applicationName + '\'' +
                ", eventSubscription=" + eventSubscription +
                '}';
    }
}
