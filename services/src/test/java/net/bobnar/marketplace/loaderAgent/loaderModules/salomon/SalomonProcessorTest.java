package net.bobnar.marketplace.loaderAgent.loaderModules.salomon;

import net.bobnar.marketplace.loaderAgent.TestBase;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonListItem;
import net.bobnar.marketplace.loaderAgent.services.loaderModules.salomon.SalomonProcessor;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.services.processor.ProcessListResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalomonProcessorTest extends TestBase {

    @Test
    void processItemList() throws Exception {
        String data = this.loadTestResource("samples/salomon-avtomobili.html");

        ProcessListResult<SalomonListItem> result = new SalomonProcessor().processItemList(data);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isSuccess()),
                () -> assertNull(result.errors),
                () -> assertEquals(25, result.processedItems.size()),
                () -> assertEquals(0, result.failedItems.size())
        );
    }

    @Test
    void processListItem() throws Exception {
        String data = this.loadTestResource("samples/salomon-avtomobili-row.html");

        ProcessItemResult<SalomonListItem> result = new SalomonProcessor().processListItem(data);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isSuccess()),
                () -> assertNotNull(result.item)
        );

        assertAll(
                () -> assertEquals("/oglas/motorna-vozila/avtomobili/opel-frontera-22-dth-16v-suv/22.LJEAZ", result.item.url),
                () -> assertEquals("22.LJEAZ", result.item.id),
                () -> assertEquals("http://mm3.salomon.si/advertMm/2022/09/922/11431922/thumb_220915-165300-0e01845.jpg", result.item.photoUrl),
                () -> assertEquals("Opel Frontera 2.2 DTH 16V SUV", result.item.title),
                () -> assertEquals("5.200,00 €", result.item.price),
                () -> assertEquals("Opel, Frontera, 2.2 DTH 16V SUV, terensko vozilo, ročni 5-stopenjski, diesel, 2003, 105000, 01#2003, rabljeno, 88, 2171, - CENA J...", result.item.description),
                () -> assertEquals("Prodam", result.item.adType)
        );
    }
}
