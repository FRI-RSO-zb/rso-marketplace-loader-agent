package net.bobnar.marketplace.loaderAgent.loaderModules.doberAvto;

import net.bobnar.marketplace.loaderAgent.TestBase;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.doberAvto.DoberAvtoProcessor;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DoberAvtoProcessorTest extends TestBase {

//    @Test
//    void processItem() {
//    }

    @Test
    void processItemList() throws IOException {
        String data = this.loadTestResource("samples/doberavto-latest.json");

        ProcessListResult<DoberAvtoListItem> result = new DoberAvtoProcessor().processItemList(data);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isSuccess()),
                () -> assertNull(result.errors),
                () -> assertEquals("ok", result.status),
                () -> assertEquals(62, result.processedItems.size()),
                () -> assertEquals(0, result.failedItems.size())
            );
    }

    @Test
    void processListItem() throws IOException {
        String data = this.loadTestResource("samples/doberavto-latest-single-item.json");

        ProcessItemResult<DoberAvtoListItem> result = new DoberAvtoProcessor().processListItem(data);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isSuccess()),
                () -> assertNull(result.errors),
                () -> assertEquals("ok", result.status),

                () -> assertEquals("N7LFRB9M", result.item.getId()),
                () -> assertEquals("Audi", result.item.getBrand()),
                () -> assertEquals("A1 1.4 TFSI Aut. S-tronic S-Line 122KM", result.item.getTitle()),
                () -> assertEquals(157000, result.item.getDrivenDistanceKm()),
                () -> assertEquals(90, result.item.getEnginePowerKW()),
                () -> assertEquals(1390, result.item.getEngineDisplacementCcm()),
                () -> assertEquals("2012-03-29", result.item.getFirstRegistrationDate()),
                () -> assertEquals(2, result.item.getTotalOwners()),
                () -> assertEquals("Bencin", result.item.getEngineType()),
                () -> assertEquals(9590, result.item.getPriceEur()),
                () -> assertEquals("Avtomatski menjalnik", result.item.getTransmissionType()),
                () -> assertEquals("https://img.doberavto.si/postings/f8b208e8-c98e-4bbd-bff1-d62a76d043d5", result.item.getPhotoUrl()),
                () -> assertEquals("Rabljeno", result.item.getAge())
            );
    }
}
