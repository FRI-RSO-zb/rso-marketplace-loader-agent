package net.bobnar.marketplace.common.dtos.catalog.v1.sellers;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Item containing seller information", example = """
        {
            "id": 1234,
            "name": "Seller 1",
            "location": "Ljubljana",
            "contact": "info@example.com"
        }
        """)
public record Seller(
        Integer id,
        String name,
        String location,
        String contact) {}
