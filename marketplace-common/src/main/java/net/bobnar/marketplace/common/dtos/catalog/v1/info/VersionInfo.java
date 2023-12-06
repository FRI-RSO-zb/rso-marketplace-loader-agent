package net.bobnar.marketplace.common.dtos.catalog.v1.info;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Version information about the service instance.",
        example = """
                {
                  "version": "1.2.3-main-abcdef1",
                  "major": "1",
                  "minor": "2",
                  "patch": "3",
                  "branchName": "main",
                  "commitId": "abcdef1234567890abcdef1234567890abcdef12s",
                  "shortCommitId": "abcdef1",
                  "isDirty": false,
                  "buildTime": "2023-12-05T12:00:00+0100"
                }""")
public record VersionInfo(
        String version,
        String major,
        String minor,
        String patch,
        String branchName,
        String commitId,
        String shortCommitId,
        boolean isDirty,
        String buildTime) {
}
