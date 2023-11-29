package ru.stqa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class PassAdminPageTest {
  private WebDriver driver;
  private WebDriverWait wait;
  private Properties properties;

  @BeforeEach
  public void start() throws IOException {

    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    String target = System.getProperty("target", "local");
    properties = new Properties();
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
  }

  @Test
  public void PassAdminPageTest() {
    driver.navigate().to(properties.getProperty("web.baseUrlAdmin"));
    driver.findElement(By.name("username")).clear();
    driver.findElement(By.name("username")).sendKeys(properties.getProperty("web.adminLogin"));
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys(properties.getProperty("web.adminPassword"));
    driver.findElement(By.name("remember_me")).click();
    driver.findElement(By.name("login")).click();

    List<WebElement> listvertical = driver
            .findElements(By.cssSelector("ul#box-apps-menu a[href*='/admin/?app=']"));

    for (int i = 0; i <= listvertical.size() - 1; i++) {
      List<WebElement> listverticalrefresh = driver
              .findElements(By.cssSelector("ul#box-apps-menu a[href*='/admin/?app=']"));
      listverticalrefresh.get(i).click();
      driver.findElement(By.cssSelector("td#content h1"));

      List<WebElement> listverticalinterior = driver
              .findElements(By.cssSelector("ul.docs a[href*='/admin/?app=']"));
      if (listverticalinterior.size() > 0) {
        for (int a = 1; a <= listverticalinterior.size() - 1; a++) {
          List<WebElement> listverticalinteriorrefresh = driver
                  .findElements(By.cssSelector("ul.docs a[href*='/admin/?app=']"));
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
