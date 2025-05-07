package com.nando.selenium;

import com.nando.frogking.chromedriver.ChromeDriverBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


@ApplicationScoped
public class SeleniumService {

    private boolean isHeadless;

    public SeleniumService(@ConfigProperty(name = "browser.isHeadless") boolean isHeadless) {
        this.isHeadless = isHeadless;
    }

    public ChromeDriver getBrowser() {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--window-size=1920,1080");
            if (isHeadless) {
                chromeOptions.addArguments("--headless=new");
            }
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.default_directory", "/tmp/"); // ou outro diretório no container
            prefs.put("safebrowsing.enabled", true);
            chromeOptions.setExperimentalOption("prefs", prefs);

            String chromedriverPath = new File(
                    getClass().getClassLoader().getResource("chromedriver").getFile()
            ).getAbsolutePath();
            return new ChromeDriverBuilder().build(chromeOptions, chromedriverPath);
    }

}
