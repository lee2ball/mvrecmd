package movierec;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class SelectInfo {
	public JButton button;
	public JLabel tId;
	public String des;
	public DB mydb;//数据库连接
	public DB mydb1;//数据库连接
	public JPanel panelContent,panel;//操作的面板
	public JRadioButton [] radio=new JRadioButton [3];
	public JTextField tf=new JTextField(5);
	public ButtonGroup group;
	public GridBagLayout gbl;//布局管理器
	public String sql="";//sql语句
	public SelectInfo(DB mydb,JPanel panelContent,String des){
		this.des=des;
		this.mydb=mydb;
		this.mydb1=new DB();
		this.panelContent=panelContent;
		tId=new JLabel("UserID:");
		tId.setFont(new Font("宋体",Font.BOLD,20));
		//init();
		if(des.equals("HaveCommented")){
			String headName[] = { "MovieName","Genre","ReleaseTime","Rating","RatingTime"};
			Object obj[][]=getHaveComInfo(-1);//-1 shows that not to show items
			infoInit(headName,obj,1,"");
		}else if(des.equals("Recommend")){//if
			String headName[] = { "MovieName","Genre","ReleaseTime","RecommendDegree"};
			Object obj[][]=getHaveComInfo(-1);//-1 shows that not to show items
			infoInit(headName,obj,0,"");			
		}
	}//selectInfo();
	public Object[][] getHaveComInfo(int uid) {
		int num=0;
		sql="select count(*) num from item,rating where itemid=mid and usrid ='"+uid+"'";
		ResultSet mItems= mydb.executeQuery(sql);
		ResultSet mItems1=null;
		try{
			if(mItems.next())
				num=mItems.getInt("num");
		}catch(Exception e){
			e.printStackTrace();
		}//catch()
		Object obj [][]=new Object [num][5];//table content
		sql="select itemid,title,releasedate,rating, timestamp from item,rating where itemid=mid and usrid ='"+uid+"' order by  rating desc,timestamp desc";
		mItems= mydb.executeQuery(sql);	
		int mid_cnum=0;
		String bDate="";
		String kind="";
		int itemId=0;
		try{
		while(mItems.next()){
			kind="";
			itemId=mItems.getInt("itemid");	
			obj[mid_cnum][0]=mItems.getString("title");	
			obj[mid_cnum][2]=mItems.getString("releasedate");
			obj[mid_cnum][3]=mItems.getInt("rating");
			bDate=mItems.getInt("timestamp")+"000";
			sql="select unknown,action,adventure,animation,children,comedy,";
			sql+="crime,documentary,drama,fantasy,filmnoir,horror,musical,";
			sql+="mystery,romance,scifi,thriller,war,western";
			sql+=" from item where mid='"+itemId+"'";
			mItems1= mydb1.executeQuery(sql);
			if(mItems1.next()){
				if(1==mItems1.getInt("unknown"))
					kind+="unknown|";
				if(1==mItems1.getInt("action"))
					kind+="action|";
				if(1==mItems1.getInt("adventure"))
					kind+="adventure|";
				if(1==mItems1.getInt("animation"))
					kind+="animation|";
				if(1==mItems1.getInt("children"))
					kind+="children|";
				if(1==mItems1.getInt("comedy"))
					kind+="comedy|";
				//"crime,documentary,drama,fantasy,filmnoir,horror,musical,"
				if(1==mItems1.getInt("crime"))
					kind+="crime|";
				if(1==mItems1.getInt("documentary"))
					kind+="documentary|";
				if(1==mItems1.getInt("drama"))
					kind+="drama|";
				if(1==mItems1.getInt("fantasy"))
					kind+="fantasy|";
				if(1==mItems1.getInt("filmnoir"))
					kind+="filmnoir|";
				if(1==mItems1.getInt("horror"))
					kind+="horror|";
				if(1==mItems1.getInt("musical"))
					kind+="musical|";
				//"mystery,romance,scifi,thriller,war,western";
				if(1==mItems1.getInt("mystery"))
					kind+="mystery|";
				if(1==mItems1.getInt("romance"))
					kind+="romance|";
				if(1==mItems1.getInt("scifi"))
					kind+="scifi|";
				if(1==mItems1.getInt("thriller"))
					kind+="thriller|";
				if(1==mItems1.getInt("war"))
					kind+="war|";
				if(1==mItems1.getInt("western"))
					kind+="western|";
			}//if(mItems1.next())
			obj[mid_cnum][1]=kind;	
			mItems1.close();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  	  
			obj[mid_cnum][4] = sdf.format(new Date(Long.parseLong( bDate)));  
			mid_cnum++;		
		}}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}//getHaveCommInfo
	public Object[][] getRecommendInfo(int uid) {
		int num=0;
		sql="select count(*) num from recom  where uid ='"+uid+"' and recdeg>='"+(SYS.recThreshold-0.5)+"' limit 0,"+SYS.recNum;
		ResultSet mItems= mydb.executeQuery(sql);
		try{
			if(mItems.next())
				num=mItems.getInt("num");
		}catch(Exception e){
			e.printStackTrace();
		}//catch()
		//{ "MovieName","Genre","ReleaseTime","RecommendDegree"};
		Object obj [][]=new Object [num][4];//表格显示内容数组
		sql="select mid,recdeg from recom  where uid ='"+uid+"' order by recdeg desc limit 0,15";
		mItems= mydb.executeQuery(sql);
		ResultSet mItems1=null;
		int mid_cnum=0;
		String kind="";
		String midDeg="";
		int itemId=0;
		double deg=0;
		try{
		while(mItems.next()){
			kind="";
			itemId=mItems.getInt("mid");	
			deg=mItems.getDouble("recdeg");
			midDeg=deg+"";
			midDeg=midDeg.substring(0, midDeg.indexOf(".")+4);
			deg=Double.parseDouble(midDeg);
			sql="select title,releasedate,unknown,action,adventure,animation,children,comedy,";
			sql+="crime,documentary,drama,fantasy,filmnoir,horror,musical,";
			sql+="mystery,romance,scifi,thriller,war,western";
			sql+=" from item where mid='"+itemId+"'";
			mItems1= mydb1.executeQuery(sql);		
			if(mItems1.next()){
				if(1==mItems1.getInt("unknown"))
					kind+="unknown|";
				if(1==mItems1.getInt("action"))
					kind+="action|";
				if(1==mItems1.getInt("adventure"))
					kind+="adventure|";
				if(1==mItems1.getInt("animation"))
					kind+="animation|";
				if(1==mItems1.getInt("children"))
					kind+="children|";
				if(1==mItems1.getInt("comedy"))
					kind+="comedy|";
				//"crime,documentary,drama,fantasy,filmnoir,horror,musical,"
				if(1==mItems1.getInt("crime"))
					kind+="crime|";
				if(1==mItems1.getInt("documentary"))
					kind+="documentary|";
				if(1==mItems1.getInt("drama"))
					kind+="drama|";
				if(1==mItems1.getInt("fantasy"))
					kind+="fantasy|";
				if(1==mItems1.getInt("filmnoir"))
					kind+="filmnoir|";
				if(1==mItems1.getInt("horror"))
					kind+="horror|";
				if(1==mItems1.getInt("musical"))
					kind+="musical|";
				//"mystery,romance,scifi,thriller,war,western";
				if(1==mItems1.getInt("mystery"))
					kind+="mystery|";
				if(1==mItems1.getInt("romance"))
					kind+="romance|";
				if(1==mItems1.getInt("scifi"))
					kind+="scifi|";
				if(1==mItems1.getInt("thriller"))
					kind+="thriller|";
				if(1==mItems1.getInt("war"))
					kind+="war|";
				if(1==mItems1.getInt("western"))
					kind+="western|";
			}//if(mItems1.next())
			obj[mid_cnum][1]=kind;	
			obj[mid_cnum][0]=mItems1.getString("title");	
			obj[mid_cnum][2]=mItems1.getString("releasedate");
			obj[mid_cnum][3]=deg;
			mItems1.close(); 
			mid_cnum++;		
		}}catch(Exception e){
			e.printStackTrace();
		}//catch()
		return obj;
	}//getRecommendInfo()
	public Object[][] getUserInfo(int uid) {
		int num=0;
		sql="select count(*) num from user  where uid ='"+uid+"'";
		ResultSet mItems= mydb.executeQuery(sql);
		try{
			if(mItems.next())
				num=mItems.getInt("num");
		}catch(Exception e){
			e.printStackTrace();
		}//catch()
		//{ "Name","Age","Gender","Occupation","ZipCode"};
		Object obj [][]=new Object [num][5];//表格显示内容数组
		sql="select age,gender,occupation,zip from user  where uid ='"+uid+"'";
		mItems= mydb.executeQuery(sql);
		int mid_cnum=0;
		try{
			while(mItems.next()){	
				obj[mid_cnum][0]=uid;
				obj[mid_cnum][1]=mItems.getInt("age");	
				obj[mid_cnum][2]=mItems.getString("gender");
				obj[mid_cnum][3]=mItems.getString("occupation");
				obj[mid_cnum][4]=mItems.getString("zip");
				mid_cnum++;		
			}//while
		}catch(Exception e){
				e.printStackTrace();
		}//catch()
		return obj;
	}//getRecommendInfo()	
	public void infoInit(String headName[],Object obj [][],int selected,String search){
		panelContent.removeAll();
		panelContent.updateUI();//更新面板
		panelContent.repaint();
		group=new ButtonGroup();
		tf=new JTextField(5);
		tf.setText(search);
		radio[0]=new JRadioButton("Recommend");
		radio[1]=new JRadioButton("HaveCommented");
		radio[2]=new JRadioButton("UserInfo");
		radio[selected].setSelected(true);
		JButton button=new JButton("Search");	
		button.setFont(new Font("宋体",Font.BOLD,20));
		radio[0].setFont(new Font("宋体",Font.BOLD,18));
		radio[1].setFont(new Font("宋体",Font.BOLD,18));
		radio[2].setFont(new Font("宋体",Font.BOLD,18));
		group.add(radio[0]);
		group.add(radio[1]);
		group.add(radio[2]);
		JTable table = new JTable(new MyTableModel(headName,obj));
		table.setDefaultRenderer(JButton.class, new ComboBoxCellRenderer());
		table.getTableHeader().setFont(new Font("宋体",Font.BOLD,16));
		table.setFont(new Font("宋体",Font.PLAIN,16));
		table.setRowHeight(30);
		//System.out.println(headName.length);
		DefaultTableCellRenderer cellrender = new DefaultTableCellRenderer();
        cellrender.setHorizontalAlignment(JTextField.CENTER);
        panelContent.setLayout(null);
		TableColumn []tc=new TableColumn [headName.length];
		for(int i=0;i<headName.length;i++){
			tc[i]=table.getColumn(headName[i]);//return TableColumn object
			tc[i].setCellRenderer(cellrender);//设置列的对齐方式
		}//for i
	     if(0==selected){
	        	tc[0].setPreferredWidth(250);
	        	tc[1].setPreferredWidth(250);
	        	tc[2].setPreferredWidth(100);
	        	tc[3].setPreferredWidth(100);
	      }else if(1==selected){
        	tc[0].setPreferredWidth(240);
        	tc[1].setPreferredWidth(240);
        	tc[2].setPreferredWidth(80);
        	tc[3].setPreferredWidth(20);
        	tc[4].setPreferredWidth(120);
        }else if(2==selected){
        	tc[0].setPreferredWidth(100);
        	tc[1].setPreferredWidth(100);
        	tc[2].setPreferredWidth(80);
        	tc[3].setPreferredWidth(250);
        	tc[4].setPreferredWidth(170);
        }//else if(2==selected){      
        JScrollPane sform=new JScrollPane(table);
		sform.setBounds(0, 100, 900, 420);
		gbl=new GridBagLayout();
		panel=new JPanel();
		panel.setLayout(gbl);	
		gridBagAdd(radio[0],1,0,1,1,0);
		gridBagAdd(radio[1],2,0,1,1,0);
		gridBagAdd(radio[2],3,0,1,1,0);
		gridBagAdd(tId,4,0,1,1,0);
		gridBagAdd(tf,5,0,1,1,0);		
		gridBagAdd(button,6,0,2,1,0);
		panel.add(radio[0]);
		panel.add(radio[1]);
		panel.add(radio[2]);
		panel.add(tId);
		panel.add(button);
		panel.add(tf);
		actionlistener al=new actionlistener();
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.addActionListener(al);
		panel.setBounds(0, 0, 900, 100);
		panelContent.add(panel);
		panelContent.add(sform);
		panelContent.updateUI();//更新面板
		panelContent.repaint();
	}//saleInit()
	public void gridBagAdd( Component comp,int x, int y, int width, int height,int right) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx=1;
		gbc.weighty=1;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets=new Insets(5,2,5,right);
		gbc.ipadx=5;
		gbc.ipady=5;
		gbl.setConstraints(comp, gbc);
	}	
	class actionlistener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("Search")){
				String midIdStr=tf.getText().trim();
				int midId;
				if(FUN.isInt(midIdStr))
					midId=new Integer (midIdStr);
				else{
					FUN.showTip("Search box's content must be a valid integer!", "Tip");
					return;
				}
				if(radio[1].isSelected()){	
						String headName[] = { "MovieName","Genre","ReleaseTime","Rating","RatingTime"};
						Object obj[][]=getHaveComInfo(midId);
						infoInit(headName,obj,1,midId+"");
				}else if(radio[0].isSelected()){//if(radio[1].isSelected())
					String headName[] = { "MovieName","Genre","ReleaseTime","RecommendDegree"};
					Object obj[][]=getRecommendInfo(midId);
					infoInit(headName,obj,0,midId+"");
					//fun.showTip("The module is under construction!", "Tip");
				}else if(radio[2].isSelected()){//if(radio[1].isSelected())
					String headName[] = { "UserID","Age","Gender","Occupation","ZipCode"};
					Object obj[][]=getUserInfo(midId);
					infoInit(headName,obj,2,midId+"");
					//fun.showTip("The module is under construction!", "Tip");
				}//else if
			}//if(e.getActionCommand().equals("Search"))
		}// actionPerformed(ActionEvent e)
	}//actionlistener implements ActionListener
}
