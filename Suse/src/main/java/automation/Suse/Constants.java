package automation.Suse;

public class Constants {

	public static String excelFileLocation=System.getProperty("user.dir")+"/src/main/java/SuseLinks.xlsx";
	public static String win_chromePath =System.getProperty("user.dir")+"./drivers/chromedriver.exe"; 
	
}

/*
int i=1;
for(;i<excelRowSize;i++)
{
	
	if(i>1)
	{
		for(int j=2;j<=204;j++)
		{   
			
			Main.urlsFromExcel.add(sheet.getRow(i).getCell(0).getStringCellValue());
			System.out.println("Url from Excel "+Main.urlsFromExcel.get(i-1));	
		}
	}
	else
		{   
		    Main.urlsFromExcel.add(sheet.getRow(i).getCell(0).getStringCellValue());
			System.out.println("Url from Excel "+Main.urlsFromExcel.get(i-1));
		}
		
}*/