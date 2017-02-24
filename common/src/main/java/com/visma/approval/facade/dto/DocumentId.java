package com.visma.approval.facade.dto;

/**
 * Created by robert on 10.02.2017.
 */
public class DocumentId {
    public String systemId;
    public String displayId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentId that = (DocumentId) o;

        if (systemId != null ? !systemId.equals(that.systemId) : that.systemId != null) return false;
        return displayId != null ? displayId.equals(that.displayId) : that.displayId == null;

    }

    @Override
    public int hashCode() {
        int result = systemId != null ? systemId.hashCode() : 0;
        result = 31 * result + (displayId != null ? displayId.hashCode() : 0);
        return result;
    }
}