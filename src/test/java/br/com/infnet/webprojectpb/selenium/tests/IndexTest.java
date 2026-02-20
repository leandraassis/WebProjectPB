package br.com.infnet.webprojectpb.selenium.tests;

import br.com.infnet.webprojectpb.selenium.core.BaseTest;
import br.com.infnet.webprojectpb.selenium.pages.IndexPage;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //8080
public class IndexTest extends BaseTest {

    private IndexPage indexPage;
    private final String BASE_URL =  "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        indexPage = new IndexPage(driver, BASE_URL);
        indexPage.open();
    }

    @Test
    void testLoadPage() {
        Assertions.assertEquals("CRUD Products", driver.getTitle());
    }

    @Test
    public void testAddProduct() throws InterruptedException {
        String name = "Peruca";
        String price = "40";
        String quantity = "3";

        indexPage.addProduct(name, price, quantity);
        Thread.sleep(1500);

        Assertions.assertTrue(indexPage.containsProduct(name));
    }

    @Test
    public void testUpdateProduct() throws InterruptedException {
        String oldName = "Peruca";
        String newName = "Cabelo";
        String newPrice = "50";
        String newQuantity = "10";

        indexPage.addProduct(oldName, "20", "2");
        indexPage.updateProduct(oldName, newName, newPrice, newQuantity);

        Thread.sleep(1500);
        Assertions.assertTrue(indexPage.containsProduct(newName));
        Assertions.assertFalse(indexPage.containsProduct(oldName));
    }

    @Test
    public void testDeleteProduct() throws InterruptedException {
        String name = "Deleted";

        indexPage.addProduct(name, "30", "5");
        indexPage.deleteProduct(name);

        Thread.sleep(1500);
        Assertions.assertFalse(indexPage.containsProduct(name));
    }
}