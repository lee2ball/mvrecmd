package movierec;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;

public class DataToDB {

	/**
	 * @param args
	 */
	public void readDataToDB(String fname,String regex){
		String mstr="";
		String test[];
		DB mydb= new DB();
		String sql="";
		boolean flag=true;
		//u.data u.genre u.info u.item u.occupation u.user
		int num=0;
		try{
			FileInputStream fout = new FileInputStream(SYS.fileDir+fname);//("ml-100k/u.data");
			InputStreamReader reader = new InputStreamReader(fout, "utf8");
			BufferedReader   in   =   new   BufferedReader(reader);
			while((mstr=in.readLine())!=null){
				flag=true;
				if(fname.equals("u.data"))
					sql="INSERT INTO rating (id, usrid, itemid, rating, timestamp) VALUES (NULL";
				if(fname.equals("u.genre"))
					sql="INSERT INTO genre (id,genre,mid) VALUES (NULL";
				if(fname.equals("u.user"))
					sql="INSERT INTO user (id,uid,age,gender,occupation,zip) VALUES (NULL";	
				if(fname.equals("u.item"))
					sql="INSERT INTO item VALUES (NULL";	
				//if(fname.equals("u.info"))
					//sql=
				num++;
				mstr=mstr.replace(regex, "	");
				test=mstr.split("	");
				//System.out.println(mstr);
				//System.out.println(test.length);
				for(int i=0;i<test.length;i++){
					//System.out.print(test[i]+"--");
					//System.out.println("length:"+test.length);
					if(test[0].length()>0)
						sql+=",'"+test[i].replace("'", "\\'")+"'";
					else
						flag=false;
				}//
				System.out.println();
				sql+=")";
				if(flag)
					System.out.println(sql);
				if(flag)
					mydb.executeUpdate(sql);
				//System.out.println(mstr);
			}//while
			//System.out.println("----------------------");
			in.close();
			reader.close();
			fout.close();
			//mydb.close_result();
			mydb.close_state();
			mydb.close_connect();
		}catch(Exception e){
			e.printStackTrace();
		}//catch
		
		//System.out.println(num);
	}//readDataToDB
	//public static void main(String[] args) {
		// TODO Auto-generated method stub
		//dataToDB dtb = new dataToDB();
		//dtb.readDataToDB("u.data","	");
		//dtb.readDataToDB("u.genre","|");
		//dtb.readDataToDB("u.info","	");//need to adjust a little
		//dtb.readDataToDB("u.item","	");//need to adjust much
		//dtb.readDataToDB("u.user","|");	
		//u.data u.genre u.info u.item u.occupation u.user
		//item a little different
	//}//main
	
}//class dataToDB

