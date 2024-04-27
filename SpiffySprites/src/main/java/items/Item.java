package items;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.image.Image;

public class Item {

	public String name;
	private String description;
	private int hungerPoints;
	private int cost;
	private int count;
	private int buyCount;
	private Image img;
	private int index;
	
/////////////////////////////////////////////////////////////////////////////////////////////////		
	public Item(String name, String description, int hungerPoints, int cost, Image img, int index) {
		this.name = name;
		this.description = description;
		this.hungerPoints = hungerPoints;
		this.cost = cost;
		this.img = img;
		this.index = index;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// GETTERS & SETTERS
	@JsonIgnore
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	@JsonIgnore
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonIgnore
	public int getHungerPoints() {
		return hungerPoints;
	}	
	public void setHungerPoints(int hungerPoints) {
		this.hungerPoints = hungerPoints;
	}
	@JsonIgnore
	public int getCost() {
		return cost;
	}	
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getCount() {
		return count;
	}	
	public void setCount(int count) {
		this.count = count;
	}
	@JsonIgnore
	public int getBuyCount() {
		return buyCount;
	}	
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	@JsonIgnore
	public Image getImage() {
		return img;
	}
	public void setImage(Image img) {
		this.img = img;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

///////////////////////////////////////////////////////////////////////////////////////////////// METHODS	
	//toString
	public String toString() {
		return name + " (x" + count + ")\n" + "Recovers " + hungerPoints + " hp \nDescription: " + description;
	}
	
}
