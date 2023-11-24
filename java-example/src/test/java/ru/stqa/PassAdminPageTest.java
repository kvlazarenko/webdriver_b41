package ru.stqa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PassAdminPageTest extends TestBase {
  private WebDriver driver;
  private WebDriverWait wait;

  @BeforeEach
  public void start() {

    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  @Test
  public void PassAdminPageTest() {
    driver.navigate().to("http://localhost:8080/litecart/admin/login.php");
    driver.findElement(By.name("username")).clear();
    driver.findElement(By.name("username")).sendKeys("admin");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("admin");
    driver.findElement(By.name("remember_me")).click();
    driver.findElement(By.name("login")).click();

    List<WebElement> listvertical = driver
            .findElements(By.cssSelector("ul#box-apps-menu a[href^='http://localhost:8080/litecart/admin/?app=']"));

    for (int i = 0; i <= listvertical.size() - 1; i++) {
      List<WebElement> listverticalrefresh = driver
              .findElements(By.cssSelector("ul#box-apps-menu a[href^='http://localhost:8080/litecart/admin/?app=']"));
      listverticalrefresh.get(i).click();
      driver.findElement(By.cssSelector("td#content h1"));

      List<WebElement> listverticalinterior = driver
              .findElements(By.cssSelector("ul.docs a[href^='http://localhost:8080/litecart/admin/?app=']"));
      if (listverticalinterior.size() > 0) {
        for (int a = 1; a <= listverticalinterior.size() - 1; a++) {
          List<WebElement> listverticalinteriorrefresh = driver
                  .findElements(By.cssSelector("ul.docs a[href^='http://localhost:8080/litecart/admin/?app=']"));
          listverticalinteriorrefresh.get(a).click();
          driver.findElement(By.cssSelector("td#content h1"));
          driver.navigate().back();
        }
        driver.navigate().back();
      }
    }
  }

  @AfterEach
  public void stop() {
    driver.quit();
    driver = null;
  }
}
