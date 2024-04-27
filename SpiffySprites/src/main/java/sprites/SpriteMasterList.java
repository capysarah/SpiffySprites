package sprites;

import java.util.ArrayList;

import items.Item;
import items.ItemMasterList;
import javafx.scene.image.Image;

public class SpriteMasterList {
	
	private ArrayList<Sprite> spriteList = new ArrayList<>();
	private static SpriteMasterList instance;
	private int numUnlocked = 1;
	
/////////////////////////////////////////////////////////////////////////////////////////////////	
	private SpriteMasterList() {
		ItemMasterList itemML = ItemMasterList.getInstance();
	//Fill list
		spriteList.add(new Sprite("Suspicious Egg", 0, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/0.png"), false));		
		spriteList.add(new Sprite("", 1, itemML.getItem(0), itemML.getItem(4), 
				new Image("/assets/sprites/1.png"), true));		
		spriteList.add(new Sprite("", 2, itemML.getItem(3), itemML.getItem(2), 
				new Image("/assets/sprites/2.png"), true));		
		spriteList.add(new Sprite("", 3, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/3.png"), true));		
		spriteList.add(new Sprite("", 4, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/4.png"), true));		
		spriteList.add(new Sprite("", 5, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/5.png"), true));		
		spriteList.add(new Sprite("", 6, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/6.png"), true));		
		spriteList.add(new Sprite("", 7, itemML.getItem(4), itemML.getItem(0), 
				new Image("/assets/sprites/7.png"), true));		
		spriteList.add(new Sprite("", 8, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/8.png"), true));		
		spriteList.add(new Sprite("", 9, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/9.png"), true));		
		spriteList.add(new Sprite("", 10, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/10.png"), true));		
		spriteList.add(new Sprite("", 11, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/11.png"), true));		
		spriteList.add(new Sprite("", 12, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/12.png"), true));	
		spriteList.add(new Sprite("", 13, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/13.png"), true));		
		spriteList.add(new Sprite("", 14, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/14.png"), true));		
		spriteList.add(new Sprite("", 15, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/15.png"), true));		
		spriteList.add(new Sprite("", 16, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/16.png"), true));		
		spriteList.add(new Sprite("", 17, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/17.png"), true));		
		spriteList.add(new Sprite("", 18, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/18.png"), true));		
		spriteList.add(new Sprite("", 19, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/19.png"), true));
		spriteList.add(new Sprite("", 20, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/20.png"), true));
		spriteList.add(new Sprite("", 21, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/21.png"), true));
		spriteList.add(new Sprite("", 22, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/22.png"), true));
		spriteList.add(new Sprite("", 23, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/23.png"), true));
		spriteList.add(new Sprite("", 24, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/24.png"), true));
		spriteList.add(new Sprite("", 25, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/25.png"), true));
		spriteList.add(new Sprite("", 26, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/26.png"), true));
		spriteList.add(new Sprite("", 27, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/27.png"), true));
		spriteList.add(new Sprite("", 28, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/28.png"), true));
		spriteList.add(new Sprite("", 29, itemML.getItem(0), itemML.getItem(1), 
				new Image("/assets/sprites/29.png"), true));
	}	
/////////////////////////////////////////////////////////////////////////////////////////////////	
	public static SpriteMasterList getInstance() {
		if(instance == null) {
			instance = new SpriteMasterList();
		}
		return instance;
	}	
	public Sprite getSprite(int level) {
		return spriteList.get(level);
	}	
	public void setName(String name, int level) {
		spriteList.get(level).setName(name);
	}
	public int getNumUnlocked() {
		return numUnlocked;
	}
	public void setNumUnlocked(int num) {
		this.numUnlocked = num;
	}
}
