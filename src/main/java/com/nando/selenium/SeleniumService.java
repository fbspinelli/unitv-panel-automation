package com.nando.selenium;

import com.nando.frogking.chromedriver.ChromeDriverBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


@ApplicationScoped
public class SeleniumService {

    private boolean isHeadless;

    private ChromeDriver chromeDriver;

    private String webdriveFile;

    public SeleniumService(@ConfigProperty(name = "browser.isHeadless") boolean isHeadless,
                           @ConfigProperty(name = "webdriver") String webdriveFile) {
        this.isHeadless = isHeadless;
        this.webdriveFile = webdriveFile;
    }

    public ChromeDriver getBrowser() {
        if (chromeDriver == null) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--window-size=1920,1080");
            if (isHeadless) {
                chromeOptions.addArguments("--headless=new");
            }
            chromeDriver = new ChromeDriverBuilder().build(chromeOptions, webdriveFile);

        }
        return chromeDriver;
    }

}
