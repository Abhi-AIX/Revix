package com.revix.api.analyze;

import com.revix.api.analyze.dto.AnalyzeJobCreatedResponse;
import com.revix.api.analyze.dto.AnalyzeRequest;
import com.revix.api.analyze.dto.JobStatusResponse;
import com.revix.jobs.JobService;
import com.revix.jobs.model.AnalysisJob;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AnalyzeController {

    private final JobService jobService;

    public AnalyzeController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/analyze")
    public AnalyzeJobCreatedResponse analyze(@Valid @RequestBody AnalyzeRequest request) {
        AnalysisJob job = jobService.createJob(request.language(), request.code());
        return new AnalyzeJobCreatedResponse(job.getId(), job.getStatus().name());
    }

    @GetMapping("/analysis/{jobId}")
    public JobStatusResponse getStatus(@PathVariable String jobId) {
        AnalysisJob job = jobService.getJob(jobId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found")
                );

        return new JobStatusResponse(
                job.getId(),
                job.getStatus().name(),
                job.getSummary(),
                job.getErrorMessage()
        );
    }


}
