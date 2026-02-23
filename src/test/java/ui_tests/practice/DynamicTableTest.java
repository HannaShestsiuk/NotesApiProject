package ui_tests.practice;

import org.junit.jupiter.api.Test;
import pages.practice_pages.DynamicTablePage;
import pages.practice_pages.HomePage;
import ui_tests.BaseTest;

public class DynamicTableTest extends BaseTest {

    @Test
    void validateChromeCpuUsageTest() {
        HomePage home = new HomePage(page()).open();
        DynamicTablePage dynamicTablePage = home.goToDynamicTablePage();

        dynamicTablePage.compareCpuValues();
    }
}
