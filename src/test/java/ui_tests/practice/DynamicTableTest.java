package ui_tests.practice;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice_app.pages.DynamicTablePage;
import practice_app.pages.HomePage;
import ui_tests.BaseTest;

public class DynamicTableTest extends BaseTest {

    @DisplayName("[UI]. Dynamic Table page. Compare Chrome CPU load with value in the yellow label.")
    @Description("""
    1. Open https://practice.expandtesting.com/.
    2. Open 'Dynamic Table' page.
    3. Compare Chrome CPU load value from the table with value in the yellow label.
    """)
    @Test
    void validateChromeCpuUsageTest() {
        HomePage home = new HomePage(page()).open();
        DynamicTablePage dynamicTablePage = home.goToDynamicTablePage();

        dynamicTablePage.compareCpuValues();
    }
}
