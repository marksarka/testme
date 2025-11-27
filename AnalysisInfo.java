package com.sg.analysis.mgmt.service.prepipeline.model.workflow;

import com.sg.analysis.mgmt.graphql.model.group.GroupRole;
import com.sg.analysis.mgmt.service.external.model.product.ExperimentTypeEnum;
import com.sg.analysis.mgmt.service.integrated.systems.info.IntegratedSystemInfo;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Serdeable
@Data
@AllArgsConstructor
public class AnalysisInfo {
    private Long id;
    private String batchMidAlt;
    private AnalysisTypeInfo analysisType;
    private Long patientId;
    private String patientUserRef;
    private ExperimentTypeEnum experimentType;
    private IntegratedSystemInfo sisInfo;
    private String fileType;
    private List<GroupRole> groupRoles;
}