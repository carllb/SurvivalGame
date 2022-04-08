package survivalGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import engine.GameManager;

public class SurvivalGameLauncher implements ActionListener{
	
	JFrame window = new JFrame("SurvivalGame Launcher");
	JButton startGame = new JButton("Start Game");
	JComboBox<DisplayMode> displayModes = new JComboBox<DisplayMode>();
	JCheckBox fullScreen = new JCheckBox("FullScreen");
	
	public static void main(String[] args) {	
		new SurvivalGameLauncher();
	}
	
	public SurvivalGameLauncher(){
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(640, 480);
		window.setLayout(null);
		int w = window.getBounds().width/2;
		int h = window.getBounds().height/2;
		window.setLocation(w, h);
		window.add(startGame);
		startGame.setSize(200,20);
		startGame.setLocation(640/2-120, 640-300);
		window.add(displayModes);
		displayModes.setSize(200,200);
		displayModes.setLocation(640/2-100, 0);
		DisplayMode[] dmodes = null;
		try {
			dmodes = Display.getAvailableDisplayModes();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		for(int i=0;i<dmodes.length;i++){
			displayModes.addItem(dmodes[i]);
		}
		window.add(fullScreen);
		fullScreen.setLocation(10, 10);
		fullScreen.setSize(100,20);
		startGame.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		DisplayMode dm = displayModes.getItemAt(displayModes.getSelectedIndex());
		System.out.println(dm);
		SurvivalGame game = new SurvivalGame(dm, null,fullScreen.isSelected());
		GameManager gm = new GameManager();
		game.gm = gm;
		window.dispose();
		gm.startGame(game, 100);
	}
}
