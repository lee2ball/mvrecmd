package computation;
import java.sql.ResultSet;
import movierec.DB;
import movierec.FUN;
import movierec.SYS;
public class ItemBasedSim {
	/**
	 * @param args
	 */
	public int threadNum=5;
	public int sleepTime=3000;//ms
	public double thresholdRating=2.5;
	public int recNum=50;//the num of movies to recommend,which need to be computed
	public int RuNNum=20;//
	public Thread thCom[];
	DB recSimdb=null;
	//public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("test");
		/*
		long startTime = System.currentTimeMillis();
		System.out.println(fun.getDateTime()+" starts...");
		itemBasedSim ibs=new itemBasedSim (5);
		//ibs.sim(3,6);
		//System.out.println("sim:"+ibs.sim(5,9));
		ibs.compRecDeg();
		//ibs.compRecDegUI(1,1);
		long finishTime = System.currentTimeMillis();
		System.out.println(fun.getDateTime()+" finishs,spent:"+(finishTime-startTime)+"ms");
		*/
		//System.out.println("spent:"+(finishTime-startTime)+"ms");
		/*startTime = System.currentTimeMillis();
		ibs.compRecDegUI(2,1);
		finishTime = System.currentTimeMillis();
		System.out.println("spent:"+(finishTime-startTime)+"ms");*/
	//}//main()
	public ItemBasedSim(int thNum){
		threadNum=thNum;
		thCom=new Thread[threadNum];
	}//itemBasedSim()
	public void compRecDeg(){
		//SELECT avg(rating) FROM rating,item WHERE mid=itemid and war=1 group by usrid
		DB uiddb=new DB();	
		recSimdb=new DB();
		ResultSet uidItems=null;	
		String sql="delete FROM avgrating";
		uiddb.executeUpdate(sql);
		sql="insert into avgrating SELECT usrid,avg(rating) avgRating FROM rating group by usrid";
		uiddb.executeUpdate(sql);
		//sql="delete FROM sim";
		//uiddb.executeUpdate(sql);
		sql="delete FROM recom";
		uiddb.executeUpdate(sql);
		sql="SELECT uid FROM user group by uid";
		uidItems=uiddb.executeQuery(sql);	
		for(int i=0;i<threadNum;i++){
			thCom[i]=new Thread(new thComp(uidItems,i));	
			thCom[i].start();
		}//for i
		boolean thFinished=false;
		while(!thFinished){
			try{
				Thread.sleep(sleepTime);
			}catch(Exception e){//try
				e.printStackTrace();
			}//catch();
			thFinished=true;
			for(int i=0;i<threadNum;i++){
				if(thCom[i].isAlive()){
					//System.out.println(thCom[i].getName()+":"+thCom[i].getState());
					thFinished=false;
					break;
				}//if
			}//for i
		}//while(!thFinished)
		recSimdb.close_state();
		recSimdb.close_connect();
		uiddb.close_result();
		uiddb.close_state();
		uiddb.close_connect();
	}//compRecDeg()
	public double compRecDegUI(int uid,int iid,int thID,DB mrdb,DB mydb,DB uiddb){
		//db mrdb=new db();
		String mrsql="SELECT itemid,rating FROM rating WHERE usrid='"+uid+"'";
		mrsql+=" and rating>='"+this.thresholdRating+"' limit 0,"+this.RuNNum;
		//need to 
		ResultSet mrItems=null;
		double simUI=0;
		double fz=0;
		double fm=0;
		double pui=0;
		//System.out.println("uid="+uid+"  , itemid= "+iid+" , threadID="+thID);
		mrItems=mrdb.executeQuery(mrsql);
		try{
			while(mrItems.next()){				
				simUI=sim(iid,mrItems.getInt("itemid"),thID,mydb,uiddb);
				fz+=simUI*mrItems.getInt("rating");
				fm+=Math.abs(simUI);
				//System.out.println(uid+","+iid+"----------2");
			}//while(uidItems.next())
			if(fm!=0)
				pui=fz/fm;
			else
				pui=0;
			//System.out.println("----------3");
		}catch(Exception e){
			e.printStackTrace();
		}
		return pui;
	}//comRecDegUI()
	public double sim(int midi,int midj,int thID,DB mydb,DB uiddb){
		//db mydb=new db();
		//db uiddb=new db();
		ResultSet mItems=null;
		ResultSet uidItems=null;
		String sql="SELECT sim FROM sim WHERE midi='"+midi+"' and midj='"+midj+"' or midi='"+midj+"' and midj='"+midi+"'";	
		uidItems=uiddb.executeQuery(sql);
		//select and fllaowing insert may have problems
		try{
			if(uidItems.next()){
				return uidItems.getDouble("sim");
			}//if(uidItems.next())
		}catch(Exception e){
			e.printStackTrace();
		}//catch()
		///String sqlUid="SELECT usrid FROM rating GROUP BY usrid ";
		String sqlUid="SELECT usrid,avgRating FROM avgrating ";
		//maybe there are users who has not commented any movie
		uidItems=uiddb.executeQuery(sqlUid);	
		int uid=0;
		double ru=0;
		double rui=0;
		double ruj=0;
		double mid1=0;//(rui-ru)(ruj-ru)
		double mid2=0;//pow((rui-ru),2);
		double mid3=0;//pow((ruj-ru),2);
		try{	
			while(uidItems.next()){	
				ru=0;
				rui=0;
				ruj=0;
				uid=uidItems.getInt("usrid");
				ru=uidItems.getDouble("avgRating");		
				sql="SELECT rating FROM rating WHERE usrid='"+uid+"' AND itemid ='"+midi+"'";
				mItems= mydb.executeQuery(sql);
				if(mItems.next()){
					rui=mItems.getDouble("rating");
				}//if(mitems.next())			
				sql="SELECT rating FROM rating WHERE usrid='"+uid+"' AND itemid ='"+midj+"'";
				mItems= mydb.executeQuery(sql);	
				if(mItems.next()){
					ruj=mItems.getDouble("rating");
				}//if(mitems.next())
				//System.out.println("RU="+ru);
				//System.out.println("RUI="+rui);
				//System.out.println("RUJ="+ruj);
				mid1+=(rui-ru)*(ruj-ru);
				mid2+=Math.pow(rui-ru, 2);
				mid3+=Math.pow(ruj-ru,2);
			}//while(uidItems.next())
			mid2=Math.sqrt(mid2)+Math.sqrt(mid3);
			if(mid2!=0){
				mid3=mid1/mid2;
			}else
				mid3=0;
		}catch(Exception e){
			e.printStackTrace();
		}//catch
		sql="INSERT INTO sim (midi, midj, sim,timestamp) VALUES ('"+midi+"','"+midj+"','"+mid3+"','";
		sql+=System.currentTimeMillis()+thID+Math.random()+"')";
		//mydb.executeUpdate(sql);
		//insert may have problems ,because some select no sim at the same time
		//but have unique index
		try{
			insertSim(sql);
		}catch(Exception e){
			//
		}//catch
		return mid3;		
	}//sim()
	public synchronized int getUserID(ResultSet uidItems){
		int uid=0;//0 shows that no usrid needs to process
		try{
			if(uidItems.next()){
				uid=uidItems.getInt("uid");
			}//if
		}catch(Exception e){
			e.printStackTrace();
		}//catch
		return uid;
	}//getUserID()
	public synchronized void insertRecom(String sql){
		recSimdb.executeUpdate(sql);
	}//insertRecom
	public synchronized void insertSim(String sql){
		//here may have some problems
		recSimdb.executeUpdate(sql);
	}//insertSim
	
	class thComp implements Runnable{	
		ResultSet uidItems=null;	
		int thID=0;	
		int gNum[]=new int[SYS.genre.length];
		int gNumS=0;
		public void run(){
			int uid=0;
			int mid=0;
			String isql="";
			String rsql="";
			//String orderCol[]={"id","usrid","itemid","rating","timestamp"};
			double recDeg=0;
			DB middb=new DB();
			DB mrdb=new DB();
			DB mydb=new DB();
			DB uiddb=new DB();
			ResultSet midItems=null;
			int midNum=0;
			int i=0;
			int hasRecIid[] = new int[SYS.recNum*2+5];
			int hRNum=0;
			while(0!=(uid=getUserID(uidItems))){
				//here should compute the user's intersting to decide the num of each 
				//kind to recommend
			    hRNum=0;
				gNumS=0;
				for(i=0;i<SYS.genre.length;i++){		
					gNum[i]=0;
					isql="SELECT avg(rating) avgR FROM rating,item WHERE mid=itemid and "+SYS.genre[i]+"=1 and usrid='"+uid+"'";
					midItems=middb.executeQuery(isql);
					try{
						if(midItems.next())
						gNum[i]=(int) midItems.getDouble("avgR");
					}catch(Exception e){
						e.printStackTrace();
					}//catch
					gNumS+=gNum[i];
				}//for i
				if(gNumS>0){				
					for(i=0;i<SYS.genre.length;i++){
						midNum=(int) (gNum[i]/(gNumS*1.0)*SYS.recNum*2);
						if(midNum>=1){
							isql="SELECT itemid FROM rating,item WHERE mid=itemid and "+SYS.genre[i]+"=1 and usrid='"+uid+"' and rating>='"+thresholdRating;
							isql+="' group by itemid order by rating desc limit 0,"+midNum;
							//System.out.println(isql);
							//各类间可能会有重复的item							
							midItems=middb.executeQuery(isql);
							try{
								while(midItems.next()){
									//System.out.println("----");
									recDeg=0;
									mid=midItems.getInt("itemid");
									if(hasRec(hasRecIid,hRNum,mid))
										continue;
									hasRecIid[hRNum++]=mid;
									//System.out.println(thID+":("+hRNum+"):"+uid+","+mid);
									recDeg=compRecDegUI(uid,mid,thID,mrdb,mydb,uiddb);//very cost time
									rsql="INSERT INTO recom (id, uid, mid, recdeg) VALUES (NULL,'"+uid+"','"+mid+"','"+recDeg+"')";
									//System.out.println(rsql);
									insertRecom(rsql);
								}//while(iidItems.next())
							}catch(Exception e){
								e.printStackTrace();
							}//catch()
						}//if(midNum>=1)
					}//for
				}else{
					isql="SELECT itemid FROM rating  where usrid!='"+uid+"' and rating>="+thresholdRating+" group by itemid order by rating desc";
					isql+=" limit 0,"+SYS.recNum*2;
					midItems=middb.executeQuery(isql);
					try{
						while(midItems.next()){
							recDeg=0;
							mid=midItems.getInt("itemid");
							recDeg=compRecDegUI(uid,mid,thID,mrdb,mydb,uiddb);
							rsql="INSERT INTO recom (id, uid, mid, recdeg) VALUES (NULL,'"+uid+"','"+mid+"','"+recDeg+"')";
							//System.out.println(rsql);
							insertRecom(rsql);
						}//while(iidItems.next())
					}catch(Exception e){
						e.printStackTrace();
					}//catch()
				}//else
			}//while(0!=(uid=getUserID(uidItems)))
			middb.close_result();
			middb.close_state();
			middb.close_connect();
			uiddb.close_result();
			uiddb.close_state();
			uiddb.close_connect();
			mrdb.close_result();
			mrdb.close_state();
			mrdb.close_connect();
		}//run()
		public thComp(ResultSet uidItems,int thID){
			this.uidItems=uidItems;
			this.thID=thID;
		}//thComp
		public boolean hasRec(int hasRecIid[],int hRNum,int iid){
			boolean flag=false;
			for(int i=0;i<hRNum;i++)
				if(hasRecIid[i]==iid){
					flag=true;
					break;
				}//if(hasRecIid[i]==iid)
			return flag;
		}
	}//class cdatatodb
}
