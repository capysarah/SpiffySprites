package sprites;

import java.awt.event.ActionListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import items.Item;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite {
	//General
	private String name;
	private Item favFood;
	private Item hatedFood;
	private Image img;
	private boolean changed;
	private boolean locked;
	//Levels
	private int satiety;
	private int maxSatiety = 100;
	private int mood;
	private int maxMood = 100;
	private int exp;
	private int level;
	private int maxExp;
	//Statuses
	private boolean isMiserable = false;
	private boolean isDying = false;
	//Location on ground
	private int x;
	private int y;


/////////////////////////////////////////////////////////////////////////////////////////////////
	//Constructor for new sprite
	public Sprite() {};
	//Constructor to load
	public Sprite(String name, int satiety, int mood, int exp, int level) {
		this.name = name;
		this.satiety = satiety;
		this.mood = mood;
		this.exp = exp;
		this.level = level;
	}
	//Constructor for book
	public Sprite(String name, int level, Item favFood, Item hatedFood, Image img, boolean locked) {
		this.name = name;
		this.level = level;
		if(level == 0) {
			this.maxExp = 10;
		}
		else {
			this.maxExp = level * 100;
		}
		this.favFood = favFood;
		this.hatedFood = hatedFood;
		this.img = img;
		this.locked = locked;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////
//Name	
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}	
//Image
	@JsonIgnore
	public Image getImage() {
		return img;
	}
	public void setImage(Image img) {
		this.img = img;
	}
//Satiety
	public int getSatiety() {
		return satiety;
	}	
	public void setSatiety(int satiety) {
		this.satiety = satiety;
		if(satiety == 0) {
			setDying(true);
		}
		else {
			setDying(false);
		}
	}	
//Mood
	public int getMood() {
		return mood;
	}
	public void setMood(int mood) {
		this.mood = mood;
		if(mood > maxMood) {
			setMood(maxMood);
		}
		else if(mood < 0) {
			setMood(0);
		}
		if(mood == 0) {
			setMiserable(true);
		}
		else {
			setMiserable(false);
		}
	}
//Exp & max exp
	public int getExp() {
		return exp;
	}	
	public void setExp(int exp) {
		this.exp = exp;
	}
	@JsonIgnore
	public int getMaxExp() {
		return maxExp;
	}
//Level
	public int getLevel() {
		return level;
	}	
	public void setLevel(int level) {
		this.level = level;
	}
//Fav food
	@JsonIgnore
	public Item getFavFood() {
		return favFood;
	}	
//Hated food
	@JsonIgnore
	public Item getHatedFood() {
		return hatedFood;
	}	
//Status effects - miserable & dying
	@JsonIgnore
	public boolean isMiserable() {
		return isMiserable;
	}
	public void setMiserable(boolean isMiserable) {
		this.isMiserable = isMiserable;
		if(isMiserable == true) {
			System.out.println("Sprite is miserable.");
		}
	}
	@JsonIgnore
	public boolean isDying() {
		return isDying;
	}
	public void setDying(boolean var) {
		this.isDying = var;
		if(var == true) {
			System.out.println("Sprite is dying.");
		}
	}
//Locked state
	@JsonIgnore
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean var) {
		this.locked = var;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////
	@JsonIgnore
	public ImageView createView() {
		ImageView view = new ImageView(getImage());
		return view;
	}	
//Decrement stats
	public void decrementStats() {
		//Can't have negative stats
		if(satiety > 0) {
			satiety--;
			System.out.println("Sprite lost 1 satiety. Total satiety = " + getSatiety());
		}
		if(mood > 0) {
			mood--;
			System.out.println("Sprite lost 1 mood. Total mood = " + getMood());
		}
	}
	
	
//Feed
	public void feed(Item food) {
	//Food preference affects mood
		int points = food.getHungerPoints();
		if(food == favFood && isMiserable() == false) {
			setMood(mood + 20); //Gain 20 mood points for fav food
		}
		else if(food == hatedFood) {
			setMood(mood - 20);//Lose 20 mood points for hated food
		}		
	//Feeding affects satiety
		if((satiety + points) > maxSatiety) {
			setSatiety(maxSatiety);
		}
		else {
		setSatiety(satiety + points);
		}
	}	
//Pet
	public void pet() {
		if(mood == maxMood) {
			System.out.println("Nothing happens.");
		}
		else {			
			mood++;
			System.out.println("Sprite has been pet. Total mood = " + getMood());
		}
	}	
//Get x
	@JsonIgnore
	public int getX() {
		return this.x;
	}
//Set x
	public void setX(int x) {
		this.x = x;
	}
//Get y
	@JsonIgnore
	public int getY() {
		return this.y;
	}
//Set y
	public void setY(int y) {
		this.y = y;
	}	
//toString
	public String toString() {
		return "Level: " + getLevel() + "\nExp: " + getExp() + "\nMax exp: " + getMaxExp() + "\nSatiety: " + getSatiety() + "\nMood:" + getMood();
	}
}
