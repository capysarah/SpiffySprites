package items;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Inventory {

	private ArrayList<Item> inventory = null;
	private int gold;
	private int numElements = 0;
		
	public Inventory() {
		inventory = new ArrayList<Item>();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////	
	public ArrayList<Item> getInventory() {
		return inventory;
	}		
	public int getGold() {
		return gold;
	}	
	public void setGold(int gold) {
		this.gold = gold;
	}
	public Item getItem(int index) {
		return inventory.get(index);
	}
	public boolean canAfford(Item item) {
		if(item.getBuyCount() * item.getCost() > gold) {
			return false;
		}
		else {
			return true;
		}
	}
	public int getMaxBuy(Item item) {
		int temp = item.getBuyCount();
		int max = 0;
		for(int i = 1; i < 500; i++) {
			item.setBuyCount(i);
			if(canAfford(item) == true) {
				continue;
			}
			else {
				max = (i - 1);
				break;
			}
		}
		item.setBuyCount(temp);
		return max;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// METHODS
//Add first free item
	public void starterItems(Item item, int quantity) {
		inventory.add(item);
		item.setCount(quantity);
		numElements++;
	}
//Buy item
	public void buyItem(Item item, int quantity) {
		//Must have enough gold
		int total = (item.getCost() * quantity);
		if(gold < total) {
			System.out.println("You can't afford this.");
		}
		//Must have enough inventory space
		else if(isFull() && contains(item) == false) {
			System.out.println("You don't have enough space for this.");
		}
		//If both are true, player can buy the item
		else {
			//If player already has that item, add to count & update label to reflect quantity
			if(contains(item)) {
				int current = item.getCount();
				item.setCount(current + quantity);
				setGold((getGold() - total));
				System.out.println("Bought " + quantity + " " + item.getName() + " for " + total + " gold.");
			}
			//Else add new item to inventory
			else {
				inventory.add(item);
				item.setCount(quantity);
				setGold((getGold() - total));
				numElements++;
				System.out.println("Bought " + quantity + " " + item.getName() + " for " + total + " gold.");
			}		
		}
	}

//Remove item
	public void removeItem(Item item) {
		//Decrement count if player has more than one
		if(item.getCount() > 1) {
			item.setCount(item.getCount() - 1);
		}
		//Remove item entirely if player only has one
		else {
			inventory.remove(item);
			numElements--;
		}
		System.out.println("Used 1 " + item.getName() + ".");
	}
	
//Contains
	public boolean contains(Item item) {
		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i) == item) {
				return true;
			}
		}
		return false;
	}
//isFull
	@JsonIgnore
	public boolean isFull() {
		//Only 4 inventory slots allowed
		return(numElements == 4);
	}
//isEmpty
	@JsonIgnore
	public boolean isEmpty() {
		return (size() == 0);
	}	
//Size
	public int size() {
		//Reflects number of slots used, not total quantity
		return numElements;
	}
//toString
	public String toString() {
		String result = "";
		for(int i = 0; i < inventory.size(); i++) {
			result += "Slot " + (i + 1) + ": " + inventory.get(i).toString() + "\n";
		}
		return result;
	}
}
