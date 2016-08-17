package com.stgconsulting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.containsAny;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * Created by Richard Harkins on 7/5/2016.
 */

public class WebPageTest {

    private WebDriver driver;
    static String baseWebPageURL = "http://www.skiutah.com";
    private boolean browserStarted = false;
    private HashMap<String, String> resortList = new HashMap<String, String>();

    public void Initialize()
    {
        // Populate resortList HashMap
        resortList.put("beaver mountain", "Beaver Mtn");
        resortList.put("cherry peak", "Cherry Peak");
        resortList.put("nordic valley", "Nordic Valley");
        resortList.put("powder mountain", "Powder Mtn");
        resortList.put("snowbasin", "Snowbasin");
        resortList.put("alta", "Alta");
        resortList.put("brighton", "Brighton");
        resortList.put("snowbird", "Snowbird");
        resortList.put("solitude", "Solitude");
        resortList.put("deer valley", "Deer Valley");
        resortList.put("park city", "Park City");
        resortList.put("sundance", "Sundance");
        resortList.put("brian head", "Brian Head");
        resortList.put("eagle point", "Eagle Point");
    }

    public void StartBrowser()
    {
        // Firefoxdriver settings
        File pathToBinary = new File("C:\\Users\\Richard Harkins\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        driver = new FirefoxDriver(ffBinary,firefoxProfile);
        //driver = new FirefoxDriver();

        // Chromedriver settings
        //File file = new File("C:\\ChromeDriver\\chromedriver.exe");
        //System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
        //driver = new ChromeDriver();

        // Set all new windows to maximize
        driver.manage().window().maximize();
        // Set an implicit wait of 10 seconds to handle delays in loading and finding elements
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    private void VerifyPageTitle(String webPageURL, String titleStringToTest)
    {
        // StartBrowser
        if (browserStarted == false)
        {
            StartBrowser();
            browserStarted = true;
        }
        // Open Webpage URL
        driver.get(webPageURL);
        // Get page title of current page
        String pageTitle = driver.getTitle();
        // Print page title of current page
        System.out.println("Page title of current page is: " + pageTitle);
        // Print title string to test
        System.out.println("Title String to Test is: " + titleStringToTest);
        // Test that the titleStringToTest = title of current page
        Assert.assertTrue(pageTitle.equals(titleStringToTest), "Current Page Title is not equal to the expected page title value");
        // If there is no Assertion Error, Print out that the Current Page Title = Expected Page Title
        System.out.println("Current Page Title = Expected Page Title");

    }

    private void VerifyNavigation(String navigationMenu)
    {
        // Build CSS Selector based on navigation menu user wants to click on
        String cssSelectorText = "a[title='" + navigationMenu + "']";
        // Find menu WebElement based on CSS Selector
        WebElement navigationMenuWebElement = driver.findElement(By.cssSelector(cssSelectorText));
        // Get href attributte from menu WebElement
        String navigationMenuURL = navigationMenuWebElement.getAttribute("href");
        // Navigate to href and validate page title
        VerifyPageTitle(navigationMenuURL, "Ski and Snowboard The Greatest Snow on Earth® - Ski Utah");
    }

    private void SubMenuNavigation(String navigationMenu, String navigationSubMenu) throws InterruptedException {
        // Build CSS Selector based on navigation menu user wants to click on
        String cssSelectorTextNavigationMenu = "a[title='" + navigationMenu + "']";
        // Find menu WebElement based on CSS Selector
        Boolean isPresent = driver.findElements(By.cssSelector(cssSelectorTextNavigationMenu)).size() == 1;
        // Check if navigation menu item exists
        if (isPresent)
        {
            // Get navigation menu WebElement
            WebElement navigationMenuWebElement = driver.findElement(By.cssSelector(cssSelectorTextNavigationMenu));
            // Get href attributte from navigation menu WebElement
            String navigationMenuURL = navigationMenuWebElement.getAttribute("href");
            //Create Actions object
            Actions mouseHover = new Actions(driver);
            // Move to navigation menu WebElement to initiate a hover event
            mouseHover.moveToElement(navigationMenuWebElement).perform();
            //String cssSelectorTextSubMenu = "a[title='" + navigationSubMenu + "']";
            // Build navigation submenu xpath to anchor tag
            String xpathSelectorTextSubmenu = "//a[.='" + navigationSubMenu + "']";
            //WebElement navigationSubMenuWebElement = driver.findElement(By.linkText(navigationSubMenu));
            // Get navigation submenu WebElement
            WebElement navigationSubMenuWebElement = driver.findElement(By.xpath(xpathSelectorTextSubmenu));
            // Check if navigation submenu exists
            Assert.assertTrue(navigationSubMenuWebElement.isEnabled(), (navigationSubMenu + " navigation submenu does not exist on this page"));
            // Click on navigation submenu WebElement
            navigationSubMenuWebElement.click();
            //mouseHover.perform();
            // Navigate to href and validate page title
            //VerifyPageTitle(navigationMenuURL, "Ski and Snowboard The Greatest Snow on Earth® - Ski Utah");
        }
        else
        {
            // Print message indicating that the navigation menu passed in to this method does not exist on the page
            System.out.println(navigationMenu + " navigation menu does not exist on this page");
        }
    }

    private void getResortMileageFromAirport(String resortName)
    {
        // Find the Compare Resorts WebElement and click on it
        WebElement compareResorts = driver.findElement(By.xpath(".//*[@id='ski-utah-welcome-map']/div/div[2]/div[4]/label/span[1]"));
        compareResorts.click();

        // Find the Miles from Airport menu item popout and click on it
        WebElement milesFromAirport = driver.findElement(By.xpath("//*[@id=\"ski-utah-welcome-map\"]/div/div[2]/div[4]/div/label[1]"));
        milesFromAirport.click();

        // Check if passed in resortName is empty or null
        if (isBlank(resortName))
        {
            // Print that no resort name was entered
            System.out.println("No Resort Name entered");
            // Exit from method
            return;
        }

        // Convert passed in resortName to all lowercase
        String resortNameLowercase = resortName.toLowerCase();
        // Declare resortValueName for holding value from HashMap
        String resortValueName = null;
        // Get resortValueName from HashMap
        resortValueName = resortList.get(resortNameLowercase);

        // Check if value returned from HashMap does not exist - returns null from HashMap
        if (isBlank(resortValueName))
        {
            System.out.println("Resort Name not found");
        }

        // Resort Name found in HashMap
        else
        {
            // Print passed in resortName
            // System.out.println("Entered Resort Name = " + resortName);
            // Print resortValueName from HashMap
            // System.out.println("Resort Value = " + resortValueName);
            // Get WebElement (first span tag) using resortValueName
            WebElement resortMileageFromAirport = driver.findElement(By.xpath("//span[@class='map-Area-shortName'][.='" + resortValueName + "']"));
            // Print class value of WebElement
            // System.out.println(resortMileageFromAirport.getAttribute("class"));
            // Print text value of WebElement
            // System.out.println(resortMileageFromAirport.getText());
            // Get WebElement (span tag with distance value) using resortValueName
            WebElement resortMileageFromAirportElement = resortMileageFromAirport.findElement(By.xpath("//span[@class='map-Area-shortName'][.='" + resortValueName + "']" + "/following-sibling::span[contains(@class,'distance')]"));
            // Print class value of WebElement
            // System.out.println(resortMileageFromAirportElement.getAttribute("class"));
            // Print innerHtml value of WebElement
            // System.out.println(resortMileageFromAirportElement.getAttribute("innerHTML"));
            // Create a variable to hold the resortMileage value
            String resortMileage = resortMileageFromAirportElement.getAttribute("innerHTML");
            // Print the mileage from the airport for the passed in resort name
            System.out.println(resortValueName + " is " + resortMileage + " miles from the airport");

        }
    }

    @Test
    private void TestLauncher() throws InterruptedException {

        Initialize();

        VerifyPageTitle(baseWebPageURL, "Ski Utah - Ski Utah");
        getResortMileageFromAirport("EAGLE point");
        //GetResortMileageFromAirport(resortName);
        //VerifyNavigation("Explore");
        //SubMenuNavigation("Explore", "Stories - Photos - Videos");

    }
}
