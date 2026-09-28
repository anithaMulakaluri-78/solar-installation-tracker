package com.solartracker.mapper;

import com.solartracker.dto.response.InstallationResponse;
import com.solartracker.dto.response.UserSummaryResponse;
import com.solartracker.entity.Installation;
import com.solartracker.entity.InstallationPanel;
import com.solartracker.entity.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class InstallationMapper {

    public InstallationResponse toResponse(Installation i) {
        List<String> panelSerials = i.getPanels().stream()
                .sorted(Comparator.comparing(InstallationPanel::getPanelPosition, Comparator.nullsLast(Integer::compareTo)))
                .map(InstallationPanel::getPanelSerialNumber)
                .toList();

        return InstallationResponse.builder()
                .id(i.getId())
                .consumerId(i.getConsumer() == null ? null : i.getConsumer().getId())
                .consumerNumber(i.getConsumer() == null ? null : i.getConsumer().getConsumerNumber())
                .ulaApplicationId(i.getUlaApplicationId())
                .section(i.getSection())
                .distribution(i.getDistribution())
                .serviceNumber(i.getServiceNumber())
                .consumerName(i.getConsumerName())
                .consumerMobile(i.getConsumerMobile())
                .newMobile(i.getNewMobile())
                .inverterSerialNumber(i.getInverterSerialNumber())
                .siteType(i.getSiteType())
                .remarks(i.getRemarks())
                .dcrStatus(i.getDcrStatus())
                .status(i.getStatus())
                .assignedInstaller(toUserSummary(i.getAssignedInstaller()))
                .panelSerialNumbers(panelSerials)
                .createdAt(i.getCreatedAt())
                .build();
    }

    private UserSummaryResponse toUserSummary(User user) {
        if (user == null) {
            return null;
        }
        return UserSummaryResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }
}
