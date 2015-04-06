package computation;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import movierec.fun;


public class compuFrame extends JFrame implements ActionListener,WindowListener{

	public JComboBox combox= null;
	public JPanel panel[]=new JPanel[3];
	public JLabel label[]=new JLabel[4];
	public JButton button=new JButton("Start");
	public JButton butRmse=new JButton("RMSE");
	public JTextArea tiptext=new JTextArea(10,40);
	public GridBagLayout gbl;
	public Container con;
	long startTime;
	long finishTime;
	/**
	 * @param args
	 */
	public static void main(String[] args){
		compuFrame cf =new compuFrame();
	}//main(String[] args)
	public compuFrame(){
		init();
	}
	public void init(){
		setTitle("MovieRecommender -- Compute Section");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screensize=tk.getScreenSize();
		setLocation(screensize.width/4,screensize.height/4);
		setSize(screensize.width/2,screensize.height/2);
		label[1]=new JLabel("Movie Recommender",SwingConstants.CENTER);
		label[1].setFont(new Font("����",Font.BOLD,30));
		label[2]=new JLabel("Powered By Andy Pan  2015-2-2",SwingConstants.CENTER);
		label[2].setFont(new Font("����",Font.BOLD,20));
		label[3]=new JLabel("Threads Number:");
		combox=new JComboBox();
		label[3].setFont(new Font("����",Font.BOLD,15));
		combox.setFont(new Font("����",Font.BOLD,15));
		panel[0]=new JPanel();
		panel[1]=new JPanel();
		panel[2]=new JPanel();
		panel[0].add(label[1]);
		gbl=new GridBagLayout();
		panel[1].setLayout(gbl);
		panel[1].setAlignmentY(CENTER_ALIGNMENT);
		for(int i=1;i<=8;i++){
			combox.addItem(i);
		}
		combox.setSelectedIndex(4);
		button.setFont(new Font("����",Font.BOLD,20));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		butRmse.setFont(new Font("����",Font.BOLD,20));
		butRmse.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gridBagAdd(label[3],1,0,1,1,0);
		gridBagAdd(combox,5,0,1,1,0);
		gridBagAdd(button,8,0,1,1,0);
		gridBagAdd(butRmse,9,0,1,1,0);
		tiptext.setEditable(false);
		textAppend("If you want to recompute recommended movies for users, click the Start button!");
		gridBagAdd(tiptext,1,3,10,1,0);

		panel[1].add(label[3]);
		panel[1].add(combox);
		panel[1].add(button);
		panel[1].add(butRmse);
		panel[1].add(tiptext);
		panel[2].add(label[2]);	
		con=getContentPane();
		con.add(panel[0],"North");
		con.add(panel[1],"Center");
		con.add(panel[2],"South");
		button.addActionListener(this);
		butRmse.addActionListener(this);
		addWindowListener(this);//window listen
		setResizable(false);
		setVisible(true);
	}
	public void textAppend(String str){
		this.tiptext.append(str+"\n");		
	}
	public void textClear(){
		this.tiptext.setText("");		
	}
	public void gridBagAdd( Component comp,int x, int y, int width, int height,int right) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets=new Insets(5,5,5,right);
		gbc.ipadx=1;
		gbc.ipady=1;
		gbl.setConstraints(comp, gbc);
	}
	public void setButtonStatus(boolean status){
		button.setEnabled(status);
		butRmse.setEnabled(status);
	}	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Start")){
			textClear();
			int thNum=Integer.valueOf(combox.getSelectedItem()+"");
			setButtonStatus(false);
			startTime = System.currentTimeMillis();
			textAppend(fun.getDateTime()+" Recompute starts:");
			textAppend("Recommended movies for users have been clear!");
			textAppend("Now is recomputing......Please wait......");
			Thread child=new Thread(new childProc(thNum));
			child.start();
		}else if(e.getActionCommand().equals("RMSE")){//if
			rmse rm=new rmse();
			fun.showTip(rm.getRmse()+"%", "RMSE");
		}//else
	}//actionPerformed()
	public void windowClosing(WindowEvent e){
		int Choose = JOptionPane.showConfirmDialog(this, "Do you really want to exit��", "Exit Tip", JOptionPane.YES_NO_OPTION);
		if (Choose == JOptionPane.YES_OPTION){	
			System.exit(1);
			dispose();
		}//if
	}//windowClosing()
	class childProc implements Runnable{
		int thNum;
		public void run(){
			itemBasedSim ibs=new itemBasedSim (thNum);
			ibs.compRecDeg();
			finishTime = System.currentTimeMillis();
			textAppend(fun.getDateTime()+" recompute finishs,spent:"+(finishTime-startTime)+"ms");
			setButtonStatus(true);
		}//run()
		public childProc(int thNum){
			this.thNum=thNum;
		}//childProc
	}//childProc

	public void  windowOpened(WindowEvent e){}
	public void  windowClosed(WindowEvent e){}
	public void  windowActivated(WindowEvent e){}
	public void  windowIconified(WindowEvent e){}
	public void  windowDeiconified(WindowEvent e){}
	public void  windowDeactivated(WindowEvent e){}	
}

