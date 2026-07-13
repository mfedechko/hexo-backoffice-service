package com.gpn.leads.service;

import com.gpn.leads.exception.LeadNotFoundException;
import com.gpn.leads.mapper.LeadMapper;
import com.gpn.leads.model.LeadEntity;
import com.gpn.leads.model.LeadStatus;
import com.gpn.leads.model.dto.CreateLeadRequest;
import com.gpn.leads.model.dto.LeadResponseDto;
import com.gpn.leads.repository.LeadRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;

    @Transactional
    public LeadResponseDto createNewLead(final CreateLeadRequest request) {
        final var lead = new LeadEntity();
        lead.setName(request.getName());
        lead.setPhone(request.getPhone());
        lead.setComment(request.getComment());
        lead.setStatus(LeadStatus.CREATED);
        final var saved = leadRepository.save(lead);
        return LeadMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public LeadResponseDto getLead(final Long id) {
        final var lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found"));
        return LeadMapper.toDto(lead);
    }

    @Transactional(readOnly = true)
    public List<LeadResponseDto> getAllLeads() {
        return leadRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(LeadMapper::toDto)
                .toList();
    }

    @Transactional
    public LeadResponseDto updateStatus(final Long id, final LeadStatus newStatus) {
        final var lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead not found"));
        lead.setStatus(newStatus);
        leadRepository.save(lead);
        return LeadMapper.toDto(lead);
    }
}
