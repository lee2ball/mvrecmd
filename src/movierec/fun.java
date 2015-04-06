package movierec;

import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class fun {
	public static void showTip(String mes,String title){//弹出对话框窗口，进行操作提示
		JOptionPane.showMessageDialog(null, mes, title,JOptionPane.INFORMATION_MESSAGE);
	}
	public static boolean isInt(String str){//判断数量格式是否是正整数
		Pattern   p  =   null; 
		Matcher   m   =   null; 
		String   mod  =   "^[1-9]+[0-9]*$";
		p   =   Pattern.compile(mod); 
		m   =   p.matcher(str); 
		if(m.matches()) 
			return   true; 
		else 
			return   false;
	}
	public static String getDateTime(){
		Date time = new Date();  
		DateFormat df = DateFormat.getDateTimeInstance();  
		return df.format(time);  
	}
}
