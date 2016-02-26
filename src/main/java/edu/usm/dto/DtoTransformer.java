package edu.usm.dto;

import edu.usm.domain.Grant;
import edu.usm.domain.InteractionRecord;
import edu.usm.domain.SustainerPeriod;

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

    public static void fromDto(InteractionRecordDto dto, InteractionRecord record) {
        record.setPersonContacted(dto.getPersonContacted());
        record.setDateOfInteraction(dto.getDateOfInteraction());
        record.setInteractionType(dto.getInteractionType());
        record.setNotes(dto.getNotes());
        record.setRequiresFollowUp(dto.isRequiresFollowUp());
    }

    public static InteractionRecordDto fromEntity(InteractionRecord record) {
        InteractionRecordDto dto = new InteractionRecordDto();
        dto.setPersonContacted(record.getPersonContacted());
        dto.setDateOfInteraction(record.getDateOfInteraction());
        dto.setInteractionType(record.getInteractionType());
        dto.setNotes(record.getNotes());
        dto.setRequiresFollowUp(record.isRequiresFollowUp());

        if (null != record.getFoundation()) {
            dto.setFoundationId(record.getFoundation().getId());
        }
        return dto;
    }

    public static SustainerPeriodDto fromEntity(SustainerPeriod sustainerPeriod) {
        SustainerPeriodDto dto = new SustainerPeriodDto();
        dto.setMonthlyAmount(sustainerPeriod.getMonthlyAmount());
        dto.setPeriodStartDate(sustainerPeriod.getPeriodStartDate());
        dto.setCancelDate(sustainerPeriod.getCancelDate());
        dto.setSentIRSLetter(sustainerPeriod.isSentIRSLetter());
        return dto;
    }

    public static void fromDto(SustainerPeriodDto dto, SustainerPeriod sustainerPeriod) {
        sustainerPeriod.setMonthlyAmount(dto.getMonthlyAmount());
        sustainerPeriod.setSentIRSLetter(dto.isSentIRSLetter());
        sustainerPeriod.setCancelDate(dto.getCancelDate());
        sustainerPeriod.setPeriodStartDate(dto.getPeriodStartDate());
    }

}
