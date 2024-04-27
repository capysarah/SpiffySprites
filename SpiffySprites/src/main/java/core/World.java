package core;

import sprites.*;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import menu.Menu;
import items.*;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class World {
	
	private Game game;
	
	private SpriteMasterList spriteList = SpriteMasterList.getInstance();
	private ItemMasterList itemList = ItemMasterList.getInstance();
	
	private Menu menu;
	private Sprite sprite;
	private ImageView spriteView;
	private Inventory inventory;
	private Item selection;

	//private Object[] saveData = {sprite, inventory};
	
	private LocalTime curTime;
	
	private Timer timer;
	private Timer deathTimer;

	private ImageView cocoon = new ImageView(new Image("/assets/sprites/cocoon.png"));
	
	private Animations animations;
	private Timeline timeline;
	private TranslateTransition tt;
	private TranslateTransition[] jPath;
	
	private AnchorPane root;
	private Background[] bgs = {
			new Background(new BackgroundImage(new Image("assets/backgrounds/bg_morning.png"), BackgroundRepeat.NO_REPEAT, 
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)),
			new Background(new BackgroundImage(new Image("assets/backgrounds/bg_sunset.png"), BackgroundRepeat.NO_REPEAT, 
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)),
			new Background(new BackgroundImage(new Image("assets/backgrounds/bg_night.png"), BackgroundRepeat.NO_REPEAT, 
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT))
	};
	private VBox popup;
///////////////////////////////////////////////////////////////////////////////////////////////// 
//Creates sprite & inventory
	public World(Game game) {
		this.game = game;
		System.out.println("Creating world...");
		inventory = game.deserializeInventory(this);
		sprite = game.deserializeSprite(this);
		setGraphic();
		game.serialize(sprite, inventory);
		menu = new Menu(this);
		animations = new Animations();
		curTime = LocalTime.now();
		timer = new Timer();
		timer.scheduleAtFixedRate(movementTimer(), 10000, 30000);
		timer.scheduleAtFixedRate(createTask(), 5000, 5000);
	}
/////////////////////////////////////////////////////////////////////////////////////////////////
	public Sprite getSprite() {
		return sprite;
	}
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	public Inventory getInventory() {
		return inventory;
	}
	public Item getItemFromML(int index) {
		return itemList.getItem(index);
	}
	public Sprite getSpriteFromML(int level) {
		return spriteList.getSprite(level);
	}
	public Item getItemFromImg(Image image) {
		Item target = null;
		for(int i = 0; i < itemList.getTotal(); i++) {
			if(image == itemList.getItem(i).getImage()) {
				target = itemList.getItem(i);
			}
		}
		return target;
	}
	public ItemMasterList getIML() {
		return itemList;
	}
	public Timer getTimer() {
		return timer;
	}
	public VBox getPopup() {
		return popup;
	}
	public void setPopUp(VBox popup) {
		this.popup = popup;
		AnchorPane.setTopAnchor(popup, 50.0);
		AnchorPane.setLeftAnchor(popup, 575.0);
		root.getChildren().add(popup);
	}
	public void removePopUp() {
		root.getChildren().remove(popup);
	}
/////////////////////////////////////////////////////////////////////////////////////////////////
//Update
	public void updateSprite() {
		menu.updateSprite();
		menu.updateStatBars();
		spriteView.setImage(sprite.getImage());
	}
	public void updateInventory() {
		menu.updateInventory();
	}
	public void updateSML() {
		spriteList.setName(sprite.getName(), sprite.getLevel());
	}
///////////////////////////////////////////////////////////////////////////////////////////////// TIMER TASKS
//Main timer task
	public TimerTask createTask() {
		TimerTask task = new TimerTask() {
			public void run() {	
				System.out.println("////////////////////////////////\nRunning stats task:");
				sprite.decrementStats();
				int expRate = 0;
				//Conditions for death
				if(sprite.getSatiety() == 0) {
						sprite.setDying(true);
						startDeathTimer();
				}
				//Conditions for misery
				if(sprite.getMood() == 0) {
						sprite.setMiserable(true);
				}
				//Top condition
				if(sprite.getSatiety() >= 95 && sprite.getMood() >= 95) {
					expRate = 5;
					if(sprite.isDying() == true) {
						sprite.setDying(false);
						deathTimer.cancel();
					}
					if(sprite.isMiserable() == true) {
						sprite.setMiserable(false);
					}
				}
				//Good condition
				else if(sprite.getSatiety() >= 50 && sprite.getMood() >= 50) {
					expRate = 3;
					if(sprite.isDying() == true) {
						sprite.setDying(false);
						deathTimer.cancel();
					}
					if(sprite.isMiserable() == true) {
						sprite.setMiserable(false);
					}
				}
				//Avg condition
				else if(sprite.getSatiety() >= 10 && sprite.getMood() >= 10) {
					expRate = 1;
					if(sprite.isDying() == true) {
						sprite.setDying(false);
						deathTimer.cancel();
					}
					if(sprite.isMiserable() == true) {
						sprite.setMiserable(false);
					}
				}
				//Bad condition
				else if(sprite.getMood() > 0 && sprite.getSatiety() > 0) {
					if(sprite.isDying() == true) {
						sprite.setDying(false);
						deathTimer.cancel();
					}
					if(sprite.isMiserable() == true) {
						sprite.setMiserable(false);
					}
				}
				//Final exp gain rate
				final int expGain = expRate;
				//Updates sprite labels on FX thread from within timer thread
				//Also checks the time to see if bg needs to change
				Platform.runLater(new Runnable() {
					public void run() {
						setBg();
						gainExp(expGain);
						updateSprite();
					}
				});
			}
		};
		return task;
	}
//Animation timer
	public TimerTask movementTimer() {
		TimerTask task = new TimerTask() {
			public void run() {	
				Platform.runLater(new Runnable() {
					public void run() {
						System.out.println("******************************\nStarting path.");
						tt = animations.getPath(spriteView);
						if(animations.getCurIndex() == 2) {
							spriteView.setScaleX(-spriteView.getScaleX());
						}
						else if(animations.getCurIndex() == 6){
							spriteView.setScaleX(Math.abs(spriteView.getScaleX()));
						}
						tt.play();
						spriteView.setX(animations.getX());
						spriteView.setY(animations.getY());
						System.out.println("Path finished.");
					}
				});
			}
		};
		return task;
	}
//Death task & timer
	public void startDeathTimer() {
		System.out.println("Started death timer.");
		deathTimer = new Timer();
		TimerTask death = new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						die();
					}
				});
			} };
			deathTimer.schedule(death, 10000);
	}
///////////////////////////////////////////////////////////////////////////////////////////////// SPRITE STUFF	
//Create new sprite
	public Sprite createSprite(int level, int exp, int satiety, int mood) {
		sprite = spriteList.getSprite(level);
		sprite.setExp(exp);
		sprite.setSatiety(satiety);
		sprite.setMood(mood);
		return sprite;
	}
	
	public void setGraphic() {
	//Create ImageView for sprite - main interaction point
		spriteView = sprite.createView();
	//Set click to pet
		spriteView.setOnMouseClicked(e -> {
            sprite.pet();
            playJump();
            updateSprite();
        });
	//Start drag & drop to feed
		spriteView.setOnDragOver(e -> {
			final Sprite target = getSprite();
            if(e.getGestureSource() != target && e.getDragboard().hasImage()) {
            	e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
	//Set on drag & drop to feed
		spriteView.setOnDragDropped(e -> {
			Dragboard dragBoard = e.getDragboard();		
            if(dragBoard.hasImage()) {
            	sprite.feed(selection);
            	playJump();
            	inventory.removeItem(selection);
            	updateSprite();
            	updateInventory();
            }
            e.consume();
        });
	}
//Load sprite	
	public Sprite loadSprite(Sprite sprite) {
		Sprite temp = getSpriteFromML(sprite.getLevel());
		temp.setExp(sprite.getExp());
		if(sprite.getSatiety() > 0) 
			temp.setSatiety(sprite.getSatiety());
		else
			temp.setSatiety(1);
		if(sprite.getMood() > 0) 
			temp.setMood(sprite.getMood());
		else
			temp.setMood(1);
		sprite = temp;
		return sprite;
	}
//Get sprite imageview
	public ImageView getSpriteView() {
		return spriteView;
	}
//Exp gain
	public void gainExp(int amount) {
		if(sprite.getMood() == 0) {
			return; //No exp gain if bad mood
		}
		sprite.setExp(sprite.getExp() + amount);
		System.out.println("Sprite gained " + amount + " exp. Total exp = " + sprite.getExp() + "\nMax exp: " + sprite.getMaxExp());
	//Check if enough exp for level up
		if(sprite.getExp() >= sprite.getMaxExp()) {
			System.out.println("Sprite exp at max. Level up incoming...");
			sprite.setExp(sprite.getExp() - sprite.getMaxExp());
			levelUp(sprite.getLevel());
		}
	}		
//Level up
	public void levelUp(int curLevel) {
		sprite.setLevel((curLevel) + 1);
		inventory.setGold((inventory.getGold()) + (sprite.getLevel() * 50));
		System.out.println("Level up! Sprite is now level " + sprite.getLevel() + ". Transformation incoming." +
				"\nGold gained: $" + (sprite.getLevel() * 25));
		transform();
		System.out.println("Adding name pop-up to root");
		popup = menu.namePopUp();
		root.getChildren().add(popup);
		AnchorPane.setTopAnchor(popup, 50.0);
		AnchorPane.setLeftAnchor(popup, 575.0);
		updateInventory();
		updateSprite();
		game.serialize(sprite, inventory);
		
	}
//Transform
	public void transform() {
		System.out.println("Switch sprites");
		Sprite temp = spriteList.getSprite(sprite.getLevel());
		spriteList.getSprite(sprite.getLevel()).setLocked(false);
		spriteList.setNumUnlocked(sprite.getLevel());
		temp.setSatiety(sprite.getSatiety());
		temp.setMood(sprite.getMood());
		temp.setExp(0);
		playTransform(temp);
	}			
//Death :(
	public void die() {
		//Insert animations and sounds
		System.out.println("Sprite has died of starvation. Game over.");
		timeline = animations.getDeath(spriteView);
		timeline.play();
		timeline.setOnFinished(e -> {
			deathTimer.cancel();
			popup = menu.deathPopUp(game);
			AnchorPane.setTopAnchor(popup, 50.0);
			AnchorPane.setLeftAnchor(popup, 575.0);
			root.getChildren().add(popup);
		});
		
	}
//Animations
	public void playJump() {
		jPath = animations.getJumpPath(spriteView);
    	for(int i = 0; i < jPath.length; i++) {
    		jPath[i].play();
    	}
	}
	public void playTransform(Sprite temp) {
		cocoon.setOpacity(0);
		AnchorPane.setTopAnchor(cocoon, (AnchorPane.getTopAnchor(spriteView) - 40));
		AnchorPane.setLeftAnchor(cocoon, (AnchorPane.getLeftAnchor(spriteView) - 40));	
		root.getChildren().add(cocoon);
		FadeTransition[] fts = animations.getCocoon(cocoon);
		ScaleTransition st = animations.scaleCocoon(cocoon);
		fts[0].play();
		fts[0].setOnFinished(e -> {
			sprite = temp;
			st.play();
		});
		fts[1].setOnFinished(e -> {
			root.getChildren().remove(cocoon);
			playJump();
		});
		st.setOnFinished(e -> {
			fts[1].play();
		});
	}
///////////////////////////////////////////////////////////////////////////////////////////////// INVENTORY STUFF	
//Create inventory
	public Inventory createInventory() {
		inventory = new Inventory();
		inventory.setGold(50);
		inventory.starterItems(getItemFromML(0), 5);
		return inventory;
	}
//Buy item
	public void buyItem(Item item, int quantity) {
		inventory.buyItem(item, quantity);
		updateInventory();
	}
//Use item
	public void useItem(Item item) {
		inventory.removeItem(item);
		updateInventory();
	}
//Set selection
	public void setSelection(Item item) {
		selection = item;
	}

///////////////////////////////////////////////////////////////////////////////////////////////// BG STUFF
//Create main pane
	public AnchorPane createPane() {
		root = new AnchorPane();
		root.setPrefSize(1200, 700);
		root.setBackground(getBg());
		//If game is new, show tutorial pop-up
		popup = menu.tutorialPopUp();
		AnchorPane.setTopAnchor(popup, 50.0);
		AnchorPane.setLeftAnchor(popup, 575.0);
		AnchorPane.setTopAnchor(spriteView, 400.0);
		AnchorPane.setLeftAnchor(spriteView, 700.0);
		//Create cocoon
		root.getChildren().addAll(menu.getMenu(), spriteView, popup);
		spriteView.setX(0);
		spriteView.setY(0);
		return root;
	}
//Get background from current time
	public Background getBg() {
		if(curTime.isBefore(LocalTime.of(6, 0)) || curTime.isAfter(LocalTime.of(19, 0))) 
			return bgs[2];		
		else if(curTime.isAfter(LocalTime.of(5, 59)) || curTime.isBefore(LocalTime.of(18, 0)))
			return bgs[0];
		else 
			return bgs[1];	
	}
//Set background
	public void setBg() {
		if(root.getBackground() != getBg())
			root.setBackground(getBg());
	}
}