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

public class TestPartida {
  private WebDriver driverBlue, driverOrange;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.chrome.driver", "D:\\Descargas\\Google Chrome\\chromedriver.exe");  
	driverBlue = new ChromeDriver();
    driverOrange = new ChromeDriver();
    driverBlue.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driverOrange.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testPartida() throws Exception {
    driverBlue.get("http://localhost:8080/LaOca/");
    JavascriptExecutor jsA =(JavascriptExecutor)driverBlue;
    WebElement element = driverBlue.findElement(By.id("login-form"));
    jsA.executeScript("arguments[0].style.display='none'", element);
    WebElement element2 = driverBlue.findElement(By.id("guest-form"));
    jsA.executeScript("arguments[0].style.display='block'", element2);
    Thread.sleep(1000);
    driverBlue.findElement(By.id("guest-submit")).click();
    driverBlue.findElement(By.linkText("Crear partida")).click();
    
    Thread.sleep(1000);
    driverOrange.get("http://localhost:8080/LaOca/");
    JavascriptExecutor jsB =(JavascriptExecutor)driverOrange;
    WebElement element3 = driverOrange.findElement(By.id("login-form"));
    jsB.executeScript("arguments[0].style.display='none'", element3);
    WebElement element4 = driverOrange.findElement(By.id("guest-form"));
    jsB.executeScript("arguments[0].style.display='block'", element4);
    Thread.sleep(1000);
    driverOrange.findElement(By.id("guest-submit")).click();
    Thread.sleep(1000);
    WebElement element6 = driverOrange.findElement(By.id("unirsePartida"));
    jsB.executeScript("arguments[0].style.display='block'", element6);
    driverBlue.findElement(By.id("button-crear-partida")).click();
    Thread.sleep(1000);
    driverOrange.findElement(By.id("button-unirse")).click();
    Thread.sleep(1000);
    jsA.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsA.executeScript("testingDado(3);");
    Thread.sleep(1000);
    jsA.executeScript("testingDado(3);");
    Thread.sleep(1000);
    jsA.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "15");
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverOrange.findElement(By.id("posicionOrange")).getText(), "2");
    jsA.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "19");    
    assertEquals(driverOrange.findElement(By.id("turnoTesting")).getText(), driverOrange.findElement(By.id("nombreUsuario")).getText());    
    jsB.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    assertEquals(driverOrange.findElement(By.id("turnoTesting")).getText(), driverOrange.findElement(By.id("nombreUsuario")).getText());
    jsB.executeScript("testingDado(5);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(3);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(2);");
    Thread.sleep(1000);
    assertEquals(driverOrange.findElement(By.id("turnoTesting")).getText(), driverOrange.findElement(By.id("nombreUsuario")).getText());
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(0);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverOrange.findElement(By.id("posicionOrange")).getText(), "56");
    assertEquals(driverBlue.findElement(By.id("turnoTesting")).getText(), driverBlue.findElement(By.id("nombreUsuario")).getText());
    jsA.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsA.executeScript("testingDado(5);");
    Thread.sleep(1000);
    jsA.executeScript("testingDado(5);");
    Thread.sleep(1000);
    jsA.executeScript("testingDado(5);");
    Thread.sleep(1000);
    jsA.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "55");
    jsB.executeScript("testingDado(6);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverOrange.findElement(By.id("posicionOrange")).getText(), "62");
    jsA.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "56");
    jsB.executeScript("testingDado(2);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverOrange.findElement(By.id("posicionOrange")).getText(), "62");
    jsA.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "60");
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverOrange.findElement(By.id("posicionOrange")).getText(), "63");    
    Thread.sleep(1000);
    assertNotEquals(driverOrange.findElement(By.id("areaChat")).getText().indexOf("ha ganado"), -1);
    assertNotEquals(driverBlue.findElement(By.id("areaChat")).getText().indexOf("ha ganado"), -1);
    Thread.sleep(2000);
  }

  @After
  public void tearDown() throws Exception {
    driverBlue.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
    driverOrange.quit();
    String verificationErrorString2 = verificationErrors.toString();
    if (!"".equals(verificationErrorString2)) {
      fail(verificationErrorString2);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driverBlue.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driverBlue.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driverBlue.switchTo().alert();
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
