package br.com.infnet.webprojectpb.selenium.pages;

import br.com.infnet.webprojectpb.selenium.core.BasePage;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class IndexPage extends BasePage {

    private final By nameInput = By.id("name");
    private final By priceInput = By.id("price");
    private final By quantityInput = By.id("quantity");
    private final By saveButton = By.id("save");
    private final By productList = By.id("productList");

    private String baseUrl;

    public IndexPage(WebDriver driver, String baseUrl) {
        super(driver);
        this.baseUrl = baseUrl;
    }

    public IndexPage open() {
        driver.get(baseUrl);
        return this;
    }

    public void setName(String name) {
        $(nameInput).clear();
        $(nameInput).sendKeys(name);
    }

    public void setPrice(String price) {
        $(priceInput).clear();
        $(priceInput).sendKeys(price);
    }

    public void setQuantity(String quantity) {
        $(quantityInput).clear();
        $(quantityInput).sendKeys(quantity);
    }

    public void clickSave() {
        click(saveButton);
    }

    public boolean containsProduct(String productName) {
        WebElement list = $(productList);
        return list.getText().contains(productName);
    }

    public void addProduct(String name, String price, String quantity) {
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        clickSave();
    }

    public void clickEdit(String productName) {
        By editButton = By.xpath("//li[.//span[contains(text(),'" + productName + "')]]//button[text()='Editar']");
        click(editButton);
    }

    public void clickDelete(String productName) {
        By deleteButton = By.xpath("//li[.//span[contains(text(),'" + productName + "')]]//button[text()='Excluir']");
        click(deleteButton);
    }

    public void updateProduct(String oldName, String newName, String newPrice, String newQuantity) {
        clickEdit(oldName);
        setName(newName);
        setPrice(newPrice);
        setQuantity(newQuantity);
        clickSave();
    }

    public void deleteProduct(String productName) {
        clickDelete(productName);
    }
}
