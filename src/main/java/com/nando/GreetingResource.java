package com.nando;

import com.nando.selenium.SeleniumService;
import com.nando.selenium.pages.LoginPage;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    private LoginPage loginPage;

    public GreetingResource(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        loginPage.executeLogin();
        return "Hello from Quarkus REST";
    }
}
