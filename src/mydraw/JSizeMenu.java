package mydraw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JSizeMenu extends JPanel {
	private	Dimension docSize;
	public JSizeMenu(Dimension currentSize) {
		super();
		// TODO Auto-generated constructor stub
		
		SpinnerModel heightModel =
		        new SpinnerNumberModel(currentSize.getHeight(), //initial value
		                               10, //min
		                               1000, //max
		                               1);                //step
		SpinnerModel widthModel =
		        new SpinnerNumberModel(currentSize.getWidth(), //initial value
		                               10, //min
		                               1000, //max
		                               1);                //step
		JSpinner heightSpinner = new JSpinner(heightModel);
		JSpinner widthSpinner = new JSpinner(widthModel);
		add(heightSpinner);
		add(widthSpinner);
		heightSpinner.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		JButton apply = new JButton("Apply");
		add(apply);
		apply.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Dimension size = new Dimension(((Double) widthSpinner.getValue()).intValue(),((Double) heightSpinner.getValue()).intValue());
				reportChange(size);
				
			}

		});
	}

	protected void reportChange(Dimension size) {
		// TODO Auto-generated method stub
		this.firePropertyChange("size", this.docSize ,size);
		this.docSize = size;
	}

	@Override 
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        Dimension size = this.getSize();
//        g.clipRect(0, 0, size.width , size.height);
//        g.drawImage(image, 0, 0, null);
        
   }
	
}
