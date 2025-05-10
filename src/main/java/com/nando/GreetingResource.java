package com.nando;

import com.nando.selenium.pages.AccountListPage;
import com.nando.selenium.pages.LoginPage;
import com.nando.service.S3Service;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;

@Path("/hello")
public class GreetingResource {

    private LoginPage loginPage;

    private AccountListPage accountListPage;

    private String pathDownload;

    private S3Service s3Service;

    public GreetingResource(LoginPage loginPage, AccountListPage accountListPage, @ConfigProperty(name = "browser" +
            ".download.path") String pathDownload, S3Service s3Service) {
        this.loginPage = loginPage;
        this.accountListPage = accountListPage;
        this.pathDownload = pathDownload;
        this.s3Service = s3Service;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        File downloadDir = new File(pathDownload);
        downloadDir.mkdirs();
//        ChromeDriver browser = loginPage.executeLogin();
//        accountListPage.downloadAllClients(browser);

        File[] files = downloadDir.listFiles();
        if (files != null && files.length == 1) {
            File downloadFile = files[0];
            s3Service.sendFileBucket(downloadFile);
        }
        return "Hello from Quarkus REST";
    }
}
