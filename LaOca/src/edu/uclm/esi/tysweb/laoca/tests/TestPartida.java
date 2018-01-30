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
  private WebDriver driverRed, driverBlue;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.chrome.driver", "D:\\Descargas\\Google Chrome\\chromedriver.exe");  
	driverRed = new ChromeDriver();
    driverBlue = new ChromeDriver();
    driverRed.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driverBlue.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testPartida() throws Exception {
    driverRed.get("http://localhost:8080/LaOca/");
    JavascriptExecutor jsA =(JavascriptExecutor)driverRed;
    WebElement element = driverRed.findElement(By.id("login-form"));
    jsA.executeScript("arguments[0].style.display='none'", element);
    WebElement element2 = driverRed.findElement(By.id("guest-form"));
    jsA.executeScript("arguments[0].style.display='block'", element2);
    Thread.sleep(1000);
    driverRed.findElement(By.id("guest-submit")).click();
    driverRed.findElement(By.linkText("Crear partida")).click();
    
    Thread.sleep(1000);
    driverBlue.get("http://localhost:8080/LaOca/");
    JavascriptExecutor jsB =(JavascriptExecutor)driverBlue;
    WebElement element3 = driverBlue.findElement(By.id("login-form"));
    jsB.executeScript("arguments[0].style.display='none'", element3);
    WebElement element4 = driverBlue.findElement(By.id("guest-form"));
    jsB.executeScript("arguments[0].style.display='block'", element4);
    Thread.sleep(1000);
    driverBlue.findElement(By.id("guest-submit")).click();
    Thread.sleep(1000);
    WebElement element6 = driverBlue.findElement(By.id("unirsePartida"));
    jsB.executeScript("arguments[0].style.display='block'", element6);
    driverRed.findElement(By.id("button-crear-partida")).click();
    Thread.sleep(1000);
    driverBlue.findElement(By.id("button-unirse")).click();
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
    assertEquals(driverRed.findElement(By.id("posicionRed")).getText(), "15");
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "2");
    jsA.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverRed.findElement(By.id("posicionRed")).getText(), "19");    
    assertEquals(driverBlue.findElement(By.id("turnoTesting")).getText(), driverBlue.findElement(By.id("nombreUsuario")).getText());    
    jsB.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    assertEquals(driverBlue.findElement(By.id("turnoTesting")).getText(), driverBlue.findElement(By.id("nombreUsuario")).getText());
    jsB.executeScript("testingDado(5);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(3);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(2);");
    Thread.sleep(1000);
    assertEquals(driverBlue.findElement(By.id("turnoTesting")).getText(), driverBlue.findElement(By.id("nombreUsuario")).getText());
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsB.executeScript("testingDado(0);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "56");
    assertEquals(driverRed.findElement(By.id("turnoTesting")).getText(), driverRed.findElement(By.id("nombreUsuario")).getText());
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
    assertEquals(driverRed.findElement(By.id("posicionRed")).getText(), "55");
    jsB.executeScript("testingDado(6);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "62");
    jsA.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverRed.findElement(By.id("posicionRed")).getText(), "56");
    jsB.executeScript("testingDado(2);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "62");
    jsA.executeScript("testingDado(4);");
    Thread.sleep(1000);
    jsA.executeScript("fichasTesting();");
    assertEquals(driverRed.findElement(By.id("posicionRed")).getText(), "60");
    jsB.executeScript("testingDado(1);");
    Thread.sleep(1000);
    jsB.executeScript("fichasTesting();");
    assertEquals(driverBlue.findElement(By.id("posicionBlue")).getText(), "63");    
    Thread.sleep(1000);
    assertNotEquals(driverBlue.findElement(By.id("areaChat")).getText().indexOf("ha ganado"), -1);
    assertNotEquals(driverRed.findElement(By.id("areaChat")).getText().indexOf("ha ganado"), -1);
    Thread.sleep(2000);
  }

  @After
  public void tearDown() throws Exception {
    driverRed.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
    driverBlue.quit();
    String verificationErrorString2 = verificationErrors.toString();
    if (!"".equals(verificationErrorString2)) {
      fail(verificationErrorString2);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driverRed.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driverRed.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driverRed.switchTo().alert();
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
