package com.sg.reporting.dataservice.extdependencies.onco;

import com.sg.reporting.dataservice.AuthContext;
import com.sg.reporting.dataservice.extdependencies.GraphQLResponse;
import com.sg.reporting.dataservice.extdependencies.onco.exchange.OncoDataEntity;
import com.sg.reporting.dataservice.extdependencies.onco.exchange.OncoDoIds;
import graphql.GraphQLError;
import graphql.execution.ExecutionId;

import java.util.List;
import java.util.Set;

public interface OncoSvcRetriever {

    GraphQLResponse<OncoDataEntity, OncoException> requestMolecularProfiles(Integer analysisId, List<Integer> doIds, Set<Integer> associationIds, List<String> geneList, ExecutionId executionId, AuthContext authContext, List<GraphQLError> svcErrors);

    void handleErrors(List<GraphQLError> svcErrors, GraphQLResponse<OncoDataEntity, OncoException> svcResponse);

    String requestOncoportalVersion(String correlationId);

    GraphQLResponse<OncoDataEntity, OncoException> requestClinicalTrials(Integer analysisId, Set<String> clinicalTrialInReportIds, ExecutionId executionId, AuthContext authContext, List<GraphQLError> svcErrors);

    GraphQLResponse<OncoDataEntity, OncoException> requestDetectedMolecularProfilesIds(Integer analysisId, List<String> geneList, ExecutionId executionId, AuthContext authContext, List<GraphQLError> svcErrors);

    GraphQLResponse<OncoDoIds, OncoException> requestDiseasesChildrenFromTreeByIds(List<Integer> doIds, AuthContext authContext, ExecutionId executionId, List<GraphQLError> svcErrors);
}
