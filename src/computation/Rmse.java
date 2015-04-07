package computation;

import java.sql.ResultSet;

import movierec.DB;
import movierec.SYS;

public class Rmse {

	/**
	 * @param args
	 */
	public double getRmse(){
		DB recdb=new DB();
		ResultSet rItems=null;
		String sql="select count(id) num from recom ";
		int num=0;
		double m=0;
		double rmseMid=0;
		double mid=0;
		double wi=8.324;
		rItems=recdb.executeQuery(sql);
		try{
			if(rItems.next())
				num=rItems.getInt("num");
			
		}catch(Exception e){
			e.printStackTrace();
		}//catch
		sql="select avg(recdeg) avgr from recom ";
		rItems=recdb.executeQuery(sql);
		try{
			if(rItems.next())
				m=rItems.getInt("avgr");
			
		}catch(Exception e){
			e.printStackTrace();
		}//catch
		sql="select recdeg from recom ";
		rItems=recdb.executeQuery(sql);
		try{
			while(rItems.next()){
				mid=rItems.getDouble("recdeg");
				rmseMid+=Math.pow(mid-m,2)*SYS.wi;
			}//while
		}catch(Exception e){
			e.printStackTrace();
		}//catch	
		rmseMid/=num-1;
		rmseMid=Math.sqrt(rmseMid)*100;
		String midDeg=rmseMid+"";
		midDeg=midDeg.substring(0, midDeg.indexOf(".")+3);
		rmseMid=Double.parseDouble(midDeg);
		return rmseMid;
		//System.out.println("RMSE="+midDeg+"%");
	}//getRmse()
}//class rmse
