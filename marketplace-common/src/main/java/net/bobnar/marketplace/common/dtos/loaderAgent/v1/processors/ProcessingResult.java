package net.bobnar.marketplace.common.dtos.loaderAgent.v1.processors;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Result of processing operation.")
public record ProcessingResult(
        Boolean isSuccessful,
        String errors,
        Object result
) {}
