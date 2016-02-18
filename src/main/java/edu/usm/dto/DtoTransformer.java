package edu.usm.dto;

import edu.usm.domain.Grant;

/**
 * Created by andrew on 2/18/16.
 */
public class DtoTransformer {

    public static void fromDto(GrantDto dto, Grant grant) {
        grant.setName(dto.getName());
        grant.setAmountAppliedFor(dto.getAmountAppliedFor());
        grant.setAmountReceived(dto.getAmountReceived());
        grant.setStartPeriod(dto.getStartPeriod());
        grant.setEndPeriod(dto.getEndPeriod());
        grant.setApplicationDeadline(dto.getApplicationDeadline());
        grant.setIntentDeadline(dto.getIntentDeadline());
        grant.setReportDeadline(dto.getReportDeadline());
        grant.setDescription(dto.getDescription());
        grant.setRestriction(dto.getRestriction());
    }

    public static GrantDto fromEntity(Grant grant) {
        GrantDto dto = new GrantDto();
        dto.setName(grant.getName());
        dto.setAmountAppliedFor(grant.getAmountAppliedFor());
        dto.setAmountReceived(grant.getAmountReceived());
        dto.setStartPeriod(grant.getStartPeriod());
        dto.setEndPeriod(grant.getEndPeriod());
        dto.setApplicationDeadline(grant.getApplicationDeadline());
        dto.setIntentDeadline(grant.getIntentDeadline());
        dto.setReportDeadline(grant.getReportDeadline());
        dto.setDescription(grant.getDescription());
        dto.setRestriction(grant.getRestriction());
        if (null != grant.getFoundation()) {
            dto.setFoundationId(grant.getFoundation().getId());
        }
        return dto;
    }

}
