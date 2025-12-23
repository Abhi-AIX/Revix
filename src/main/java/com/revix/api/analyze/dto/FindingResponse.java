package com.revix.api.analyze.dto;

import java.util.List;

public record FindingResponse( String category,
                               String severity,
                               String message,
                               String suggestion,
                               String filePath,
                               Integer lineStart,
                               Integer lineEnd) {

}
