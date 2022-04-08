package survivalGame;

import java.util.ArrayList;

public class Inventory {
	
	private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();
	int maxWeight;
	
	public Inventory(int maxWeight){	
			this.maxWeight = maxWeight;
	}
	public InventoryItem[] getArray(){
		InventoryItem[] temp = new InventoryItem[items.size()];
		items.toArray(temp);
		return temp;
	}
	

	public boolean addItem(InventoryItem item){
		int totalWeight = 0;
		for(int i=0;i<items.size();i++){
			totalWeight += items.get(i).item.weight;
		}
		if((item.item.weight + totalWeight)> maxWeight){
			return false;
		}
		items.add(item);
		return true;
	}
	
	public void removeItem(InventoryItem item){
		items.remove(item);
	}
}
