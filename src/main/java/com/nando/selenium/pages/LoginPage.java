package com.nando.selenium.pages;

import com.nando.exception.ExtractCodeException;
import com.nando.exception.LogginErrorException;
import com.nando.selenium.SeleniumService;
import com.nando.service.EmailService;
import com.nando.service.OcrService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class LoginPage {
    private SeleniumService seleniumService;

    private String panelUrl;

    private String login;

    private String password;

    private OcrService ocrService;

    private WebDriverWait webDriverWait;

    private ChromeDriver browser;

    private EmailService emailService;

    public LoginPage(SeleniumService seleniumService,
                     @ConfigProperty(name = "panel.url") String panelUrl,
                     @ConfigProperty(name = "panel.login") String login,
                     @ConfigProperty(name = "panel.password") String password,
                     OcrService ocrService,
                     EmailService emailService) {
        this.seleniumService = seleniumService;
        this.panelUrl = panelUrl;
        this.login = login;
        this.password = password;
        this.ocrService = ocrService;
        this.emailService = emailService;
    }

    public ChromeDriver executeLogin() {
        browser = seleniumService.getBrowser();
        setWait(browser);
        browser.get(panelUrl + "/login");

        putLogin();
        putPassword();
        putCaptcha();
        clickBtnLogin();
        if (popupCodeEmailIsPresent()) {
            fillCodeFromEmail();
            clickConfirm();
        }
        verifyIsLoggedOrException();
        return browser;
    }

    private void verifyIsLoggedOrException() {
        try {
            WebElement menuAccount = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//*[@id=\"app\"]/section/section/section/div[2]/header/div[2]/span[2]/span/span"
            )));
        } catch (Exception e) {
            throw new LogginErrorException(e);
        }
    }

    private void clickConfirm() {
        WebElement btnConfirm = browser.findElement(By.xpath("//*[@id=\"htmlRoot\"]/body/div[2]/div/div[2]/div" +
                    "/div[2]/div[2]/div/div[1]/div/div/form/div[3]/div/div/div/button[1]"));
        btnConfirm.click();
    }

    private void fillCodeFromEmail() {
        WebElement btnEmail = browser.findElement(By.xpath("//*[@id=\"htmlRoot\"]/body/div[2]/div/div[2]/div/div" +
                "[2]/div[2]/div/div[1]/div/div/div[2]/label[2]"));
        btnEmail.click();

        WebElement btnSendCodeEmail = browser.findElement(By.xpath("//*[@id=\"htmlRoot\"]/body/div[2]/div/div[2" +
                "]/div/div[2]/div[2]/div/div[1]/div/div/form/div[2]/div/div/div/span/button"));
        btnSendCodeEmail.click();

        WebElement inputTextCode = browser.findElement(By.id("form_item_verCode"));
        String code = extractCode();
        inputTextCode.sendKeys(code);
    }

    private String extractCode() {
        String bodyEmail = emailService.getBodyLastEmail();
        Pattern pattern = Pattern.compile("<div[^>]*>\\s*(\\d{4})\\s*</div>");
        Matcher matcher = pattern.matcher(bodyEmail);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new ExtractCodeException();
        }
    }

    private boolean popupCodeEmailIsPresent() {
        try {
            WebElement titleEmailCode = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//*[@id" +
                    "=\"vcDialogTitle0" +
                     "\"]/span")));

            return Objects.equals(titleEmailCode.getText(), "Identity authentication");
        } catch (Exception ex) {
            return false;
        }
    }

    private void clickBtnLogin() {
        WebElement btnLogin = browser.findElement(By.xpath("//*[@id=\"app\"]/div/div[2]/div/div[2]/div/div/form/div[5" +
                "]/div/div/div" +
                "/button"));
        btnLogin.click();
    }

    private void putCaptcha() {
        WebElement captcha =
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/div" +
                        "[2]/div" +
                        "/div[2]/div/div/form/div[3]/div/div/div/img")));
        String imageBase64 = captcha.getAttribute("src").split(",")[1];
        String captchaText = ocrService.toText(imageBase64);
        WebElement inputTextCaptcha = browser.findElement(By.id("form_item_validateCode"));
        inputTextCaptcha.sendKeys(captchaText);
    }

    private void putPassword() {
        WebElement inputTextPassword =
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/div" +
                        "[2]/div" +
                        "/div[2]/div/div/form/div[2]/div/div/div/span/input")));
        inputTextPassword.sendKeys(password);
    }

    private void putLogin() {
        WebElement inputTextLogin = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(
                "form_item_account"
        )));
        inputTextLogin.sendKeys(login);
    }

    private void setWait(ChromeDriver browser) {
        webDriverWait = new WebDriverWait(browser, Duration.ofSeconds(5));
    }

}
