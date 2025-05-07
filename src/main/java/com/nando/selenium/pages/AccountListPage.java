package com.nando.selenium.pages;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@ApplicationScoped
public class AccountListPage {

    private String panelUrl;

    private WebDriverWait webDriverWait;

    public AccountListPage(@ConfigProperty(name = "panel.url") String panelUrl) {
        this.panelUrl = panelUrl;

    }

    public void downloadAllClients(ChromeDriver browser) {
        browser.get(panelUrl + "/account/list");
        webDriverWait = new WebDriverWait(browser, Duration.ofSeconds(5));

        WebElement btnAllRecords = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//*[@id=\"app\"]/section/section/section/div[3]/div[2]/div[3]/div[2]/div/div/div/div[1]/div/div/div/div[3]/span[2]/span")));
        Actions actions = new Actions(browser);
        actions.moveToElement(btnAllRecords).perform();

        WebElement opcaoDownload = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"app\"]/section/section/section/div[3]/div[2]/div[3]/div[3]/div/div/div/ul/li[1]")
        ));
        opcaoDownload.click();
    }

}
