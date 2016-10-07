package arbitrage;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 

public class main extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Scanner in1;

	public static void main(String[] args) throws FileNotFoundException{
		
		ArrayList text = new ArrayList();
		textFiles(text);
		
		in1 = new Scanner(new FileReader((String) text.get(0)));
		Scanner in2 = new Scanner(new FileReader((String) text.get(1)));
		
		ArrayList date = new ArrayList();
		ArrayList<Double> s = new ArrayList<Double>();
		ArrayList<Double> k = new ArrayList<Double>();
		ArrayList<Double> r = new ArrayList<Double>();
		ArrayList<Double> sig = new ArrayList<Double>();
		ArrayList<Double> T = new ArrayList<Double>();
		
		while(in1.hasNextDouble()){
			date.add(in1.next());
			s.add(in1.nextDouble());
			k.add(in1.nextDouble());
			r.add(in1.nextDouble());
			sig.add(in1.nextDouble());
			T.add(in1.nextDouble());
		}
		
		double[] C2 = new double[s.size()]; 
		double[] P2 = new double[s.size()];
		int j=0;
		
		while(in2.hasNextDouble()){
			in2.next();
			C2[j] = in2.nextDouble();
			P2[j] = in2.nextDouble();
			j++;
		}
		
		double[] d1 = new double[s.size()];
		double[] d2 = new double[s.size()];
		
		for(int i=0; i<s.size(); i++){
			double[] temp = new double[2];
			temp = calculateD1D2(s.get(i), k.get(i), r.get(i), sig.get(i), T.get(i));
			d1[i] = temp[0];
			d2[i] = temp[1];
		}
		
		double[] normalD1 = cnd1(d1);
		double[] normalD2 = cnd2(d2);
		double[] C = new double[s.size()];
		double[] P = new double[s.size()];
		
		for(int i=0; i<s.size(); i++){
			double[] temp = calculateCnP(s.get(i), k.get(i), r.get(i), sig.get(i), T.get(i), normalD1[i], normalD2[i]);
			C[i] = temp[0];
			P[i] = temp[1];
		}
		if(!text.isEmpty()){
			main1(date, C, P, C2, P2);
		}
	}
	
	private static void textFiles(final ArrayList text){
		final JFrame j1 = new JFrame();
		JButton b1 = new JButton("Open");
		JLabel l1 = new JLabel("Click here and open the option price");
		JPanel p1 = new JPanel();
		j1.setLayout(new BorderLayout(3,3));
		
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showSaveDialog(j1);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				    File yourFile = fc.getSelectedFile();
					text.add(yourFile.getAbsolutePath());
				}
			}
		});
		
		JButton b2 = new JButton("Open");
		JLabel l2 = new JLabel("Click here and open formulas' value"); 
		JPanel p2 = new JPanel();
		
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showSaveDialog(j1);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				    File yourFile = fc.getSelectedFile();
					text.add(yourFile.getAbsolutePath());
				}
			}
		});
		
		j1.setSize(400, 200);
		j1.setDefaultCloseOperation(j1.EXIT_ON_CLOSE);
		j1.setLayout(new BorderLayout());
		j1.setVisible(true);
		p1.add(l1);
		p1.add(b1);
		p2.add(l2);
		p2.add(b2);
		j1.add(p1, BorderLayout.NORTH);
		j1.add(p2, BorderLayout.SOUTH);
		
		System.out.println("here 1");
		while(text.size() != 2){
			System.out.println("choose files first");
			continue;
		}
	}
	
	private static void main1(final ArrayList date, final double[] C, final double[] P, final double[] C2, final double[] P2){
		
		JFrame j = new JFrame();
		j.setTitle("Arbitrage");
		j.setSize(400, 300);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setLayout(new GridLayout(3,1));
		
		JButton CallButton = new JButton("Call");
		JButton PutButton = new JButton("Put");
		JButton AnalyseButton = new JButton("Analyse");
		
		CallButton.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e){
				  DefaultListModel dlm = new DefaultListModel();
				  JList list = new JList(dlm);
				  JScrollPane scrollPane = new JScrollPane(list);
				  final JFrame j = new JFrame();
				  j.setTitle("Fair call prices");
				  j.setLayout(new BorderLayout(1, 2));
				  JButton b = new JButton("Okay");
				  
				  b.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						j.dispose();
					}  
				  });
				  
				  j.setSize(250, 500);
				  j.setDefaultCloseOperation(j.EXIT_ON_CLOSE);
				  j.setVisible(true);
				  
				  dlm.addElement("date       Call price");
				  dlm.addElement("    ");
				  for(int i=0; i<C.length; i++){
					  dlm.addElement(date.get(i)+"    "+C[i]);
				  }
				
				  j.add(scrollPane);
				  j.add(b, BorderLayout.EAST);
			  }
			});
		
		PutButton.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e){
				  DefaultListModel dlm = new DefaultListModel();
				  JList list = new JList(dlm);
				  JScrollPane scrollPane = new JScrollPane(list);
				  final JFrame j = new JFrame();
				  j.setTitle("Fair put prices");
				  j.setLayout(new BorderLayout(5, 5));
				  JButton b = new JButton("Okay");
				  
				  b.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						j.dispose();		
					}				  
				  });
				  
				  j.setSize(250, 500);
				  j.setDefaultCloseOperation(j.EXIT_ON_CLOSE);
				  j.setVisible(true);
				  
				  dlm.addElement("date       Put price");
				  dlm.addElement("   ");
				  for(int i=0; i<C.length; i++){
					  dlm.addElement(date.get(i)+"    "+P[i]);
				  }
					
				  j.add(scrollPane);
				  j.add(b, BorderLayout.EAST);
			  }
			});
		
		AnalyseButton.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e){
				  DefaultListModel dlm = new DefaultListModel();
				  JList list = new JList(dlm);
				  JScrollPane scrollPane = new JScrollPane(list);
				  final JFrame j = new JFrame();
				  j.setTitle("Result Analysys");
				  j.setLayout(new BorderLayout(1, 5));
				  JButton b = new JButton("Okay");
				  
				  b.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						j.dispose();
					}
				  });
				  
				  j.setSize(450, 450);
				  j.setDefaultCloseOperation(j.EXIT_ON_CLOSE);
				  j.setVisible(true);
				  
				  for(int i=0; i<C.length; i++){
					  if(C[i]==C2[i] && P[i]==P2[i])
						  dlm.addElement(date.get(i)+"    The prices equal to the fair values, hence no arbitrage");
					  else
						  dlm.addElement(date.get(i)+"     The prices not equal to the fair values, hence arbitrage");
				  }
				  
				  j.add(scrollPane);
				  j.add(b, BorderLayout.EAST);
			  }
			});
		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		
		JLabel CallLabel = new JLabel("Click here to calculate fair call price");
		JLabel PutLabel = new JLabel("Click here to calculate fair put price");
		JLabel AnalyseLabel = new JLabel("Click here to analyse the fair value and actual value");
		
		p1.add(CallLabel);
		p1.add(CallButton);
		p2.add(PutLabel);
		p2.add(PutButton);
		p3.add(AnalyseLabel);
		p3.add(AnalyseButton);
		
		j.add(p1);
		j.add(p2);
		j.add(p3);
		
		j.setVisible(true);
	}

	public static double[] calculateD1D2(double s, double k, double r, double sig, double T){
		double temp1 = 0, temp2 = 0, temp3 = 0;
		double[] ret = new double[2];
		temp1 = Math.log(s/k) + ((r + (sig*sig)/2)*(T));
		temp2 = (sig*(Math.sqrt(T)));
		temp3 = (sig*(Math.sqrt(T)));
		ret[0] = (temp1/temp2);
		ret[1] = ((temp1/temp2)-temp3);
		return ret;
	}
	
	public static double[] cnd1(double[] d1s){
		double[] l1s = new double[d1s.length];
		double[] k1s = new double[d1s.length];
		double[] w1s = new double[d1s.length];
		Double a1=0.31938153, a2=-0.356563782, a3=1.781477937,a4=-1.821255978,a5=1.330274429;
		for(int	i=0; i<d1s.length; i++){
			l1s[i] = Math.abs(d1s[i]);
			k1s[i] = 1.0/(1.0+0.2316419*l1s[i]);
			w1s[i] = 1.0-1.0/Math.sqrt(2.0*Math.PI)*Math.exp(-l1s[i]*l1s[i]/2)*(a1*k1s[i]+a2*k1s[i]*k1s[i]+a3*Math.pow(k1s[i],3)+a4*Math.pow(k1s[i],4)+a5*Math.pow(k1s[i],5));
			if(d1s[i]<0.0){
				w1s[i]=1.0-w1s[i];
			}
		}
	return w1s;
	}
	
	public static double[] cnd2(double[] d2s){
		double[] l2s = new double[d2s.length];
		double[] k2s = new double[d2s.length];
		double[] w2s = new double[d2s.length];
		Double a1=0.31938153, a2=-0.356563782, a3=1.781477937,a4=-1.821255978,a5=1.330274429;
		for(int i=0; i<d2s.length; i++){
			l2s[i] = Math.abs(d2s[i]);
			k2s[i] = 1.0/(1.0+0.2316419*l2s[i]);
			w2s[i] = 1.0-1.0/Math.sqrt(2.0*Math.PI)*Math.exp(-l2s[i]*l2s[i]/2)*(a1*k2s[i]+a2*k2s[i]*k2s[i]+a3*Math.pow(k2s[i],3)+a4*Math.pow(k2s[i],4)+a5*Math.pow(k2s[i],5));
			if(d2s[i]<0.0){
				w2s[i]=1.0-w2s[i];
			}
		}
		return w2s;
	}
	
	public static double[] calculateCnP(double s, double k, double r, double sig, double T, double nd1, double nd2){
		double temp1 = 0;
		double[] temp = new double[2];
		temp1 = k*Math.exp((-r)*(T));
		temp[0] = s*nd1 - temp1*nd2;
		temp[1] = temp1 - s + temp[0];
		return temp;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
