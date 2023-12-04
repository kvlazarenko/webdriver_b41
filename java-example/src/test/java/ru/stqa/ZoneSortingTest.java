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
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ZoneSortingTest {
  private WebDriver driver;
  private WebDriverWait wait;
  private Properties properties;
  int countryColNum = 4;
  int countryColZoneNum = 5;
  int zoneColNum = 2;
  String countryName = "";
  String currentZoneName = "";
  List<String> stringCountryWithZone = new ArrayList<>();


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
  public void ZoneSortingTest() {
    driver.navigate().to(properties.getProperty("web.baseUrlAdmin"));
    driver.findElement(By.name("username")).clear();
    driver.findElement(By.name("username")).sendKeys("admin");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("admin");
    driver.findElement(By.name("remember_me")).click();
    driver.findElement(By.name("login")).click();
    driver.findElement(By.cssSelector("a[href*='countries']")).click();

//    List<WebElement> webcountry = driver
//            .findElements(By.cssSelector(".dataTable a[href*='_country&country_code=']"));
//    List<String> stringcountry = new ArrayList<>();
//    for (WebElement e : webcountry) {
//      boolean a = e.getText().isEmpty();
//      if (a == false) {
//        stringcountry.add(e.getText());
//      }
//    }
//    List<String> stringcountrynew = new ArrayList<>(stringcountry);
//    Collections.sort(stringcountrynew);
//    boolean sort = stringcountrynew.equals(stringcountry);

//    driver.get("http://localhost:8080/litecart/admin/?app=countries&doc=edit_country&country_code=CA");

//    initTable();
    WebElement table = driver.findElement(By.cssSelector("table.dataTable tbody"));
    List<WebElement> rows = table.findElements(By.tagName("tr"));

    List<String> stringcountry = new ArrayList<>();
    for (int i = 1; i < rows.size() - 1; i++) {
      countryName = rows.get(i).findElements(By.tagName("td")).get(countryColNum).getText();
      stringcountry.add(countryName);
      String countryZoneTextNum = rows.get(i).findElements(By.tagName("td")).get(countryColZoneNum).getText();
      Integer zoneNum = Integer.parseInt(countryZoneTextNum);

      //если кол-во зон у страны больше 0, то собираем наименование стран в список
      if (zoneNum > 0) {
        stringCountryWithZone.add(countryName);
      }
    }
    List<String> stringCountryNew = new ArrayList<>(stringcountry);
    Collections.sort(stringCountryNew);
    boolean sortzone = stringCountryNew.equals(stringcountry);
    Assertions.assertTrue(sortzone);


    for (int j = 1; j > stringCountryWithZone.size(); j++) {
      driver.findElement(By.linkText(stringCountryWithZone.get(j))).click();
      WebElement tableZone = driver.findElement(By.cssSelector("table.dataTable tbody"));
      List<WebElement> rowsZone = tableZone.findElements(By.tagName("tr"));
      List<String> stringCountryZone = new ArrayList<>();
      for (int f = 1; j < rowsZone.size() - 1; f++) {
        currentZoneName = rowsZone.get(f).findElements(By.tagName("td")).get(zoneColNum).getText();
        stringCountryZone.add(currentZoneName);
      }
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

