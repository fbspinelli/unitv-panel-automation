package com.nando;

import com.nando.selenium.pages.AccountListPage;
import com.nando.selenium.pages.LoginPage;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.openqa.selenium.chrome.ChromeDriver;

@Path("/hello")
public class GreetingResource {

    private LoginPage loginPage;

    private AccountListPage accountListPage;

    public GreetingResource(LoginPage loginPage, AccountListPage accountListPage) {
        this.loginPage = loginPage;
        this.accountListPage = accountListPage;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        ChromeDriver browser = loginPage.executeLogin();
        accountListPage.downloadAllClients(browser);
        //salvar no S3
        return "Hello from Quarkus REST";
    }
}
