package automation.Suse;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;



public class DynamicWebElements {

	static WebDriver driver;
	public static WebElement getWebElementByID(String value)
	{
		return driver.findElement(By.xpath("//*[@id='"+value+"']"));
	}

	
    
	public static WebElement getWebElementByText(String value)
	{
		return driver.findElement(By.xpath("//*[text()='"+value+"']"));
	}
	
	public static WebElement getWebElementByContainsText(String value)
	{
		return driver.findElement(By.xpath("//*[contains(text(),'"+value+"')]"));
	}
	
}
