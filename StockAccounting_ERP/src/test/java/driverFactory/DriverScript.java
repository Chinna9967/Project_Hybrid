package driverFactory;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.mongodb.MapReduceCommand.OutputType;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	public static WebDriver driver;
String inputpath ="E:\\QEDGE\\Automation Testing\\Project_Rangareddy\\StockAccounting_ERP_1\\FileInput\\DataEngine.xlsx";
String outputpath ="E:\\QEDGE\\Automation Testing\\Project_Rangareddy\\StockAccounting_ERP_1\\FileOutput\\HybridResults.xlsx";
ExtentReports reports;
ExtentTest test;
public void startTest()throws Throwable
{
	String ModuleStatus ="";
	///create reference object
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in master test cases sheet
	for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
	{
		if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
		{
			//store correspoding sheet into TCModule variable
			String TCModule =xl.getCellData("MasterTestCases", i, 1);
			
			//iterate all rows in TCModule sheet
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				
				String Description =xl.getCellData(TCModule, j, 0);
				String Object_Type = xl.getCellData(TCModule, j, 1);
				String Locator_Type = xl.getCellData(TCModule, j, 2);
				String Locator_Value = xl.getCellData(TCModule, j, 3);
				String TestData = xl.getCellData(TCModule, j, 4);
				reports = new ExtentReports("./ExtentReports/"+ Description+"  "+TCModule+FunctionLibrary.generateDate()+".html");
				test=reports.startTest(TCModule);
				test.assignAuthor("kashi");
				test.assignCategory("Functional");
				try {
					if(Object_Type.equalsIgnoreCase("startBrowser"))
					{
						driver =FunctionLibrary.startBrowser();
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("openApplication"))
					{
						FunctionLibrary.openApplication(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("ValidateTitle"))
					{
						FunctionLibrary.ValidateTitle(driver, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if (Object_Type.equalsIgnoreCase("mouseClick")) {
						FunctionLibrary.mouseClick(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if (Object_Type.equalsIgnoreCase("categoryTable")) {
						FunctionLibrary.categoryTable(driver, TestData);
						test.log(LogStatus.INFO, Description);
					}
					else if (Object_Type.equals("captureData")) {
						FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
						test.log(LogStatus.INFO, Description);
					}
					else if (Object_Type.equalsIgnoreCase("supplierTable")) {
						FunctionLibrary.supplierTable(driver);
						test.log(LogStatus.INFO, Description);
					}
					//write as pass in status cell
					xl.setCellData(TCModule, j, 5, "Pass", outputpath);
					test.log(LogStatus.PASS, Description);
					ModuleStatus="True";
				}catch(Exception e)
				{
					System.out.println(e.getMessage());
					//write as Fail in status cell
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					test.log(LogStatus.FAIL, Description);
					ModuleStatus="False";
					//Take screenshot after failing script
					//File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					
					//FileUtils.copyFile(scrFile, new File("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".png"));
										
					//String image = test.addScreenCapture("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".png");
										
					//test.log(LogStatus.FAIL, image);
					//break;
				}
				 if(ModuleStatus.equalsIgnoreCase("True"))
				 {
					xl.setCellData("MasterTestCases", i, 3, "Pass", outputpath); 
				 }
				 else
				 {
					 xl.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
				 }
			}
			reports.endTest(test);
			reports.flush();
			
		}
		else
		{
			//which test case flag to N Write as Blocked into status cell
			xl.setCellData("MasterTestCases", i, 3, "Blocked", outputpath);
		}
	}
}
}
