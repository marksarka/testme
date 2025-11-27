package com.sg.dispatch.graphql.resolver.field;

import com.sg.dispatch.graphql.loader.*;
import com.sg.dispatch.graphql.model.comment.CommentGraphQLTO;
import com.sg.dispatch.graphql.model.dispatch_sample.DispatchSampleGraphQLTO;
import com.sg.dispatch.graphql.model.dispatch_sample.DispatchSampleStatusHistoryGraphQLTO;
import com.sg.dispatch.graphql.model.quality_metric.QualityMetricGraphQLTO;
import com.sg.dispatch.graphql.model.sample_submission.SampleSubmissionGraphQLTO;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Singleton
@RequiredArgsConstructor
public class DispatchSampleFieldResolver implements GraphQLResolver<DispatchSampleGraphQLTO> {
    public CompletableFuture<SampleSubmissionGraphQLTO> sampleSubmission(DispatchSampleGraphQLTO dispatchSample, DataFetchingEnvironment env) {
        DataLoader<Long, SampleSubmissionGraphQLTO> dataLoader = env.getDataLoader(SampleSubmissionDataLoader.DATALOADER_NAME);
        return dataLoader.load(dispatchSample.getId(), env.getLocalContext());
    }

    public CompletableFuture<List<DispatchSampleStatusHistoryGraphQLTO>> statusHistories(DispatchSampleGraphQLTO  dispatchSample, DataFetchingEnvironment env) {
        DataLoader<Long, List<DispatchSampleStatusHistoryGraphQLTO>> dataLoader = env.getDataLoader(DispatchSampleStatusHistoriesDataLoader.DATALOADER_NAME);
        return dataLoader.load(dispatchSample.getId(), env.getLocalContext());
    }

    public CompletableFuture<List<QualityMetricGraphQLTO>> qualityMetrics(DispatchSampleGraphQLTO dispatchSample, DataFetchingEnvironment env) {
        DataLoader<Long, List<QualityMetricGraphQLTO>> dataLoader = env.getDataLoader(DispatchSampleQualityMetricsDataLoader.DATALOADER_NAME);
        return dataLoader.load(dispatchSample.getId(), env.getLocalContext());
    }

    public CompletableFuture<List<CommentGraphQLTO>> comments(DispatchSampleGraphQLTO dispatchSample, DataFetchingEnvironment env) {
        DataLoader<Long, List<CommentGraphQLTO>> dataLoader = env.getDataLoader(DispatchSampleCommentsDataLoader.DATALOADER_NAME);
        return dataLoader.load(dispatchSample.getId(), env.getLocalContext());
    }

    public CompletableFuture<Integer> turnAroundTime(DispatchSampleGraphQLTO dispatchSample, DataFetchingEnvironment env) {
        DataLoader<Long, Integer> dataLoader = env.getDataLoader(DispatchSampleTurnaroundTimeDataLoader.DATALOADER_NAME);
        return dataLoader.load(dispatchSample.getId(), env.getLocalContext());
    }

    public CompletableFuture<String> sequencerAgnosticPipelineName(DispatchSampleGraphQLTO dispatchSampleGraphQLTO, DataFetchingEnvironment env) {
        DataLoader<Long, String> dataLoader = env.getDataLoader(DispatchSampleSequencerAgnosticPipelineNameDataLoader.DATALOADER_NAME);
        // Using getFirst here due to the assumption that all candidatesgapipids represent the same sequencer-agnostic pipeline name
        return dataLoader.load(dispatchSampleGraphQLTO.getCandidateSgapipIds().getFirst(), env.getLocalContext());
    }
}
