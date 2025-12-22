CREATE TABLE analysis_job (
    id UUID PRIMARY KEY,
    source VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    language VARCHAR(32) NOT NULL,

    summary TEXT,
    error_message TEXT,

    created_at TIMESTAMP NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP
);

CREATE TABLE analysis_finding (
    id UUID PRIMARY KEY,
    job_id UUID NOT NULL REFERENCES analysis_job(id) ON DELETE CASCADE,

    category VARCHAR(64) NOT NULL,
    severity VARCHAR(32) NOT NULL,

    message TEXT NOT NULL,
    suggestion TEXT,
    confidence NUMERIC(3,2),

    file_path VARCHAR(512),
    line_start INT,
    line_end INT
);
