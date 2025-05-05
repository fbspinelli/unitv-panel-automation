package com.nando.selenium.pages;

import com.nando.selenium.SeleniumService;
import com.nando.service.OcrService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@ApplicationScoped
public class LoginPage {
    private SeleniumService seleniumService;

    private String panelUrl;

    private String login;

    private String password;

    private OcrService ocrService;

    public LoginPage(SeleniumService seleniumService,
                     @ConfigProperty(name = "panel.url") String panelUrl,
                     @ConfigProperty(name = "panel.login") String login,
                     @ConfigProperty(name = "panel.password") String password,
                     OcrService ocrService) {
        this.seleniumService = seleniumService;
        this.panelUrl = panelUrl;
        this.login = login;
        this.password = password;
        this.ocrService = ocrService;
    }

    public void executeLogin() {
        ChromeDriver browser = seleniumService.getBrowser();
        browser.get(panelUrl);

        WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(5));

        WebElement inputTextLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("form_item_account"
        )));
        inputTextLogin.sendKeys(login);

        WebElement inputTextPassword =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/div[2]/div" +
                        "/div[2]/div/div/form/div[2]/div/div/div/span/input")));
        inputTextPassword.sendKeys(password);

        WebElement captcha =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/div[2]/div" +
                        "/div[2]/div/div/form/div[3]/div/div/div/img")));
        String imageBase64 = captcha.getAttribute("src").split(",")[1];
        String captchaText = ocrService.toText(imageBase64);

        WebElement inputTextCaptcha = browser.findElement(By.id("form_item_validateCode"));
        inputTextCaptcha.sendKeys(captchaText.replaceAll("\\s+", ""));

        List<WebElement> elements = browser.findElements(By.xpath("//*[@id=\"htmlRoot\"]/body/div[2]/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div[2]/label[2]"));
        if (!elements.isEmpty()) {
            elements.get(0).click();
            WebElement btnSendCodeEmail = browser.findElement(By.xpath("//*[@id=\"htmlRoot\"]/body/div[2]/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div/form/div[2]/div/div/div/span/button"));
            btnSendCodeEmail.click();

        }

    }

}
