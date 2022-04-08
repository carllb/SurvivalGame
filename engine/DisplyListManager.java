package engine;


import org.lwjgl.opengl.GL11;

public class DisplyListManager {
	
	private boolean ntg = false;
	private int listId = 0;
	
	public int reqestNewListId(int timeout){
		int start = 0;
		ntg = true;
		while(ntg){
			try{
				Thread.sleep(5);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(start >= timeout) return 0;
			
			start += 5;			
		}
		
		return listId;
	}
	
	
	public void genLists(){
		if(ntg){
			listId = GL11.glGenLists(1);
			ntg = false;
		}	
	}
}
