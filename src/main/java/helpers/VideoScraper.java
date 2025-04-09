package helpers;

import database.MongoDatabaseHandler_Impl;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * @author Oliwia Daszczynska
 */
@Deprecated
public class VideoScraper {
    /**
     * @author Oliwia Daszczynska
     */
    @Deprecated
    public static void scrapeVideo(Document speech) {
        String sessionnr = String.valueOf(speech.getInteger("session_nr"));
        System.out.println(speech.getString("agenda_item"));
        Document speaker = MongoDatabaseHandler_Impl.getCollectionSpeaker().find(new Document("_id", speech.get("speaker_id"))).first();
        String formattedSpeaker = formatSpeaker(speaker);
        System.out.println(formattedSpeaker);

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://www.bundestag.de/mediathek/plenarsitzungen");

            // this is the speaker span. identified using filter-906296-906302
            WebElement speakerDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("span[aria-labelledby='select2-filter-906296-906302-container'] .select2-selection__rendered")));
            // click to open the speaker filter dropdown
            speakerDropdown.click();

            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[contains(text(), '" + formattedSpeaker + "')]")));
            option.click(); // click the option which contains speaker name

            // filter by session number field, identified using filter-906296-906304
            WebElement sessionNrField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("filter-906296-906304")));
            sessionNrField.click();
            sessionNrField.sendKeys(sessionnr);
            sessionNrField.sendKeys(Keys.ENTER);


            // RESULTS AFTER APPLYING FILTERS:
            WebElement elementWithVideoIds = driver.findElement(By.cssSelector("div.mediathekFilterRow.bt-mediathek-row"));
            WebElement elementWithItemCount = elementWithVideoIds.findElement(By.cssSelector("div[data-allitemcount]"));

            // Extract the value of the attribute
            String allItemCount = elementWithItemCount.getDomAttribute("data-allitemcount");
            String currentSlideCount = elementWithItemCount.getDomAttribute("data-currentslidecount");
            System.out.println("found video amount: " + allItemCount);

            List<WebElement> anchorElements = elementWithVideoIds.findElements(By.tagName("a"));

            // Iterate through all anchor elements and extract the href attributes
            for (WebElement anchor : anchorElements) {
                String href = anchor.getDomAttribute("href");
                System.out.println("video-url " + href);
                WebElement presidiumParagraph = anchor.findElement(By.cssSelector("p[data-presidium]"));
                if (presidiumParagraph != null) {
                    System.out.println("agenda-item " + presidiumParagraph.getText());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    /**
     * @author Oliwia Daszczynska
     */
    @Deprecated
    private static String formatSpeaker(Document speaker) {
        return formatField(speaker, "lastname", ", ") +
                formatField(speaker, "address_title", " ") +
                formatField(speaker, "firstname", " ") +
                formatField(speaker, "adel", " ") +
                formatField(speaker, "prefix", "");
    }

    /**
     * @author Oliwia Daszczynska
     */
    @Deprecated
    private static String formatField(Document doc, String field, String suffix) {
        String value = doc.getString(field);
        return (value != null && !value.isEmpty()) ? value + suffix : "";
    }
}
