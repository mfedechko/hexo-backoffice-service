package com.gpn.leads.mapper;

import com.gpn.leads.model.LeadEntity;
import com.gpn.leads.model.dto.LeadResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LeadMapper {

    public static LeadResponseDto toDto(final LeadEntity entity) {
        final var response = new LeadResponseDto();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setPhone(entity.getPhone());
        response.setComment(entity.getComment());
        response.setStatus(entity.getStatus().name());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
