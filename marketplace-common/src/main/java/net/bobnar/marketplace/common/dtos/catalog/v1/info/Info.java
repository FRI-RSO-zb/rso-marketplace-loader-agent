package net.bobnar.marketplace.common.dtos.catalog.v1.info;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Information about the service instance.", example = """
            {
              "name": "catalog",
              "environment": "prod",
              "version": "1.2.3-main-abcdef1"
            }""")
public record Info(
        @Schema(description = "Service name")
        String name,
        @Schema(description = "Environment")
        String environment,
        @Schema(description = "Instance version")
        String version
){}
