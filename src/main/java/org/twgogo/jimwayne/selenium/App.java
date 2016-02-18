package org.twgogo.jimwayne.selenium;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Hello world!
 *
 */
public class App {
	private static Logger log = LoggerFactory.getLogger("org.twgogo.jimwayne.main");
	
	public static void main(String[] args) {
		String htmlunitSource = null;
		String webdriverSource = null;
		
		File htmlunitFile = new File("D:/test/htmlunit");
		File webdriverFile = new File("D:/test/webdriver");
		
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		
		try {
			log.trace("Try to fetch page using htmlunit.");
			HtmlPage page = webClient.getPage("http://www.bloomberg.com/news/articles/2016-02-17/from-punch-bag-to-savior-joyce-s-rise-mirrors-qantas-turnaround");
			htmlunitSource = page.getWebResponse().getContentAsString();
			
			//log.trace("Body: {}", Jsoup.parse(htmlunitSource).toString());
			log.trace("Write the result of fetched body to file {}.", htmlunitFile.getAbsolutePath());
			FileUtils.writeStringToFile(htmlunitFile, Jsoup.parse(htmlunitSource).getElementsByAttributeValue("class", "article-body").toString());
			log.trace("Result of fetching by htmlunit is written to {}.", htmlunitFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
		
		//HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38, true);
		
		WebDriver driver = new FirefoxDriver();
		
		try {
			log.trace("Try to fetch page by webdriver.");
			driver.get("http://www.bloomberg.com/news/articles/2016-02-17/from-punch-bag-to-savior-joyce-s-rise-mirrors-qantas-turnaround");
			webdriverSource = driver.getPageSource();
			/*WebElement bodyElement = driver.findElement(By.tagName("body"));
			bodyElement.getText();
			log.trace("Body: {}", bodyElement.);*/
			log.trace("Write the result of fetched body to file {}.", webdriverFile.getAbsolutePath());
			FileUtils.writeStringToFile(webdriverFile, Jsoup.parse(webdriverSource).getElementsByAttributeValue("class", "article-body").toString());
			log.trace("Result of fetching by webdriver is written to {}.", webdriverFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}
		
		log.debug("Compare: {}", htmlunitSource.compareTo(webdriverSource));
	}
}
