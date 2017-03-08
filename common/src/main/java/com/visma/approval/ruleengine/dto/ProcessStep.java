package com.visma.approval.ruleengine.dto;

import com.visma.approval.ruleengine.Process;
import lombok.*;

import javax.persistence.*;

/**
 * Created by robert on 23.02.2017.
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ProcessStep {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = null;
  @Setter
  @ManyToOne
  private Process process;
  private Integer stage;
  private Integer step;
  @Setter
  private ApproverAction action;

}
