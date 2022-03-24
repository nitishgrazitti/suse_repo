package automation.Suse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static automation.Suse.Waits.*;
import static automation.Suse.PageActions.*;
import static automation.Suse.ExcelOprations.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {
	static ArrayList<String> urlsFromExcel = new ArrayList<String>();
	static ArrayList<String> test = new ArrayList<String>();
	static ArrayList<String> linksToArrayList = new ArrayList<String>();
	static Set<String> secondLevelLinksToSet = new HashSet<String>();
	static Set<String> firstLevelLinksToSet = new HashSet<String>();
	static int m;
	static String totalLinksCount;
	static int firstLevelEachIterationLinkCount;
	static int secondLevelEachIterationLinkCount;
	static List<String> firstLevelSuseLinksOnly = new ArrayList<String>();
	static List<String> secondLevelSuseLinksOnly = new ArrayList<String>();
	static List<String> thirdPartyLinks = new ArrayList<String>();
	static int currentPage;
	static int writeTotalLinksCountToExcel;
	static int currentRowCount;
	static int thirdPartyLinkCount;
	static String paginationCount;
	static int pageCount;
	static Object[] secondLevelLinksToArray;
	static Object[] firstLevelLinksToArray;

	public static void main(String[] args) throws Exception {
//		openBrowser();
//		getBlogPaginationCount();
//		validateBlogLinks();
//		getFirstLevelMenuLinksToSet("Products", "product", "product");
//		navigateToSecondLevelLinks("Products", "product", "product");
//		getFirstLevelMenuLinksToSet("Solutions", "solution", "");
//		navigateToSecondLevelLinks("Solutions", "solution", "");
//		getFirstLevelMenuLinksToSet("Support", "support", "");
//		navigateToSecondLevelLinks("Support", "support", "");
//		getFirstLevelMenuLinksToSet("Partners", "partner", "partner");
//		navigateToSecondLevelLinks("Partners", "partner", "partner");
//		getFirstLevelMenuLinksToSet("Communities", "communities", "");
//		navigateToSecondLevelLinks("Communities", "communities", "");
//		getFirstLevelMenuLinksToSet("About", "about", "");
//		navigateToSecondLevelLinks("About", "about", "");
//		writeFirstLevelLinksToExcel();
//		writeSecondLevelLinksToExcel();
		readDataFromExcel(0);
		getStatusCodeOfUrls(0);
//		readDataFromExcel(1);
//		getStatusCodeOfUrls(1);
//		getExecutionTime();
//		closeBrowser();
	}

	public static void getStatusCodeOfUrls(int sheetNum) throws Exception {
		System.out.println("------------Verify Status Codes of URLs -----------------");
		int totalURLTested = 0;
		for (int i = 0; i < urlsFromExcel.size(); i++) {
			URL url = new URL(urlsFromExcel.get(i));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			try {
				connection.connect();
			} catch (Exception e) {
				System.out.println("Handling connect exception");
				connection.connect();
			}
			int code = connection.getResponseCode();
			String statusCode = Integer.toString(code);
			int j = i + 1;
			System.out.println(j + " URL done");
			writeDataToExcel(sheetNum, i + 1, 1, statusCode); // Here i+1 because we write the text in second row, (in
																// first
			// row there is heading)

			int validStatusCode = 200;
			int validateStatusCode1 = 201;
			if (code == validStatusCode || code == validateStatusCode1) {
				writeDataToExcel(sheetNum, i + 1, 2, "Pass");
			} else {
				writeDataToExcel(sheetNum, i + 1, 2, "Fail");
			}
			totalURLTested = j;
		}
		urlsFromExcel.clear();
		System.out.println("------------Total URLs Tested => " + totalURLTested + "------------");
		System.out.println("------------Completd Verification of  Status Codes -----------------");

	}

	public static void getFirstLevelMenuLinksToSet(String menuName, String classAttribute, String hrefAttribute)
			throws Exception {
		// for menu tabs - first level
		driver.navigate().to("https://www.suse.com/");
		executeJavaScriptClick(driver.findElement(By.xpath("//a[text()='" + menuName + "']")));
		Thread.sleep(4000);
		List<WebElement> totalFirstLevelLinks = driver.findElements(
				By.xpath("//a[contains(@class,'" + classAttribute + "') and contains(@href,'" + hrefAttribute + "')]"));
		firstLevelEachIterationLinkCount = totalFirstLevelLinks.size();
		for (int i = 1; i <= firstLevelEachIterationLinkCount; i++) {
			firstLevelLinksToSet.add(driver.findElement(By.xpath("(//a[contains(@class,'" + classAttribute
					+ "') and contains(@href,'" + hrefAttribute + "')])[position()=" + i + "]")).getAttribute("href"));
		}

	}

	public static void writeFirstLevelLinksToExcel() throws Exception {
		firstLevelLinksToArray = firstLevelLinksToSet.toArray();
		for (int i = 0; i < firstLevelLinksToArray.length; i++) {
			String linkToExcel = firstLevelLinksToArray[i].toString();
			if (linkToExcel.contains("https://www.suse.com")) {
				firstLevelSuseLinksOnly.add(linkToExcel);
			} else {
				thirdPartyLinks.add(linkToExcel);
			}
		}
		for (int k = 0; k < firstLevelSuseLinksOnly.size(); k++) {
			String onlySuseLinkToExcel = firstLevelSuseLinksOnly.get(k);
			writeDataToExcel(0, k + 1, 0, onlySuseLinkToExcel);
		}
	}

	public static void navigateToSecondLevelLinks(String tabName, String classAttribute, String hrefAttribute)
			throws Exception {
		for (int i = 1; i <= firstLevelEachIterationLinkCount; i++) {
			getSecondLevelLinksToSet(i, tabName, classAttribute, hrefAttribute);
			writeTotalLinksCountToExcel = writeTotalLinksCountToExcel + secondLevelEachIterationLinkCount;
			totalLinksCount = "Total Links are: " + Integer.toString(writeTotalLinksCountToExcel);
		}
	}

	public static void getSecondLevelLinksToSet(int position, String tabName, String classAttribute,
			String hrefAttribute) throws Exception {
		// collecting all links in set for each link iteration
		driver.navigate().to("https://www.suse.com/");
		clickElement(driver.findElement(By.xpath("//a[text()='" + tabName + "']")), "");
		String currentURL=driver.findElement(By.xpath("(//a[contains(@class,'" + classAttribute
				+ "') and contains(@href,'" + hrefAttribute + "')])[position()=" + position + "]")).getAttribute("href");
		driver.navigate().to(currentURL);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			Thread.sleep(4000);
			List<WebElement> totalSecondLevelLinks = driver.findElements(By.xpath("//a[starts-with(@href,'https')]"));
			secondLevelEachIterationLinkCount = totalSecondLevelLinks.size();
			for (int i = 1; i <= secondLevelEachIterationLinkCount; i++) {
				secondLevelLinksToSet
						.add(driver.findElement(By.xpath("(//a[contains(@href,'https')])[position()=" + i + "]"))
								.getAttribute("href"));
		}
	}

	public static void writeSecondLevelLinksToExcel() throws Exception {
		secondLevelLinksToArray = secondLevelLinksToSet.toArray();
		for (int j = 0; j < secondLevelLinksToSet.size(); j++) {
			String linkToExcel = secondLevelLinksToArray[j].toString();
			if (linkToExcel.contains("https://www.suse.com")) {
				// || linkToExcel.contains("https://susergs.com")
				secondLevelSuseLinksOnly.add(linkToExcel);
			} else {
				thirdPartyLinks.add(linkToExcel);
			}
		}
		for (int k = 0; k < secondLevelSuseLinksOnly.size(); k++) {
			String onlySuseLinkToExcel = secondLevelSuseLinksOnly.get(k);
			writeDataToExcel(1, k + 1 + currentRowCount, 0, onlySuseLinkToExcel);
		}
		for (int l = 0; l < thirdPartyLinks.size(); l++) {
			String thirdPartyLink = thirdPartyLinks.get(l);
			writeDataToExcel(2, l + 1 + thirdPartyLinkCount, 0, thirdPartyLink);
		}
		currentRowCount = currentRowCount + secondLevelSuseLinksOnly.size();
		thirdPartyLinkCount = thirdPartyLinkCount + thirdPartyLinks.size();
		thirdPartyLinks.clear();
	}

	public static void getBlogPaginationCount() {
		paginationCount = driver.findElement(By.xpath("//a[contains(@class,'next')]/preceding-sibling::a[1]"))
				.getText();
		pageCount = Integer.parseInt(paginationCount);
	}

	public static void validateBlogLinks() throws Exception {
		try {
			for (m = 1; m <= pageCount; m++) {
				List<WebElement> blogPerPage = driver
						.findElements(By.xpath("//div[@class='blog-home-card']//*[contains(@class,'read-more')]"));
				int blogsCountPerPage = blogPerPage.size();
				for (int j = 1; j <= blogsCountPerPage; j++) {
					linksToArrayList.add(driver.findElement(By.xpath(
							"(//div[@class='blog-home-card']//*[contains(@class,'read-more')])[position()=" + j + "]"))
							.getAttribute("href"));
				}
				for (int k = 0; k < blogsCountPerPage; k++) {
					String linkToExcel = linksToArrayList.get(k);
					writeDataToExcel(0, k + 1 + currentRowCount, 0, linkToExcel);
				}
				currentRowCount = currentRowCount + linksToArrayList.size();
				linksToArrayList.clear();
				scrollToBottomOfPage();
				currentPage = m + 1;
				if (m != pageCount) {
					executeJavaScriptClick(driver.findElement(By.xpath("//*[contains(@class,'next')]")));
					driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					scrollToBottomOfPage();
					verifyWebElementPresent(driver
							.findElement(By.xpath("//span[@aria-current='page' and text()=" + currentPage + "]")));
				} else {
					System.out.println("User is on last page");
				}

			}
		} catch (Exception e) {
			String failedURL = driver.getCurrentUrl();
			System.err.println("Failed at URL: " + failedURL);
			for (int k = 0; k < 1; k++) {
				writeDataToExcel(0, k + 1 + currentRowCount, 0, failedURL);
				currentRowCount = currentRowCount + 1;
			}
			driver.navigate().refresh();
			for (m = currentPage; m <= pageCount; m++) {
//				Boolean isPresent = driver.findElements(By.xpath("//h1[text()='Page Not Found']")).size() > 0;
//				if(isPresent==true) {
//					break;
//				}
				List<WebElement> blogPerPage = driver
						.findElements(By.xpath("//div[@class='blog-home-card']//*[contains(@class,'read-more')]"));
				int blogsCountPerPage = blogPerPage.size();
				for (int j = 1; j <= blogsCountPerPage; j++) {
					linksToArrayList.add(driver.findElement(By.xpath(
							"(//div[@class='blog-home-card']//*[contains(@class,'read-more')])[position()=" + j + "]"))
							.getAttribute("href"));
				}
				for (int k = 0; k < blogsCountPerPage; k++) {
					String linkToExcel = linksToArrayList.get(k);
					writeDataToExcel(0, k + 1 + currentRowCount, 0, linkToExcel);
				}
				currentRowCount = currentRowCount + linksToArrayList.size();
				linksToArrayList.clear();
				scrollToBottomOfPage();
				try {
					if (m != pageCount) {
						executeJavaScriptClick(driver.findElement(By.xpath("//*[contains(@class,'next')]")));
						currentPage = m + 1;
						driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
						scrollToBottomOfPage();
						verifyWebElementPresent(driver
								.findElement(By.xpath("//span[@aria-current='page' and text()=" + currentPage + "]")));
					} else {
						System.out.println("Execution Completed");
					}
				} catch (Throwable g) {
					String failedURL1 = driver.getCurrentUrl();
					if (failedURL != failedURL1) {
						System.err.println("Failed at URL: " + failedURL1);
						for (int k = 0; k < 1; k++) {
							writeDataToExcel(0, k + 1 + currentRowCount, 0, failedURL1);
							currentRowCount = currentRowCount + 1;
						}
					} else {
						System.out.println("Duplicate failed link");
					}
					System.out.println("Page issue");
					if (driver.findElement(By.xpath("//h1[text()='Page Not Found']")).isDisplayed()) {
						break;
					}
				}
			}
		} finally {
			System.out.println("Blog links verified");
		}
	}

	public static void getExecutionTime() throws Exception {
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		long minutes = totalTime / 1000 / 60;
		long seconds = totalTime / 1000 % 60;
		String executionTime = "Execution time is: " + minutes + " minutes and " + seconds + " seconds";
		for (int k = 0; k < 1; k++) {
			writeDataToExcel(0, k + 5 + firstLevelSuseLinksOnly.size(), 0, executionTime);
		}
		String uniqueLinks = "Unique Suse Links: " + Integer.toString(secondLevelSuseLinksOnly.size());
		writeDataToExcel(0, firstLevelSuseLinksOnly.size() + 3, 0, totalLinksCount);
		writeDataToExcel(0, firstLevelSuseLinksOnly.size() + 4, 0, uniqueLinks);
		System.out.println(executionTime);

	}

	public static void closeBrowser() {
		driver.close();
	}
}
