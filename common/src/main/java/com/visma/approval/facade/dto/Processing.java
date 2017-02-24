package com.visma.approval.facade.dto;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * Created by robert on 14.02.2017.
 */

@Embeddable
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Processing {
    private String applicationName;
    private Boolean eventSubscription;
}
