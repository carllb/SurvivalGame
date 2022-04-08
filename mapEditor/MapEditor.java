package mapEditor;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.vecmath.Vector3f;

import mapGame.Map;

import org.lwjgl.input.Keyboard;

import com.bulletphysics.collision.shapes.BoxShape;

import engine.BasicGame;
import engine.GameManager;
import engine.PhysObj;
import engine.Shape;
import engine.ShapeGenarator;


public class MapEditor extends BasicGame implements ActionListener{

	public static final String FILE_PATH = "C:'\'LWJGL'\'LWJGLBrett'\'src";
	GameManager gm = new GameManager();
	JFrame controles = new JFrame("MapEditor -- World Editor");
	DialogEditShape des = new DialogEditShape("MapEditor -- Add Shape",this);
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("World");
	DefaultMutableTreeNode shapes = new DefaultMutableTreeNode("Shapes");
	DefaultMutableTreeNode models = new DefaultMutableTreeNode("Models");
	DefaultMutableTreeNode entities = new DefaultMutableTreeNode("Entities");
	public File mapFile;
	JFileChooser fc = new JFileChooser(FILE_PATH);
	JButton openFile = new JButton("Open Map");
	JButton saveFile = new JButton("Save Map");
	JButton addShape = new JButton("Add Shape");
	JButton editObject = new JButton("Edit");
	JButton deletObject = new JButton("Delete");
	JTree objectList = new JTree(top);
	Map map = new Map(world);
	
	public MapEditor(int width, int height, Canvas canvas) {
		super(width, height, canvas);
		gm.startGame(this, 60);		
	}
	
	public static void main(String[] args) {
		(new RenderWindow()).window.setResizable(false);
	}
	
	@Override
	public void preInit() {	
		JScrollPane treeView = new JScrollPane(objectList);
		map.world = world;
		controles.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controles.setSize(215, 400);
		controles.setVisible(true);
		controles.setLayout(null);
		controles.setResizable(false);
		des.setSize(200, 400);
		des.setLayout(null);
		des.setResizable(false);
		
		openFile.addActionListener(this);			
		saveFile.addActionListener(this);
		addShape.addActionListener(this);
		editObject.addActionListener(this);
		deletObject.addActionListener(this);
		
		openFile.setLocation(0, 0);		
		saveFile.setLocation(0, 50);	
		addShape.setLocation(0, 110);
		editObject.setLocation(100,170);
		deletObject.setLocation(0,170);
		treeView.setLocation(0, 200);
		
		openFile.setSize(new Dimension(200,50));
		saveFile.setSize(new Dimension(200,50));	
		addShape.setSize(new Dimension(200,50));
		editObject.setSize(100, 25);
		deletObject.setSize(100,25);
		treeView.setSize(200, 150);	
		
		
		
		controles.add(openFile);
		controles.add(saveFile);
		controles.add(addShape);	
		controles.add(editObject);
		controles.add(deletObject);
		controles.add(treeView);
		setUpObjectList();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(saveFile == (JButton) arg0.getSource()){
			fc.setCurrentDirectory(mapFile);
			int state = fc.showSaveDialog(this.controles);
			if(state == JFileChooser.APPROVE_OPTION){
				mapFile = fc.getSelectedFile();			
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(mapFile));
					oos.writeObject(map);				
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}else if(openFile == (JButton) arg0.getSource()){
			shapes.removeAllChildren();
			fc.setCurrentDirectory(mapFile);
			int state = fc.showOpenDialog(this.controles);
			if(state == JFileChooser.APPROVE_OPTION){
				mapFile = fc.getSelectedFile();
				world.visibleObjects.clear();				
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(mapFile));
					map = (Map) ois.readObject();			
					map.loadedFromFile(world,true);
					for(Shape s : map.mapShapes){
						DefaultMutableTreeNode sn = new DefaultMutableTreeNode("Shape " + (map.mapShapes.indexOf(s) + 1));
						DefaultMutableTreeNode sc = new DefaultMutableTreeNode(s);
						sn.add(sc);
						shapes.add(sn);						
					}
					objectList.updateUI();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Map file: " + mapFile.getPath() + " may be corrupt");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.err.println("Map file: " + mapFile.getPath() + " is corrupt");				
				}
			}
		}else if(addShape == (JButton) arg0.getSource()){
			PhysObj s = ShapeGenarator.genPhysShape(new Vector3f(1,1,1),new BoxShape(new Vector3f(1,1,1)), 0);
			map.addObject(s);
			des.setVisible(true,s);
			DefaultMutableTreeNode sn = new DefaultMutableTreeNode("Shape " + map.mapShapes.size());
			DefaultMutableTreeNode sc = new DefaultMutableTreeNode(s);
			sn.add(sc);
			shapes.add(sn);
			objectList.updateUI();
			
		}else if(editObject == (JButton) arg0.getSource()){
			TreePath cS = objectList.getSelectionPath();
			if(cS == null) return;			
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) cS.getLastPathComponent();
			try{if(currentNode.getChildAt(0) == null) return;}catch(Exception e){return;}
			DefaultMutableTreeNode temp = (DefaultMutableTreeNode) currentNode.getChildAt(0);
			try{
			PhysObj s = (PhysObj) temp.getUserObject();
			des.setVisible(true, s);
			}catch(Exception e){}		
		}else if(deletObject == (JButton) arg0.getSource()){
			TreePath cS = objectList.getSelectionPath();
			if(cS == null) return;			
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) cS.getLastPathComponent();
			try{if(currentNode.getChildAt(0) == null) return;}catch(Exception e){return;}
			DefaultMutableTreeNode temp = (DefaultMutableTreeNode) currentNode.getChildAt(0);
			try{
			PhysObj s = (PhysObj) temp.getUserObject();
			map.removeObject(s);
			shapes.remove(currentNode);
			objectList.updateUI();
			}catch(Exception e){}		
		}
	}
	double dSpeed = 1;
	double speed = dSpeed;
	double factor = Math.PI/180;
	double rotSpeed = 2;
	@Override
	public void tick() {	
		
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			speed = dSpeed/10;
		}else{
			speed = dSpeed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			camera.rotx -= rotSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			camera.rotx += rotSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			camera.roty -= rotSpeed;
		}			
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			camera.roty += rotSpeed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			camera.x -= Math.sin(camera.roty*factor)*speed;
			camera.y += Math.sin(camera.rotx*factor)*speed;
			camera.z += Math.cos(camera.roty*factor)*speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			camera.x += Math.sin(camera.roty*factor)*speed;
			camera.y -= Math.sin(camera.rotx*factor)*speed;
			camera.z -= Math.cos(camera.roty*factor)*speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			camera.y -= 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_T)){
			camera.y += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			camera.x -= Math.sin((camera.roty-90)*factor)*speed;
				
			camera.z += Math.cos((camera.roty-90)*factor)*speed;
		}
				
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			camera.x -= Math.sin((camera.roty+90)*factor)*speed;
					
			camera.z += Math.cos((camera.roty+90)*factor)*speed;
		}
	}

	
	public void setUpObjectList(){
		top.add(shapes);
	//	top.add(models);
	//	top.add(entities);
		objectList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
	}
}
