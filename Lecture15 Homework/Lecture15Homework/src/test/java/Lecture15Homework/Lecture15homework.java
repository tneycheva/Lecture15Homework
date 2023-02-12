package Lecture15Homework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Lecture15homework {
    private WebDriver driver;

    @BeforeSuite
    protected final void setupTestSuite() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.edgedriver().setup();
    }

    @BeforeMethod
    protected final void setUpTest() {
        this.driver = new EdgeDriver();
        this.driver.manage().window().maximize();
        this.driver.manage().timeouts().pageLoadTimeout((Duration.ofSeconds(20)));
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    }


    @Test
    public void testCheckboxes() {

        driver.get("http://the-internet.herokuapp.com/checkboxes");

        WebElement checkbox1 = driver.findElement(By.cssSelector("#checkboxes > input[type=checkbox]:nth-child(1)"));
        boolean checkbox1State = checkbox1.isSelected();
        checkbox1.click();
        WebElement checkbox2 = driver.findElement(By.cssSelector("#checkboxes > input[type=checkbox]:nth-child(3)"));
        boolean checkbox2State = checkbox2.isSelected();
        checkbox2.click();

        Assert.assertNotEquals(checkbox1State, checkbox1.isSelected());
        Assert.assertNotEquals(checkbox2State, checkbox2.isSelected());

    }

    @Test
    public void testMultipleWindows() {
        driver.get("http://the-internet.herokuapp.com/windows");
        String firstWindowHandle = driver.getWindowHandle();
        WebElement clickHereLink = driver.findElement(By.linkText("Click Here"));
        clickHereLink.click();

        // Switch to new window opened
        // for(String winHandle : driver.getWindowHandles()){
        //   driver.switchTo().window(winHandle);
        // }

        List<String> alabala = (List<String>) driver.getWindowHandles();
        driver.switchTo().window(alabala.get(0));

        WebElement newWindowText = driver.findElement(By.xpath("//h3[contains(text(),'New Window')]"));
        Assert.assertTrue(newWindowText.isDisplayed());


        // Switch back to original browser (first window)
        driver.switchTo().window(firstWindowHandle);
        WebElement openingNewWindowText = driver.findElement(By.xpath("//h3[contains(text(),'Opening a new window')]"));
        Assert.assertTrue(openingNewWindowText.isDisplayed());


    }

    @Test
    public void testAddRemoveElements() {
        driver.get("http://the-internet.herokuapp.com/add_remove_elements/");

        WebElement addElementButton = driver.findElement(By.cssSelector("[onclick='addElement()']"));
        addElementButton.click();

        WebElement deleteElementButton = driver.findElement(By.cssSelector("[onclick='deleteElement()']"));
        deleteElementButton.click();

        WebElement elementsDiv = driver.findElement(By.id("elements"));
        Assert.assertEquals(elementsDiv.getAttribute("childElementCount"), "0");

    }

    @Test
    public void testBasicAuth() {

        //username:password@site.com

        driver.get("http://admin:admin@the-internet.herokuapp.com/basic_auth");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement CongratsText = driver.findElement(By.cssSelector("#content > div > p"));

        wait.until(ExpectedConditions.visibilityOf(CongratsText));
    }

    @Test
    public void testChallengingDOMtable() {
        driver.get("http://the-internet.herokuapp.com/challenging_dom");
        WebElement button1 = driver.findElement(By.xpath("//a[@class='button']"));

        button1.click();
        WebElement button2 = driver.findElement(By.xpath("//a[@class='button alert']"));

        button2.click();

        WebElement button3 = driver.findElement(By.xpath("//a[@class='button success']"));

        button3.click();

        List<WebElement> columnHeaders = driver.findElements(By.xpath("//table/thead//th"));
        int tableColumnsCount = columnHeaders.size();
        List<WebElement> cells = driver.findElements(By.xpath("//table/tbody//td"));
        int cellsCount = cells.size();
        int rowsCount = cellsCount / tableColumnsCount;
        String table [][] = new String[rowsCount][tableColumnsCount];
        WebElement tableElements [][] = new WebElement[rowsCount][tableColumnsCount];

        for (int i = 0; i < rowsCount; i ++){
            for (int j = 0; j < tableColumnsCount; j ++){
                String currXPath = String.format("//table/tbody//tr[%s]//td[%s]", i + 1, j + 1);
                table[i][j] = driver.findElement(By.xpath(currXPath)).getText();
                tableElements[i][j] = driver.findElement(By.xpath(currXPath));
            }
        }

    }

    @Test
    public void testContextMenu() {
        driver.get("http://the-internet.herokuapp.com/context_menu");

        WebElement rightClickBox = driver.findElement(By.id("hot-spot"));
        Actions action = new Actions(driver);
        action.contextClick(rightClickBox).perform();

        String actualText = driver.switchTo().alert().getText();
        String expectedText = "You selected a context menu";


        Assert.assertEquals(actualText, expectedText, "text is not equal");

    }

    @Test
    public void testDisappearingElements() {

    }

    @Test
    public void testDragAndDrop() {
        driver.get("http://the-internet.herokuapp.com/drag_and_drop");

        WebElement divA = driver.findElement(By.id("column-a"));
        WebElement divB = driver.findElement(By.id("column-b"));


        Actions action = new Actions(driver);
        action.clickAndHold(divA). dragAndDrop(divA, divB);





        //builder.dragAndDrop(divA, divB).perform();
        //Action dragAndDrop = builder.moveToElement(divA).clickAndHold(divA).moveToElement(divB).release(divB).build();
        //dragAndDrop.perform();
        //

    }
    @Test
    public void testDropDown() {
        driver.get("http://the-internet.herokuapp.com/dropdown");

        WebElement dropdown = driver.findElement(By.id("dropdown"));
        Select select = new Select(dropdown);
        WebElement firstOption = select.getFirstSelectedOption();
        String selectedOption0 = select.getFirstSelectedOption().getText();
        Assert.assertEquals(selectedOption0, "Please select an option");
        //select.selectByIndex(0);
        select.selectByVisibleText("Option 2");
        Assert.assertFalse(firstOption.isEnabled());
        String selectedOption = select.getFirstSelectedOption().getText();
        Assert.assertEquals(selectedOption, "Option 2");

    }
    @Test
    public void dynamicContent() {

    }
    @Test
    public void dynamicControls() {
        driver.get("http://the-internet.herokuapp.com/dynamic_controls");

        By checkBoxLoc = By.id("checkbox");
        WebElement checkbox = driver.findElement(checkBoxLoc);
        Assert.assertTrue(checkbox.isDisplayed());
        WebElement removeBtn = driver.findElement(By.xpath("//*[text()='Remove']"));
        removeBtn.click();
        Assert.assertTrue(driver.findElement(By.id("loading")).isDisplayed());
        //Wait wait = new WebDriverWait(driver, 10);
        // wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("loading"))));
        //Assert.assertFalse(isElementPresent(driver, checkBoxLoc);
        Assert.assertEquals(driver.findElement(By.id("message")).getText(), "It's gone!");
        WebElement addBtn = driver.findElement(By.xpath("//*[text()='Add']"));
        Assert.assertTrue(addBtn.isDisplayed());

    }
    @Test
    public void dynamicLoading() {
        driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");

        By startBtnLoc = By.xpath("//div[@id='start']/button[contains(text(),'Start')]");
        By loadingBarLoc = By.id("loading");
        List<WebElement> loadingBars = driver.findElements(loadingBarLoc);
        Assert.assertTrue(loadingBars.isEmpty());
        WebElement hello = driver.findElement(By.xpath("//div[@id='finish']/h4"));
        Assert.assertFalse(hello.isDisplayed());
        WebElement startBtn = driver.findElement(startBtnLoc);
        startBtn.click();
        //Wait wait = new WebDriverWait(driver, 10);
        // wait.until(ExpectedConditions.visibilityOfElementLocated(loadingBarLoc));
        // wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingBarLoc));
        Assert.assertTrue(hello.isDisplayed());
        Assert.assertEquals(hello.getText(), "Hello World!");

    }


    @Test
    public void FloatingMenu() {
        driver.get("http://the-internet.herokuapp.com/floating_menu");

        WebElement floatingMenuTitle = driver.findElement(By.xpath("//h3[contains(text(), 'Floating Menu')]"));
        Assert.assertTrue(floatingMenuTitle.isDisplayed());
        WebElement menu = driver.findElement(By.id("menu"));
        Assert.assertTrue(menu.isDisplayed());
        WebElement homeMenu = driver.findElement(By.linkText("Home"));
        Assert.assertTrue(homeMenu.isDisplayed());
        WebElement newsMenu = driver.findElement(By.linkText("News"));
        Assert.assertTrue(newsMenu.isDisplayed());
        WebElement contactMenu = driver.findElement(By.linkText("Contact"));
        Assert.assertTrue(contactMenu.isDisplayed());
        WebElement aboutMenu = driver.findElement(By.linkText("About"));
        Assert.assertTrue(aboutMenu.isDisplayed());

        //to perform Scroll on application using Selenium
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,2000)", "");

        Assert.assertTrue(menu.isDisplayed());
        Assert.assertTrue(homeMenu.isDisplayed());
        Assert.assertTrue(newsMenu.isDisplayed());
        Assert.assertTrue(contactMenu.isDisplayed());
        Assert.assertTrue(aboutMenu.isDisplayed());

        js.executeScript("window.scrollBy(0,-1000)", "");

        Assert.assertTrue(menu.isDisplayed());
        Assert.assertTrue(homeMenu.isDisplayed());
        Assert.assertTrue(newsMenu.isDisplayed());
        Assert.assertTrue(contactMenu.isDisplayed());
        Assert.assertTrue(aboutMenu.isDisplayed());

        js.executeScript("window.scrollBy(0,-1000)", "");

        Assert.assertTrue(menu.isDisplayed());
        Assert.assertTrue(homeMenu.isDisplayed());
        Assert.assertTrue(newsMenu.isDisplayed());
        Assert.assertTrue(contactMenu.isDisplayed());
        Assert.assertTrue(aboutMenu.isDisplayed());

    }


    @Test
    public void Hovers() {
        driver.get("http://the-internet.herokuapp.com/hovers");

        WebElement image1 = driver.findElement(By.xpath("(//div[@class='figure']/img)[1]"));
        WebElement userInfo1Div1 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[1]"));
        WebElement userInfoName1 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[1]/h5"));
        WebElement userInfoProfileLink1 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[1]/a"));

        WebElement image2 = driver.findElement(By.xpath("(//div[@class='figure']/img)[2]"));
        WebElement userInfo1Div2 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[2]"));
        WebElement userInfoName2 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[2]/h5"));
        WebElement userInfoProfileLink2 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[2]/a"));

        WebElement image3 = driver.findElement(By.xpath("(//div[@class='figure']/img)[3]"));
        WebElement userInfo1Div3 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[3]"));
        WebElement userInfoName3 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[3]/h5"));
        WebElement userInfoProfileLink3 = driver.findElement(By.xpath("(//div[@class='figure']/div[@class='figcaption'])[3]/a"));

        Assert.assertFalse(userInfo1Div1.isDisplayed());
        Assert.assertFalse(userInfo1Div2.isDisplayed());
        Assert.assertFalse(userInfo1Div3.isDisplayed());

        Actions builder = new Actions(driver);
        builder.moveToElement(image1).perform();
        Assert.assertTrue(userInfo1Div1.isDisplayed());
        Assert.assertTrue(userInfoName1.isDisplayed());
        Assert.assertTrue(userInfoProfileLink1.isDisplayed());
        Assert.assertEquals(userInfoName1.getText(), "name: user1");
        Assert.assertEquals(userInfoProfileLink1.getText(), "View profile");
        Assert.assertEquals(userInfoProfileLink1.getAttribute("href"), "http://the-internet.herokuapp.com/users/1");

        builder.moveToElement(image2).perform();
        Assert.assertTrue(userInfo1Div2.isDisplayed());
        Assert.assertTrue(userInfoName2.isDisplayed());
        Assert.assertTrue(userInfoProfileLink2.isDisplayed());
        Assert.assertEquals(userInfoName2.getText(), "name: user2");
        Assert.assertEquals(userInfoProfileLink2.getText(), "View profile");
        Assert.assertEquals(userInfoProfileLink2.getAttribute("href"), "http://the-internet.herokuapp.com/users/2");

        builder.moveToElement(image3).perform();
        Assert.assertTrue(userInfo1Div3.isDisplayed());
        Assert.assertTrue(userInfoName3.isDisplayed());
        Assert.assertTrue(userInfoProfileLink3.isDisplayed());
        Assert.assertEquals(userInfoName3.getText(), "name: user3");
        Assert.assertEquals(userInfoProfileLink3.getText(), "View profile");
        Assert.assertEquals(userInfoProfileLink3.getAttribute("href"), "http://the-internet.herokuapp.com/users/3");



    }
    @Test
    public void redirectLink() {

    }
    @Test
    public void testTables() {
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-table"));
        int emailIndex = 0;

        //Get Column header
        WebElement tableColumnHeader = table.findElement(By.className("rt-thead"));
        List<WebElement> columnsHeaders = tableColumnHeader.findElements(By.cssSelector("[role='columnheader']"));

        for (WebElement column : columnsHeaders) {
            String columnName = column.getText();
            if (columnName.equals("Email")) {
                emailIndex = columnsHeaders.indexOf(column);
            }

            System.out.println(column.getText());
        }

        //Get Rows
        WebElement tableBody = table.findElement(By.className("rt-tbody"));
        List<WebElement> rows = tableBody.findElements(By.cssSelector("[role='row']"));

        //Find a row by email and delete it
        for (WebElement row : rows) {
            //Get Cells
            List<WebElement> cells = row.findElements(By.cssSelector("[role='gridcell']"));
            String cellText = cells.get(emailIndex).getText();
            if (cellText.equals("alden@example.com")) {
                //Delete row
                WebElement deleteButton = row.findElement(By.cssSelector("[id^=delete-record]"));
                deleteButton.click();
                break;
            }
        }

        //Get refreshed table body
        tableBody = table.findElement(By.className("rt-tbody"));
        rows = tableBody.findElements(By.cssSelector("[role='row']"));

        //Verify that the deleted row is removed
        for (WebElement row : rows) {
            //Get Cells
            List<WebElement> cells = row.findElements(By.cssSelector("[role='gridcell']"));
            String cellText = cells.get(emailIndex).getText();
            Assert.assertNotEquals(cellText, "alden@example.com");
        }
    }
    @Test
    public void testWindows() {
        driver.get("https://demoqa.com/browser-windows");
        //Uncomment to test tabs
        //WebElement button = driver.findElement(By.id("tabButton"));
        WebElement button = driver.findElement(By.id("windowButton"));
        button.click();

        //When new tab/window is opened, the driver focus remains on the initial tab/window, if no switch command is executed
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://demoqa.com/browser-windows");

        //Get available tabs
        List<String> windows = new ArrayList<>(driver.getWindowHandles());
        String secondWindow = windows.get(1);
        //Switch the driver focus on 2nd tab
        driver.switchTo().window(secondWindow);
        driver.manage().window().maximize();
        currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://demoqa.com/sample");
        WebElement headline = driver.findElement(By.id("sampleHeading"));
        String actualHeadlineText = headline.getText();
        Assert.assertEquals(actualHeadlineText, "This is a sample page");

        //Switch back to first tab/window
        String firstWindow = windows.get(0);
        //Switch the driver focus on 1st tab
        driver.switchTo().window(firstWindow);
        currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://demoqa.com/browser-windows");
        // driver.close() closes only the current window, but driver.quits() closes all opened windows
    }

    @Test
    public void testAlert() {
        driver.get("https://demoqa.com/alerts");

        WebElement alertButton = driver.findElement(By.id("alertButton"));
        alertButton.click();

        //Interact with alert
        Alert alert = driver.switchTo().alert();
        /*
        After switchTo().alert() we cannot interact with elements from the main window
        alertButton.getText();
         */
        String actualAlertText = alert.getText();
        Assert.assertEquals(actualAlertText, "You clicked a button");
        alert.accept();

        /*
        After alert().accept() we can interact with elements from the main window
        alertButton.getText();
         */
    }

    @Test
    public void testConfirmBox() {
        driver.get("https://demoqa.com/alerts");
        WebElement confirmBoxButton = driver.findElement(By.id("confirmButton"));
        confirmBoxButton.click();

        //Interact with ConfirmBox
        Alert alert = driver.switchTo().alert();

        //Confirm Box can be accepted or dismissed
        alert.dismiss();
        String actualMessage = driver.findElement(By.id("confirmResult")).getText();
        Assert.assertEquals(actualMessage, "You selected Cancel");
    }

    @Test
    public void testPromptBox() {
        driver.get("https://demoqa.com/alerts");
        WebElement promptBoxButton = driver.findElement(By.id("promtButton"));
        promptBoxButton.click();

        String name = "Teddy";

        //Interact with PromptBox
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(name);
        alert.accept();

        String actualText = driver.findElement(By.id("promptResult")).getText();
        String expectedText = "You entered " + name;
        Assert.assertEquals(actualText, expectedText);
    }

    @Test
    public void testHoover() {
        driver.get("https://demoqa.com/tool-tips");
        WebElement element = driver.findElement(By.id("toolTipButton"));

        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        String tooltipText = driver.findElement(By.className("tooltip-inner")).getText();
        Assert.assertEquals(tooltipText, "You hovered over the Button");
    }

    @Test
    public void testIFrames() {
        driver.get("https://demoqa.com/frames");

        //Find all iframes
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));

        /*
        iframe content is available after switch
        cannot execute before switch driver.findElement(By.id("sampleHeading")).getText();
        */
        //Switch by iframe name or id
        driver.switchTo().frame("frame1");
        String frameHeadingText = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(frameHeadingText, "This is a sample page");

        /*
        While driver is switched to particular frame the other page content is unavailable
        driver.findElement(By.cssSelector("#framesWrapper div")).getText();
         */

        //Go back to main page
        driver.switchTo().defaultContent();
        String mainPageText = driver.findElement(By.cssSelector("#framesWrapper div")).getText();
        Assert.assertEquals(mainPageText, "Sample Iframe page There are 2 Iframes in this page. Use browser inspecter or firebug to check out the HTML source. In total you can switch between the parent frame, which is this window, and the two frames below");

        //Switch to 3rd frame by index
        driver.switchTo().frame(2);
        frameHeadingText = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(frameHeadingText, "This is a sample page");

        //Switch to 2nd frame by WebElement
        driver.switchTo().defaultContent();
        iframes = driver.findElements(By.tagName("iframe"));
        WebElement iframeElement = iframes.get(1);
        driver.switchTo().frame(iframeElement);
        frameHeadingText = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(frameHeadingText, "This is a sample page");
    }
}
