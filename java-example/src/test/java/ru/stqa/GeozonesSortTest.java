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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class GeozonesSortTest {
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
  public void geozonesSortTest() {
    driver.navigate().to(properties.getProperty("web.baseUrlAdmin"));
    driver.findElement(By.name("username")).clear();
    driver.findElement(By.name("username")).sendKeys("admin");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("admin");
    driver.findElement(By.name("remember_me")).click();
    driver.findElement(By.name("login")).click();
    driver.findElement(By.cssSelector("a[href*='geo_zones']")).click();

    WebElement table = driver.findElement(By.cssSelector("table.dataTable tbody"));
    List<WebElement> rows = table.findElements(By.tagName("tr"));
    for (int i = 1; i < rows.size() - 1; i++) {

      // обновление списка "tr"
      WebElement tableRefresh = driver.findElement(By.cssSelector("table.dataTable tbody"));
      List<WebElement> rowsRefresh = tableRefresh.findElements(By.tagName("tr"));

      // переход на страницу страны с геозонами
      rowsRefresh.get(i).findElements(By.tagName("td")).get(4).click();
      WebElement tableZone = driver.findElement(By.cssSelector("table.dataTable tbody"));
      List<WebElement> rowsZone = tableZone.findElements(By.tagName("tr"));
      List<String> stringCountryZone = new ArrayList<>();
      for (int f = 1; f < rowsZone.size() - 1; f++) {

        // присвоение переменной значение геоозоны
        List<WebElement> td = rowsZone.get(f).findElements(By.cssSelector("td"));
        String currentZoneName = td.get(2).findElement(By.cssSelector("[selected=selected]")).getText();
        stringCountryZone.add(currentZoneName);
      }
      // проверка геозон на порядок по алфавиту
      List<String> stringCountryZoneNew = new ArrayList<>(stringCountryZone);
      Collections.sort(stringCountryZoneNew);
      boolean sortZoneInCountry = stringCountryZoneNew.equals(stringCountryZone);
      Assertions.assertTrue(sortZoneInCountry);
      driver.navigate().back();
    }
  }

  @AfterEach

  public void stop() {
    driver.quit();
    driver = null;
  }
}
