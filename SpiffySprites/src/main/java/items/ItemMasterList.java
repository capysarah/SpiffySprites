package items;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class ItemMasterList {

	private ArrayList<Item> itemList = new ArrayList<>();
	private static ItemMasterList instance = null;
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	private ItemMasterList() {

		itemList.add(new Item("Melon", "Looks juicy and fresh.", 10, 10, new Image("/assets/items/0.png"), 0));
		itemList.add(new Item("Pasta", "Plain buttered pasta.", 20, 30, new Image("/assets/items/1.png"), 1));
		itemList.add(new Item("Club sandwich", "Comes with a side of chips!", 30, 50, new Image("/assets/items/2.png"), 2));
		itemList.add(new Item("Chicken fingers", "Disliked by nobody, ever.", 40, 100, new Image("/assets/items/3.png"), 3));
		itemList.add(new Item("Hamburger", "Big ol' hamburger.", 50, 200, new Image("/assets/items/4.png"), 4));
	}
/////////////////////////////////////////////////////////////////////////////////////////////////	
	public static ItemMasterList getInstance() {
		if(instance == null) {
			instance = new ItemMasterList();
		}
		return instance;
	}	
	public Item getItem(int index) {
		return itemList.get(index);
	}	
	public ArrayList<Item> getArray(){
		return itemList;
	}
	public int getTotal() {
		return itemList.size();
	}	
}