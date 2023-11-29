package ru.stqa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

public class AvailabilityStickerProduct {
  private WebDriver driver;
  private WebDriverWait wait;
  private Properties properties;

  @BeforeEach
  public void start() throws IOException {
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    String target = System.getProperty("target", "local");
    properties = new Properties();
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
  }

  @Test
  public void AvailabilityStickerProductTest() {

    driver.get(properties.getProperty("web.baseUrl"));
    List<WebElement> products = driver.findElements(By.cssSelector("li.product.column.shadow.hover-light"));
    for (WebElement e : products) {
      List<WebElement> sticker = e.findElements(By.cssSelector("div.sticker"));
      boolean a = sticker.size() == 1;
      if (sticker.size() > 0) {
        Assertions.assertTrue(a);
      }
    }
  }

  @AfterEach
  public void stop() {
    driver.quit();
    driver = null;
  }

}

