package mapEditor;

import java.awt.Canvas;

import javax.swing.JFrame;

import engine.BasicGame;

public class RenderWindow {
	public JFrame window = new JFrame("MapEditor -- Render Window");
	BasicGame bg;
	public RenderWindow(){
		Canvas c = new Canvas();
		c.setVisible(true);
		c.setSize(640,480);		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(640, 480);
		window.add(c);
		window.setVisible(true);
		bg = new MapEditor(640, 480, c);
	}

}
