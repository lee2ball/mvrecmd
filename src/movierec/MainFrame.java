package movierec;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MainFrame extends JFrame implements ActionListener,WindowListener{
	public JPanel panel_head=new JPanel(),panel_foot=new JPanel();
	public JPanel panelContent=new JPanel();
	public Container con=getContentPane();
	public int width=900;
	public int hight=620;
	public DB mydb=new DB();
	public JLabel foot=new JLabel("Powered By Andy Pan  2015-2-2",SwingConstants.CENTER);
	public JLabel optionSearch=new JLabel("",SwingConstants.CENTER);//?????????
	public MainFrame(){
		init();
	}//mainFrame()
	public void init(){
		addWindowListener(this);//?????????????
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screensize=tk.getScreenSize();
		setLocation(screensize.width/5,screensize.height/15);
		setSize(width,hight);
		setTitle("MovieRecommender"); 
		setResizable(false);
		setVisible(true);
		
		foot.setFont(new Font("????",Font.BOLD,18));
		panel_foot.add(foot);
		//
		panel_head.setBackground(Color.blue);
		optionSearch.setText("Let me find your favourite movies,so you just enjoy them!");
		optionSearch.setFont(new Font("????",Font.BOLD,20));		
		
		panel_head.add(optionSearch);
		con.add(panel_head,"North");
		//
		
		con.add(panelContent,"Center");
		con.add(panel_foot,"South");
		SelectInfo sh=new SelectInfo(mydb,panelContent,"Recommend");
	}//init()
	public void actionPerformed(ActionEvent e){
		//System.out.print(e.getActionCommand());
		if(e.getActionCommand().equals("??????")){//??????
			dispose();
			System.exit(0);
		}

	}//actionPerformed
	public void windowClosing(WindowEvent e){
		dispose();
		System.exit(0);
	}
	public void  windowOpened(WindowEvent e){
	}
	public void  windowClosed(WindowEvent e){
	}
	public void  windowActivated(WindowEvent e){	
	}
	public void  windowIconified(WindowEvent e){	
	}
	public void  windowDeiconified(WindowEvent e){	
	}
	public void  windowDeactivated(WindowEvent e){	
	}
}
