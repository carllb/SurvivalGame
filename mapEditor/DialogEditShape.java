package mapEditor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;

import engine.Color3f;
import engine.PhysObj;
import engine.ShapeGenarator;


public class DialogEditShape extends JFrame implements KeyListener,ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 172009117226141818L;
	
	public MapEditor me;
	JLabel llx = new JLabel("X:");
	JLabel lly = new JLabel("Y:");
	JLabel llz = new JLabel("Z:");
	JLabel lsx = new JLabel("Size X:");
	JLabel lsy = new JLabel("Size Y:");
	JLabel lsz = new JLabel("Size Z:");
		
	JTextArea sx = new JTextArea("0");
	JTextArea sy = new JTextArea("0");
	JTextArea sz = new JTextArea("0");
	JTextArea ssx = new JTextArea("10");
	JTextArea ssy = new JTextArea("10");
	JTextArea ssz = new JTextArea("10");
	
	JColorChooser jcc = new JColorChooser();
	
	JButton changeColor = new JButton("Change Color");
	JButton ok = new JButton("OK");
	
	public PhysObj currentShape;
	
	public DialogEditShape(String arg0,MapEditor me){
		super(arg0);
		this.me = me;
		llx.setLocation(0, 0);     
		lly.setLocation(0, 40);
		llz.setLocation(0, 80);		
		lsx.setLocation(0, 120);
		lsy.setLocation(0, 160);
		lsz.setLocation(0, 200);
		sx.setLocation(40, 0);
		sy.setLocation(40, 40);
		sz.setLocation(40, 80);		
		ssx.setLocation(70, 120);
		ssy.setLocation(70, 160);
		ssz.setLocation(70, 200);
		changeColor.setLocation(0,250);
		
		llx.setSize(30, 10);
		lly.setSize(30, 10);
		llz.setSize(30, 10);		
		lsx.setSize(60, 10);
		lsy.setSize(60, 10);
		lsz.setSize(60, 10);				
		sx.setSize(30,15);
		sy.setSize(30,15);
		sz.setSize(30,15);		
		ssx.setSize(30,15);
		ssy.setSize(30,15);
		ssz.setSize(30,15);
		changeColor.setSize(184, 30);
		
		sx.addKeyListener(this);
		sy.addKeyListener(this);
		sz.addKeyListener(this);		
		ssx.addKeyListener(this);
		ssy.addKeyListener(this);
		ssz.addKeyListener(this);
		changeColor.addActionListener(this);
		
		
		add(llx);
		add(lly);
		add(llz);
		add(lsx);
		add(lsy);
		add(lsz);
		add(sx);
		add(sy);
		add(sz);
		add(ssx);
		add(ssy);
		add(ssz);
		add(changeColor);
		
	}
	public void setVisible(boolean b,PhysObj s) {
		currentShape = s;
		if(s != null){
			sx.setText(String.valueOf(s.location.x));
			sy.setText(String.valueOf(s.location.y));
			sz.setText(String.valueOf(s.location.z));		
			ssx.setText(String.valueOf(s.size.x));
			ssy.setText(String.valueOf(s.size.y));
			ssz.setText(String.valueOf(s.size.z));
		}
		super.setVisible(b);
	}
	@Override
	public void keyPressed(KeyEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent arg0) {
		try{
			//currentShape.color = new Color3f(0,0,1);				
			ShapeGenarator.resizePhysShape(currentShape, new Vector3f(Float.parseFloat(ssx.getText()),Float.parseFloat(ssy.getText()),Float.parseFloat(ssz.getText())));
			currentShape.movePhysObj(Double.parseDouble(sx.getText()), Double.parseDouble(sy.getText()), Double.parseDouble(sz.getText()));
			currentShape.collisionShape = new BoxShape(currentShape.size.asVector3f());
		}catch (Exception e){
		//	e.printStackTrace();
		//		if(e.getMessage() == "empty String" || e.getMessage() == 'input string: "-"') return;
		/*		
		//	if(sx.getText() != " " || sx.getText() != "0.")
				sx.setText("0.0");
		//	if(sy.getText() != " " || sy.getText() != "0.")
				sy.setText("0.0");
		//	if(sz.getText() != " " || sz.getText() != "0.")
				sz.setText("0.0");
		//	if(ssx.getText() != " " || ssx.getText() != "0.")
				ssx.setText("0.0");
		//	if(ssy.getText() != " " || ssy.getText() != "0.")
				ssy.setText("0.0");
		//	if(ssz.getText() != " " || ssz.getText() != "0.")
				ssz.setText("0.0");
				*/
		}
		//System.out.println(ssy.getText());
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	public void actionPerformed(ActionEvent e) {
		if(changeColor == (JButton)e.getSource() ){
			Color c = JColorChooser.showDialog(jcc, "MapEditor -- Color Shape", new Color(255, 0, 0));
			float r,g,b;
			r = c.getRed();
			g = c.getGreen();
			b = c.getBlue();
			r /= 255f;
			g /= 255f;
			b /= 255f;
			currentShape.color = new Color3f(r,g,b);
		}
	}
}
