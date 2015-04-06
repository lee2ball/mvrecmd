package movierec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class db {	
	public String driver = "com.mysql.jdbc.Driver";
	public String url = "jdbc:mysql://localhost:3306/movierecommender";
	public String user = "root";
	public String password = "******";
	private Connection conn=null;
	private Statement statement=null;
	private ResultSet rs =null;
	public db(){
        try { 
            // ????????????
            Class.forName(driver);
            // ?????????
             conn = DriverManager.getConnection(url, user, password);
            //if(!conn.isClosed()) 
            // System.out.println("Succeeded connecting to the Database!");
            // statement???????SQL???
            statement = conn.createStatement();
        }catch(Exception e){
        	e.printStackTrace();
        }
	}//db()
	public db(String driver,String url,String user,String password){
		this.driver=driver;
		this.url=url;
		this.user=user;
		this.password=password;
        try { 
            // ????????????
            Class.forName(driver);
            // ?????????
             conn = DriverManager.getConnection(url, user, password);
            if(!conn.isClosed()) 
            // System.out.println("Succeeded connecting to the Database!");
            // statement???????SQL???
            statement = conn.createStatement();
        }catch(Exception e){
        	e.printStackTrace();
        }			
	}//db(String driver,String url,String user,String password)
	public void close_result(){
      try{
    	  if(!rs.isClosed()&&rs!=null)
    		  rs.close();
      }catch(Exception e){
    	  e.printStackTrace();
      }
	}
	public void close_state(){
	   try{
		   if(!statement.isClosed()&&statement!=null)
			   statement.close();
	    }catch(Exception e){
	     e.printStackTrace();
	    }		
	}
	public void close_connect(){
	      try{
	    	  if(!conn.isClosed()&&conn!=null)
	    		  conn.close();
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }		
	}
	public ResultSet executeQuery(String sql){
		try{
			rs= statement.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rs;
	}
	public void executeUpdate(String sql){
		try{
			statement.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
