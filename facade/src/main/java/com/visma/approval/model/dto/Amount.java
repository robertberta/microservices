package com.visma.approval.model.dto;

import javax.persistence.Embeddable;

/**
 * Created by robert on 10.02.2017.
 */
public class Amount {
    public Double amount;
    public String isoCode;

    public Amount(Double amount, String isoCode){
        this.isoCode = isoCode;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Amount amount1 = (Amount) o;

        if (amount != null ? !amount.equals(amount1.amount) : amount1.amount != null) return false;
        return isoCode != null ? isoCode.equals(amount1.isoCode) : amount1.isoCode == null;

    }

    @Override
    public int hashCode() {
        int result = isoCode != null ? isoCode.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

}
