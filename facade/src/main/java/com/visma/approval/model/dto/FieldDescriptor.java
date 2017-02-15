package com.visma.approval.model.dto;

/**
 * Created by robert on 08.02.2017.
 */
public class FieldDescriptor {
    public String name;
    public String type;

    public FieldDescriptor(String name,String type) {
        this.name = name;
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldDescriptor that = (FieldDescriptor) o;

        if (!name.equals(that.name)) return false;
        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
