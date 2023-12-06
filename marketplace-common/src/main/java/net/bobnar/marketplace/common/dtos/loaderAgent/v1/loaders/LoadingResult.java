package net.bobnar.marketplace.common.dtos.loaderAgent.v1.loaders;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Result of loading operation.")
public record LoadingResult(
        Boolean isSuccessful,
        String content
) {}
