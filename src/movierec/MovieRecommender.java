package movierec;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MovieRecommender {
	public static void main(String[] args) throws ParseException {
		File lockFile=new File(SYS.fileLock);
		  //System.out.println(now);
		if(!lockFile.exists()){
			//db toDB=new db();
			DataToDB dt=new DataToDB();
			dt.readDataToDB("u.data","	");
			dt.readDataToDB("u.genre","|");
			////dt.readDataToDB("u.info","	");//need to adjust a little
			dt.readDataToDB("u.item","|");//need to adjust much
			dt.readDataToDB("u.user","|");
			try{
				lockFile.createNewFile();
				System.out.println("Prepared job done!");
			}catch(Exception e){
				e.printStackTrace();
			}//catch
		}else{
				System.out.println("file.lock has been created. ");
				System.out.println("If you want to put data to database,please delete file.lock first!");
				MainFrame mf=new MainFrame();		
		}//else
	}
}
