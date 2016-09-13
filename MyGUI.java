/**
 * @(#)MyGUI.java
 *
 *
 * @author 
 * @version 1.00 2016/9/9
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.SpinnerNumberModel;

public class MyGUI {
        
	private JFrame mainFrame; //mainFrame is the window that contains everything
	private JPanel mainPanel; //mainPanel is contained my mainFrame, and contains all UI components
	
	private int spinner01Value=100;
	private double spinner02Value=1.0;
	private double spinner03Value=0.9;
	private double spinner04Value=1.5;
	private double spinner05Value=0.001;
	private double spinner06Value=0.001;
	private double spinner07Value=0.000;
	private double spinner08Value=1.0;
	private double spinner09Value=100.0;
	private double spinner10Value;
	private double spinner11Value;
	private double spinner12Value;
	private double spinner13Value;
	private double spinner14Value;
	private boolean restartBool = false;
	private boolean updateBool = false;
	
    public MyGUI() {
    	prepareGUI();
    }
    public boolean getRestartBool(){
   		return restartBool;
   	}
   	public int getSpinner01Value(){
   		return spinner01Value;
   	}
   	public double getSpinner02Value(){
   		return spinner02Value;
   	}
   	public double getSpinner03Value(){
   		return spinner03Value;
   	}
   	public double getSpinner04Value(){
   		return spinner04Value;
   	}
   	public double getSpinner05Value(){
   		return spinner05Value;
   	}
   	public double getSpinner06Value(){
   		return spinner06Value;
   	}
   	public double getSpinner07Value(){
   		return spinner07Value;
   	}
   	public double getSpinner08Value(){
   		return spinner08Value;
   	}
   	public double getSpinner09Value(){
   		return spinner09Value;
   	}
   	public void setRestartBool(boolean inputBoolean){
   		restartBool=inputBoolean;
   	}
    private void prepareGUI(){
    	mainFrame = new JFrame("Simulation Settings");
    	mainFrame.setSize(500,400);
    	
    	mainFrame.addWindowListener(new WindowAdapter(){
    		public void windowClosing(WindowEvent windowEvent){
    			System.exit(0);
    		}
    	});
    	
    	mainPanel = new JPanel();
    	mainPanel.setLayout(new GridLayout(16,2));
    	
    	mainFrame.add(mainPanel);
    	mainFrame.setVisible(true);
    }
    public void createUIComponents(){
    	//Add all the UI Components
    	SpinnerNumberModel model01 = new SpinnerNumberModel( spinner01Value,  1,  Integer.MAX_VALUE,  1);
    	SpinnerNumberModel model02 = new SpinnerNumberModel( spinner02Value,  0,  Double.MAX_VALUE,  0.01);
   		SpinnerNumberModel model03 = new SpinnerNumberModel( spinner03Value,  0,  Double.MAX_VALUE,  0.01);
   		SpinnerNumberModel model04 = new SpinnerNumberModel( spinner04Value,  0,  Double.MAX_VALUE,  0.01);
   		SpinnerNumberModel model05 = new SpinnerNumberModel( spinner05Value,  0,  Double.MAX_VALUE,  0.0001);
   		SpinnerNumberModel model06 = new SpinnerNumberModel( spinner06Value,  -(Double.MAX_VALUE/2),  Double.MAX_VALUE,  0.0001);
   		SpinnerNumberModel model07 = new SpinnerNumberModel( spinner07Value,  -Double.MAX_VALUE,  Double.MAX_VALUE,  0.0001);
   		SpinnerNumberModel model08 = new SpinnerNumberModel( spinner08Value,  -Double.MAX_VALUE,  Double.MAX_VALUE,  0.01);
   		SpinnerNumberModel model09 = new SpinnerNumberModel( spinner09Value,  -Double.MAX_VALUE,  Double.MAX_VALUE,  0.01);
    	
    	JLabel label01 = new JLabel("Partcile Count");
    	mainPanel.add(label01);
    	
    	JSpinner spinner01 = new JSpinner(model01);
    	spinner01.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner01Value=(Integer)(((JSpinner)e.getSource()).getValue());
        	}
      	});

      	mainPanel.add(spinner01);
    	
    	JLabel label02 = new JLabel("Initial Mass");
    	mainPanel.add(label02);
    	
    	JSpinner spinner02 = new JSpinner(model02);
    	spinner02.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner02Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner02);
    	
    	JLabel label03 = new JLabel("Mass Variation");
    	mainPanel.add(label03);
    	
    	JSpinner spinner03 = new JSpinner(model03);
    	spinner03.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner03Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner03);
    	
    	JLabel label04 = new JLabel("Disk Radius");
    	mainPanel.add(label04);
    	
    	JSpinner spinner04 = new JSpinner(model04);
    	spinner04.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner04Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner04);
    	
    	JLabel label05 = new JLabel("\u0394Time");
    	mainPanel.add(label05);
    	
    	JSpinner spinner05 = new JSpinner(model05);
    	spinner05.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner05Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	spinner05.setEditor(new JSpinner.NumberEditor(spinner05, "0.0000"));
      	mainPanel.add(spinner05);
    	
    	JLabel label06 = new JLabel("Gravitational Constant");
    	mainPanel.add(label06);
    	
    	JSpinner spinner06 = new JSpinner(model06);
    	spinner06.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner06Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	spinner06.setEditor(new JSpinner.NumberEditor(spinner06, "0.0000"));
      	mainPanel.add(spinner06);
    	
    	JLabel label07 = new JLabel("Random Initial Velocity");
    	mainPanel.add(label07);
    	
    	JSpinner spinner07 = new JSpinner(model07);
    	spinner07.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner07Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	spinner07.setEditor(new JSpinner.NumberEditor(spinner07, "0.0000"));
      	mainPanel.add(spinner07);
    	
    	JLabel label08 = new JLabel("Initial Disk Rotation");
    	mainPanel.add(label08);
    	
    	JSpinner spinner08 = new JSpinner(model08);
    	spinner08.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner08Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner08);
    	
    	JLabel label09 = new JLabel("Central Particle Mass");
    	mainPanel.add(label09);
    	
    	JSpinner spinner09 = new JSpinner(model09);
    	spinner09.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner09Value=(Double)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner09);
    	
    	JLabel label10 = new JLabel("Label 10");
    	mainPanel.add(label10);
    	
    	JSpinner spinner10 = new JSpinner();
    	spinner10Value = 0;
    	spinner10.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner10Value=(Integer)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner10);
    	
    	JLabel label11 = new JLabel("Label 11");
    	mainPanel.add(label11);
    	
    	JSpinner spinner11 = new JSpinner();
    	spinner11Value = 0;
    	spinner11.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner11Value=(Integer)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner11);
    	
    	JLabel label12 = new JLabel("Label 12");
    	mainPanel.add(label12);
    	
    	JSpinner spinner12 = new JSpinner();
    	spinner12Value = 0;
    	spinner12.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner12Value=(Integer)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner12);
    	
    	JLabel label13 = new JLabel("Label 13");
    	mainPanel.add(label13);
    	
    	JSpinner spinner13 = new JSpinner();
    	spinner13Value = 0;
    	spinner13.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner13Value=(Integer)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner13);
    	
    	JLabel label14 = new JLabel("Label 14");
    	mainPanel.add(label14);
    	
    	JSpinner spinner14 = new JSpinner();
    	spinner14Value = 0;
    	spinner14.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
            	spinner14Value=(Integer)(((JSpinner)e.getSource()).getValue());
        	}
      	});
      	mainPanel.add(spinner14);
      	
      	JButton updateButton= new JButton("Update values without restarting");
      	JButton restartButton= new JButton("Update values and restart");
      	updateButton.setActionCommand("update");
      	restartButton.setActionCommand("restart");
      	updateButton.addActionListener(new ButtonClickListener());
      	restartButton.addActionListener(new ButtonClickListener());
    	mainPanel.add(updateButton);
    	mainPanel.add(restartButton);
    	
    	
    	
      	
      	
      	
      	mainFrame.setVisible(true);
    }
    private class ButtonClickListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         String command = e.getActionCommand();  
         if( command.equals( "update" ))  {
            System.out.println("Update dummy!");
            System.out.println(spinner01Value);
         }
         else if( command.equals( "restart" ) )  {
            System.out.println("Restarting");
            restartBool = true;
            
         }
       }		
    }
  	
}
