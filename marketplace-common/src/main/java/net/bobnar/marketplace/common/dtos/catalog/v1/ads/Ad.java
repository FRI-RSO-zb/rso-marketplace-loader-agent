package net.bobnar.marketplace.common.dtos.catalog.v1.ads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Item containing the information about the ad.", example = """
        {
            "id": 1234,
            "title": "Ad 1",
            "source": "internal"
        }
        """)
public record Ad(
        Integer id,
        String title,
        String source,
        Integer sellerId) {}
