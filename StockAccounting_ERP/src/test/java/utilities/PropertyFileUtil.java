package utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFileUtil {
public static Properties conprop;
public static String getValueForKey(String key)throws Throwable
{
	conprop = new Properties();
	conprop.load(new FileInputStream("E:\\QEDGE\\Automation Testing\\Project_Rangareddy\\StockAccounting_ERP_1\\FileInput\\DataEngine.xlsx"));
	return conprop.getProperty(key);
}
}
