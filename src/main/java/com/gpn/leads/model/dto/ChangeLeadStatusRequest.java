package com.gpn.leads.model.dto;

import com.gpn.leads.model.LeadStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeLeadStatusRequest {
    private LeadStatus newStatus;
}
