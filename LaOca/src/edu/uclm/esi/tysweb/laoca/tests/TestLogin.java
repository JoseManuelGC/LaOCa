package edu.uclm.esi.tysweb.laoca.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestLogin {
  private WebDriver driverRoja, driverAzul;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.chrome.driver", "/Applications/Selenium/chromedriver");
    driverRoja = new ChromeDriver();
    driverAzul= new ChromeDriver();
    baseUrl = "http://localhost:8080/";
    driverRoja.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driverAzul.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Test
  public void testCreacionDePartida() throws Exception {
    driverRoja.get(baseUrl + "/LaOca/");
    Thread.sleep(5000);
    new Select(driverRoja.findElement(By.id("selectBroker"))).selectByVisibleText("BrokerConPool");
    driverRoja.findElement(By.cssSelector("option[value=\"BrokerConPool\"]")).click();
    driverRoja.findElement(By.id("nombre")).clear();
    driverRoja.findElement(By.id("nombre")).sendKeys("pepe1@pepe.com");
    driverRoja.findElement(By.id("numero")).clear();
    driverRoja.findElement(By.id("numero")).sendKeys("2");
    driverRoja.findElement(By.id("btnCrearPartida")).click();
    Thread.sleep(1000); 
    
    driverAzul.get(baseUrl + "/LaOca/");
    Thread.sleep(3000);
    new Select(driverAzul.findElement(By.id("selectBroker"))).selectByVisibleText("BrokerConPool");
    driverAzul.findElement(By.cssSelector("option[value=\"BrokerConPool\"]")).click();
    driverAzul.findElement(By.id("nombre")).clear();
    driverAzul.findElement(By.id("nombre")).sendKeys("pepe2@pepe.com");
    driverAzul.findElement(By.id("btnUnirseAPartida")).click();
    Thread.sleep(1000); 
    
    
  }

  @Test
  public void testLogin() throws Exception {
    driverRoja.get(baseUrl + "/LaOca/");
    new Select(driverRoja.findElement(By.id("selectBroker"))).selectByVisibleText("BrokerConPool");
    driverRoja.findElement(By.cssSelector("option[value=\"BrokerConPool\"]")).click();
    driverRoja.findElement(By.id("nombre")).clear();
    driverRoja.findElement(By.id("nombre")).sendKeys("pepe");
    driverRoja.findElement(By.id("numero")).clear();
    driverRoja.findElement(By.id("numero")).sendKeys("2");
    driverRoja.findElement(By.id("btnCrearPartida")).click();
    Thread.sleep(1000);
    try {
    		String textoMostrado=driverRoja.findElement(By.id("divMensajes")).getText();
      assertEquals("Error: Usuario no registrado", textoMostrado);
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driverRoja.findElement(By.id("nombre")).clear();
    driverRoja.findElement(By.id("nombre")).sendKeys("pepe1@pepe.com");
    driverRoja.findElement(By.cssSelector("button")).click();
    Thread.sleep(2000);
  }

  @After
  public void tearDown() throws Exception {
    driverRoja.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driverRoja.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driverRoja.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driverRoja.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
