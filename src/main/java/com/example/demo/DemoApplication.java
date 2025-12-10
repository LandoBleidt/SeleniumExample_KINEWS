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

        String url = "https://www.golem.de/";  //URL definieren

        WebDriver driver = null; //WebDriver initiieren
        try {
            driver = new ChromeDriver(options); //neues ChromeDriver Objekt erstellen (in dem Fall wird nun Chrome als Browser benutzt)
            //options.addArguments("--headless"); Headless, aber du willst ja die Seite sehen also macht das keinen Sinn
            driver.get(url); //Zuvor definierte URL wird nun abgerufen

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));//Es wird ein neues WebDriverWait Objekt erstellt, was 20 Sekudnen lang auf das folgende Ereignis "wait.until" wartet.
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("body"))); //Es wird also nun so lange gewartet, bis ein Body auf der Website, die man aufrufen möchte erkennbar ist

            try {
                Thread.sleep(1500);
                List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                boolean clicked = false;
                for (WebElement iframe : iframes) {
                    driver.switchTo().frame(iframe);
                    try {
                        WebElement cookieButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//button[@title='Zustimmen und weiter' and @aria-label='Zustimmen und weiter']")

                            //TODO: wie findet man den XPath mit Chrome Dev Tools: Rechtsklick auf das Element -> Kopieren -> XPath
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
                        By.xpath("//button[@title='Zustimmen und weiter' and @aria-label='Zustimmen und weiter']") //Es wird gewartet, bis ein aria-label mit dem Titel "Zustimmen und weiter" sichtbar ist. gesucht wird per xpath
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
