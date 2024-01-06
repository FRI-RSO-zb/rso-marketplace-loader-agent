package net.bobnar.marketplace.loaderAgent.loaderModules.bolha;

import net.bobnar.marketplace.loaderAgent.TestBase;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.bolha.BolhaProcessor;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BolhaProcessorTest extends TestBase {

    @Test
    void processList() throws IOException {
        String data = this.loadTestResource("samples/bolha-avto-oglasi.html");

        ProcessListResult<BolhaListItem> result = new BolhaProcessor().processItemList(data);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("", result.errors);
        assertEquals("ok", result.status);
        assertEquals(31, result.processedItems.size()); // We actually only get 99 ads in the top100
        assertEquals(0, result.failedItems.size());
    }

//    @Test
//    void processListing() throws FileNotFoundException {
//        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-ad-details-18980973.html");
//        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(isr);
//        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
//
//        var result = new AvtoNetService().processListing(data);
//
//        assertNotNull(result);
//    }

    @Test
    void  processListItem() throws Exception {
        String data = this.loadTestResource("samples/bolha-avto-oglasi-row.html");

        ProcessItemResult<BolhaListItem> result = new BolhaProcessor().processListItem(data);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.item);
        assertFalse(result.item.isExposed);
        assertEquals("VW Arteon 2.0 TDI 110kW-MODEL 2020-ODLIČEN-MENJAM-UGODNO", result.item.title);
        assertEquals(11911746, result.item.id);
        assertEquals("https://static.bolha.com/dist/ceb7cc1a7b.png", result.item.photoPath);
        assertEquals("Rabljeno vozilo", result.item.age);
        assertEquals(1, result.item.drivenDistanceKm);
        assertEquals(2019, result.item.manufacturingYear);
        assertEquals("Sevnica, Sevnica", result.item.location);
        assertEquals("2023-11-16T19:05:17+01:00", result.item.publishDate);
        assertEquals("24.500 €", result.item.price);
    }
}
