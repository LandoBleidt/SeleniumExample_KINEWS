package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DemoApplication {
    public static void main(String[] args) {

        String url = (args.length > 0) ? args[0] : System.getenv("KI_NEWS_URL");
        if (url == null || url.isEmpty()) {
            System.err.println("Error: No URL provided. Please provide a URL as a command-line argument or set the KI_NEWS_URL environment variable.");
            System.exit(1);
        }
        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("KI_NEWS_HEADLESS", "false"));
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = null;
        try {
            driver = new ChromeDriver(options);
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("body")));
            System.out.println("Seite geladen. Titel:" + driver.getTitle());
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Seite: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}