package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Calculates phases of the moon for a given date in a very basic and limited way
 */
public class MoonPhaseCalculatorSelenium implements MoonPhaseCalculator {

    @Override
    public MoonPhase getMoonPhase(LocalDate date) {
        WebDriver webDriver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new"); // Runs Chrome without a visible UI
            webDriver = new ChromeDriver(options);
            // WebDriver webDriver = new ChromeDriver();
            WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            webDriver.get("https://aa.usno.navy.mil/data/RS_OneDay");

            // fill in the date input
            WebElement name = webDriver.findElement(By.cssSelector("input#date"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
            String formattedDate = date.format(formatter);
            name.sendKeys(formattedDate + Keys.TAB);

            WebElement submit = webDriver.findElement(By.cssSelector("input#submit"));
            submit.click();

            // wait for the next page
            //        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            //        WebElement waitElement = wait.until(
            //                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("nav.site-nav-secondary"))
            //        );
            Thread.sleep(1000);

            WebElement resultWebElement = webDriver.findElement(By.cssSelector("h4 + p + p"));
            String resultStr = resultWebElement.getText();


            MoonPhase[] moonPhases = MoonPhase.values();
            for (MoonPhase moonPhase : moonPhases) {
                if (resultStr.contains(moonPhase.getName())) {
                    return moonPhase;
                }
            }

        } catch (Exception e) {
            // don't care
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }

        return null;
    }
}
 