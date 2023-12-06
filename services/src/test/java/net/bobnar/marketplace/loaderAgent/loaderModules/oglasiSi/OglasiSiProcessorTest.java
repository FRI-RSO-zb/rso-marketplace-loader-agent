package net.bobnar.marketplace.loaderAgent.loaderModules.oglasiSi;

import net.bobnar.marketplace.loaderAgent.TestBase;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.oglasiSi.OglasiSiProcessor;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OglasiSiProcessorTest extends TestBase {

    @Test
    void processItemList() throws Exception {
        String data = this.loadTestResource("oglasi-si-avtomobili.html");

        ProcessListResult<OglasiSiListItem> result = new OglasiSiProcessor().processItemList(data);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isSuccess()),
                () -> assertNull(result.errors),
                () -> assertEquals(30, result.processedItems.size()),
                () -> assertEquals(0, result.failedItems.size())
        );
    }

    @Test
    void processListItem() throws Exception{
        String data = this.loadTestResource("oglasi-si-avtomobili-row.html");

        ProcessItemResult<OglasiSiListItem> result = new OglasiSiProcessor().processListItem(data);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isSuccess()),
                () -> assertNotNull(result.item)
        );

        assertAll(
                () -> assertEquals("oglas/avtomobili/kia-rio-14-tdi/1913732", result.item.url),
                () -> assertEquals("1913732", result.item.id),
                () -> assertEquals("/storage/products/1913732/thumb/8212979344720230920165701.jpg", result.item.photoUrl),
                () -> assertEquals("Kia rio 1.4 TDI", result.item.title),
                () -> assertEquals("3.990 €", result.item.price),
                () -> assertEquals("Kia rio TDI 1.4, letnik 2013, prevoženih 249000. Lepo ohranjen...", result.item.sellerNotes),
                () -> assertEquals("Maribor", result.item.location),
                () -> assertEquals("Posodobljeno 13.11.2023", result.item.lastUpdated)
        );
    }
}
