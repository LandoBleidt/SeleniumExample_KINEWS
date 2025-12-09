package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DemoApplication {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        // Optional: Headless-Modus aktivieren
        // options.addArguments("--headless");

        String url = "https://www.golem.de/";

        WebDriver driver = null;
        try {
            driver = new ChromeDriver(options);
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("body")));

            try {
                Thread.sleep(1500);
                List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                boolean clicked = false;
                for (WebElement iframe : iframes) {
                    driver.switchTo().frame(iframe);
                    try {
                        WebElement cookieButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//button[@title='Zustimmen und weiter' and @aria-label='Zustimmen und weiter']")
                        ));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cookieButton);
                        wait.until(ExpectedConditions.elementToBeClickable(cookieButton)).click();
                        System.out.println("Cookies akzeptiert.");
                        clicked = true;
                        break;
                    } catch (Exception ignored) {
                    }
                    driver.switchTo().defaultContent();
                }
                if (!clicked) {
                    driver.switchTo().defaultContent();
                    WebElement cookieButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[@title='Zustimmen und weiter' and @aria-label='Zustimmen und weiter']")
                    ));
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cookieButton);
                    wait.until(ExpectedConditions.elementToBeClickable(cookieButton)).click();
                    System.out.println("Cookies akzeptiert.");
                }
            } catch (Exception e) {
                System.out.println("Cookie-Banner nicht gefunden oder konnte nicht geklickt werden.");
            }

            System.out.println("Schließe das Chrome-Fenster, um das Programm zu beenden...");
            while (true) {
                try {
                    List<String> handles = driver.getWindowHandles().stream().toList();
                    if (handles.isEmpty()) break;
                    Thread.sleep(1000);
                } catch (NoSuchWindowException | InterruptedException e) {
                    break;
                }
            }

        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Seite: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
