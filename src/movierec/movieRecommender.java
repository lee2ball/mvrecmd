package movierec;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class movieRecommender {
	public static void main(String[] args) throws ParseException {
		File lockFile=new File(sys.fileLock);
		  //System.out.println(now);
		if(!lockFile.exists()){
			//db toDB=new db();
			dataToDB dt=new dataToDB();
			dt.readDataToDB("u.data","	");
			dt.readDataToDB("u.genre","|");
			////dt.readDataToDB("u.info","	");//need to adjust a little
			dt.readDataToDB("u.item","|");//need to adjust much
			dt.readDataToDB("u.user","|");
			try{
				lockFile.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}//catch
		}else{
				System.out.println("file.lock has been created. ");
				System.out.println("If you want to put data to database,please delete file.lock first!");
				mainFrame mf=new mainFrame();		
		}//else
	}
}
