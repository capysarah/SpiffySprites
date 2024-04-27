package menu;


import core.Game;
import core.World;
import items.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Menu {
	
	private World world;
	private VBox menu;

	private Item selection;
	
	protected Font labelFont = new Font("Impact", 18);
	protected Font headerFont = new Font("Impact", 24);
	protected Font baseFont = new Font("Comic Sans MS", 14);
	
	private Label name;
	private Label lvl;	
	private Label fav;
	private Label hated;
	
	private Label expBar;
	private Label satiety;
	private Label mood;
	StackPane[] barPanes = { new StackPane(), new StackPane(), new StackPane() };
	Rectangle[] statRect = { new Rectangle(245.0, 20.0), new Rectangle(245.0, 20.0), new Rectangle(245.0, 20.0) };
	
	private Label gold;
	private Label shopGold;
	
	private ScrollPane inventoryScroll;
	private VBox vboxInventory;

	private ScrollPane bookScroll;
	
	private VBox popup;
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	public Menu(World world) {
		this.world = world;
		menu = createMenu();
	}
	public VBox getMenu() {
		return menu;
	}
	public VBox getPopUp() {
		return popup;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// UI UPDATES	
//Update inventory display
	public void updateInventory() {
		inventoryScroll.setContent(getInventory());
		shopGold.setText("" + world.getInventory().getGold());
		gold.setText("" + world.getInventory().getGold());
	}
//Update sprite info
	public void updateSprite() {
	//Name
		if(name.getText() != world.getSprite().getName()) {
			name.setText(world.getSprite().getName());
			world.updateSML();
			updateBook();
		}
	//Level & traits
		if(Integer.parseInt(lvl.getText()) != world.getSprite().getLevel()) {
			lvl.setText(String.valueOf(world.getSprite().getLevel()));
			fav.setText(world.getSprite().getFavFood().getName());
			hated.setText(world.getSprite().getHatedFood().getName());
		}
	//Stats	
		if(Integer.parseInt(satiety.getText()) != world.getSprite().getSatiety()) {
			satiety.setText(String.valueOf(world.getSprite().getSatiety()));
		}
		if(Integer.parseInt(mood.getText()) != world.getSprite().getMood()) {
			mood.setText(String.valueOf(world.getSprite().getMood()));
		}
		
		//Probably need one for exp ? - still needs a label. Maybe put it on the level bar		
	}
//Update stat bar display
	public void updateStatBars() {
		statRect[0].setWidth(world.getSprite().getExp() * (200 / world.getSprite().getMaxExp()));
		statRect[1].setWidth(world.getSprite().getSatiety() * 2);
		statRect[2].setWidth(world.getSprite().getMood() * 2);
		expBar.setText(String.valueOf(world.getSprite().getExp()));
	}
//Update book
	public void updateBook() {
		bookScroll.setContent(getBook());
	}
	
///////////////////////////////	CREATE MENU
	public VBox createMenu() {
		VBox vboxMenu = new VBox(10);
		ImageView viewHeader = new ImageView(new Image("assets/backgrounds/header_skeb.png"));

		AnchorPane.setTopAnchor(vboxMenu, 0.0);
		AnchorPane.setRightAnchor(vboxMenu, 700.0);
		AnchorPane.setLeftAnchor(vboxMenu, 0.0);
		AnchorPane.setBottomAnchor(vboxMenu, 0.0);

		vboxMenu.setPadding(new Insets(10, 5, 5, 100));
		vboxMenu.getChildren().addAll(viewHeader, createInfoBox(), createStatBox(), createMultiMenu());
		Image menuImg = new Image("assets/backgrounds/menu_background.png");
		ImageView menuView = new ImageView(menuImg);
		menuView.setFitHeight(700);
		menuView.setFitWidth(400);
		menuView.setPreserveRatio(true);
		vboxMenu.setBackground(new Background(new BackgroundImage(menuImg, BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		return vboxMenu;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// GENERAL INFO DISPLAY
	public HBox createInfoBox() {
		HBox spriteInfo = new HBox(30);
		spriteInfo.setPadding(new Insets(5, 5, 5, 5));
	//Labels
		VBox vboxInfo = new VBox(5);
		vboxInfo.setPadding(new Insets(0, 0, 0, 5));
		Label lblName = new Label("Name:");
		Label lblLevel = new Label("Level:");
		Label lblFav = new Label("Favorite Food:");
		Label lblHate = new Label("Hated food:");
		Label[] infoLbls = {lblName, lblLevel, lblFav, lblHate};
		for(int i = 0; i < infoLbls.length; i++) {
			infoLbls[i].setFont(labelFont);
			vboxInfo.getChildren().add(infoLbls[i]);
		}
	//Current sprite data
		VBox vboxData = new VBox(5);
		name = new Label(world.getSprite().getName());
		lvl = new Label(String.valueOf(world.getSprite().getLevel()));
		fav = new Label(world.getSprite().getFavFood().getName());
		hated = new Label(world.getSprite().getHatedFood().getName());

		Label[] dataLbls = {name, lvl, fav, hated};
		for(int i = 0; i < dataLbls.length; i++) {
			dataLbls[i].setFont(baseFont);
			vboxData.getChildren().add(dataLbls[i]);
		}
		spriteInfo.getChildren().addAll(vboxInfo, vboxData);
		return spriteInfo;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// STATS DISPLAY
	public HBox createStatBox() {
		HBox spriteStats = new HBox(15);
		spriteStats.setPadding(new Insets(0, 5, 5, 15));		
		VBox vboxLbl = new VBox(20);
//Label display
		Label lblExp = new Label("    Exp:");
		Label lblHunger = new Label("Satiety:");
		Label lblMood = new Label("  Mood:");
		Label[] lblList = {lblExp, lblHunger, lblMood};
		for(int i = 0; i < lblList.length; i++) {
			lblList[i].setFont(labelFont);
			vboxLbl.getChildren().add(lblList[i]);
		}
//Bar display
		VBox vboxBar = getStatBars();
//Show current levels
		VBox numLvl = new VBox(20);
		expBar = new Label(String.valueOf(world.getSprite().getExp()));
		satiety = new Label(String.valueOf(world.getSprite().getSatiety()));
		mood = new Label(String.valueOf(world.getSprite().getMood()));
//Number display
		Label[] numList = {expBar, satiety, mood};
		for(int i = 0; i < numList.length; i++) {
			numList[i].setFont(baseFont);
			numLvl.getChildren().add(numList[i]);
		}	
		spriteStats.getChildren().addAll(vboxLbl, vboxBar, numLvl);
		return spriteStats;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// MULTIMENU UI
	public VBox createMultiMenu() {
		VBox multiMenu = new VBox(10);
		multiMenu.setPrefSize(400, 400);
		multiMenu.setPadding(new Insets(5, 0, 5, 10));
//Buttons for menu options
		HBox hboxMenuBtns = new HBox(30);
		hboxMenuBtns.setPadding(new Insets(0, 0, 0, 75));
		Button inventoryBtn = new Button();	
		Button shopBtn = new Button();		
		Button bookBtn = new Button();	
		hboxMenuBtns.getChildren().addAll(inventoryBtn, shopBtn, bookBtn);
		Button[] btnList = {inventoryBtn, shopBtn, bookBtn};
		VBox [] menuList = {createInventory(), createShop(), createBook()};
		for(int i = 0; i < btnList.length; i++) {
			btnList[i].setPrefSize(30, 30);
			btnList[i].setBackground(new Background(new BackgroundImage(new Image("assets/backgrounds/button_bg.png"), BackgroundRepeat.NO_REPEAT, 
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			ImageView btnIcon = new ImageView(("assets/menu-icons/menu_icon_" + (i + 1) + ".png"));
			btnList[i].setGraphic(btnIcon);
			final int index = i;	
			btnList[index].setOnMouseClicked(e -> {
				multiMenu.getChildren().set(1, menuList[index]);				
			});			
		}	
		multiMenu.getChildren().addAll(hboxMenuBtns, vboxInventory);	
		return multiMenu;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// INVENTORY UI
	public VBox createInventory() {
		vboxInventory = new VBox(10);
		
		inventoryScroll = new ScrollPane();
		inventoryScroll.setPrefSize(330, 400);
		inventoryScroll.setMaxWidth(Region.USE_PREF_SIZE);
		inventoryScroll.setMaxHeight(Region.USE_PREF_SIZE);
		inventoryScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		Label lblMoney = new Label("Gold:");
		lblMoney.setFont(labelFont);
		gold = new Label(String.valueOf(world.getInventory().getGold()));
		gold.setFont(baseFont);
		HBox hboxMoney = new HBox(10);
		hboxMoney.getChildren().addAll(lblMoney, gold);
		inventoryScroll.setContent(getInventory());
		vboxInventory.getChildren().addAll(hboxMoney, inventoryScroll);
		return vboxInventory;
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////// SHOP UI
	public VBox createShop() {
		VBox vboxShop = new VBox(5);
		Label moneyLbl = new Label("Gold:");
		moneyLbl.setFont(labelFont);
		shopGold = new Label(String.valueOf(world.getInventory().getGold()));
		shopGold.setFont(baseFont);
		HBox moneyHbox = new HBox(10);
		moneyHbox.getChildren().addAll(moneyLbl, shopGold);
		StackPane shopContent = new StackPane();
		shopContent.setPadding(new Insets(0, 60, 0, 0));
		ScrollPane shopScroll = new ScrollPane();
		shopScroll.setPrefSize(330, 400);
		shopScroll.setMaxWidth(Region.USE_PREF_SIZE);
		shopScroll.setMaxHeight(Region.USE_PREF_SIZE);
		shopScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);		
		VBox shopVbox = new VBox(10);
		shopVbox.setPadding(new Insets(5, 0, 0, 5));
		for(int i = 0; i < world.getIML().getTotal(); i++) {
			selection = world.getIML().getItem(i);
			int index = i;
			VBox vboxShopItems = new VBox(0);
			vboxShopItems.getChildren().addAll(new Label(selection.getName()), new Label(selection.getDescription()), 
					new Label(selection.getHungerPoints() + "/50 HP"), new Label("$" + selection.getCost()));
			HBox hboxShopItems = new HBox(5);
			ImageView view = new ImageView(new Image("assets/items/" + i + ".png"));
			hboxShopItems.getChildren().addAll(view, vboxShopItems);
			shopVbox.getChildren().addAll(hboxShopItems, new Separator());
//Buy items
			view.setOnMouseClicked(e -> {
				selection = world.getIML().getItem(index);
				ImageView buyView = new ImageView(view.getImage());
				buyView.setFitHeight(100);
				buyView.setFitWidth(100);
				world.setPopUp(shopPopUp(buyView, selection, shopContent));

			});
		}
		shopScroll.setContent(shopVbox);
		shopContent.getChildren().add(shopScroll);
		vboxShop.getChildren().addAll(moneyHbox, shopContent);
		return vboxShop;
	}

///////////////////////////////////////////////////////////////////////////////////////////////// SPRITE BOOK UI
	public VBox createBook() {
		VBox vboxBook = new VBox(15);
		Label lblBook = new Label("Sprite Book");
		lblBook.setFont(labelFont);
		bookScroll = new ScrollPane();
		bookScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		bookScroll.setPrefSize(330, 400);
		bookScroll.setMaxWidth(Region.USE_PREF_SIZE);
		bookScroll.setMaxHeight(Region.USE_PREF_SIZE);
		bookScroll.setContent(getBook());
		vboxBook.getChildren().addAll(lblBook, bookScroll);
		return vboxBook;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// INVENTORY DATA
	public VBox getInventory() {
		VBox itemVbox = new VBox(10);
		itemVbox.setPadding(new Insets(5, 0, 0, 5));
		for(int i = 0; i < (world.getInventory().size()); i++) {
			if(world.getInventory().size() == 0) {
				break;
			}
			else {
				VBox vboxItems = new VBox();
				vboxItems.getChildren().addAll(new Label(world.getInventory().getItem(i).getName() + " (x" + 
						world.getInventory().getItem(i).getCount() + ")"), 
						new Label(world.getInventory().getItem(i).getDescription()), 
						new Label(world.getInventory().getItem(i).getHungerPoints() + "/50 HP"));
				HBox hboxItems = new HBox(5);
				ImageView view = new ImageView(world.getInventory().getItem(i).getImage());

				hboxItems.getChildren().addAll(view, vboxItems);
				itemVbox.getChildren().addAll(hboxItems, new Separator());
//Using items			
				view.setOnDragDetected(e -> {
					world.setSelection(world.getItemFromImg(view.getImage()));
					Dragboard dragBoard = view.startDragAndDrop(TransferMode.MOVE);
					ClipboardContent content = new ClipboardContent();
					content.putImage(view.getImage());
					dragBoard.setContent(content);					
					e.consume();
				});
			}
		}
		return itemVbox;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// BOOK DATA	
	public VBox getBook() {
		VBox bookBox = new VBox(10);
		bookBox.setPadding(new Insets(5, 0, 0, 5));
		for(int i = 0; i < (world.getSprite().getLevel() + 5); i++) {
			VBox vboxSprite = new VBox(0);
			HBox hboxSprites = new HBox(15);
			ImageView view;
			if(world.getSpriteFromML(i).isLocked() == true) {
				vboxSprite.getChildren().addAll(new Label(world.getSpriteFromML(i).getName()));
				view = new ImageView(new Image("/assets/sprites/locked.png"));
			}
			else {
				vboxSprite.getChildren().addAll(new Label(world.getSpriteFromML(i).getName()), new Label("Lvl " + i), 
						new Label("Likes " + world.getSpriteFromML(i).getFavFood().getName()), new Label("Dislikes " + world.getSpriteFromML(i).getHatedFood().getName()));
				view = new ImageView(world.getSpriteFromML(i).getImage());
				view.setOnMouseClicked(e -> {
					world.setPopUp(namePopUp());
				});
			}
			view.setFitHeight(80);
			view.setFitWidth(80);
			hboxSprites.getChildren().addAll(view, vboxSprite);
			bookBox.getChildren().addAll(hboxSprites, new Separator());
		}
		return bookBox;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// STAT BARS
	public VBox getStatBars() {
		VBox vboxBar = new VBox(15);
		Rectangle expBar = new Rectangle(205.0, 25.0);
		Rectangle hungerBar = new Rectangle(205.0, 25.0);
		Rectangle moodBar = new Rectangle(205.0, 25.0);
		Rectangle[] barList = {expBar, hungerBar, moodBar};
		for(int i = 0; i < barList.length; i++) {
			barPanes[i].getChildren().add(barList[i]);
			statRect[i] = new Rectangle(200.0, 20.0);
			if(i == 0) {
				statRect[i].setFill(Paint.valueOf("Green"));
				statRect[i].setWidth(world.getSprite().getExp() * (200 / world.getSprite().getMaxExp()));
			}
			else if(i == 1) {
				statRect[i].setFill(Paint.valueOf("Orange"));
				statRect[i].setWidth(world.getSprite().getSatiety() * 2);
			}
			else {
				statRect[i].setFill(Paint.valueOf("Yellow"));
				statRect[i].setWidth(world.getSprite().getMood() * 2);
			}
			barPanes[i].getChildren().add(statRect[i]);
			barPanes[i].setAlignment(Pos.CENTER_LEFT);
			StackPane.setMargin(statRect[i], new Insets(0, 0, 0, 2));
			barPanes[i].setPadding(new Insets(0, 0, 0, 5));
			vboxBar.getChildren().add(barPanes[i]);
		}
		return vboxBar;	
	}
///////////////////////////////////////////////////////////////////////////////////////////////// POP-UPS
//Naming pop-up
	public VBox namePopUp() {
		popup = new VBox(10);
		popup.setPadding(new Insets(15, 15, 15, 15));
		popup.setAlignment(Pos.CENTER);
		popup.setPrefSize(500, 350);
		popup.setBackground(new Background(new BackgroundImage(new Image("assets/backgrounds/popup0.png"), BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		Label prompt = new Label("New name?");
		prompt.setFont(headerFont);
		TextField tf = new TextField();
		tf.setPrefSize(50, 20);
		Button enter = new Button("Done");
		enter.setPrefSize(100, 30);
		enter.setFont(labelFont);
		enter.setOnMouseClicked(e -> {
			world.getSprite().setName(tf.getText());
			world.updateSML();
			updateSprite();
			world.removePopUp();
		});
		enter.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
				world.getSprite().setName(tf.getText());
				world.updateSML();
				updateSprite();
				world.removePopUp();
			}
		});
		popup.getChildren().addAll(prompt, tf, enter);
		return popup;
	}
//Shop pop-up
///////////////////////////////////////////////////////////////////////////////////////////////// SHOP POP-UP UI
	public VBox shopPopUp(ImageView view, Item item, StackPane parent) {
		popup = new VBox(10);
		popup.setPrefSize(500, 350);
		popup.setBackground(new Background(new BackgroundImage(new Image("assets/backgrounds/popup0.png"), BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		popup.setAlignment(Pos.CENTER);
		//Create buy menu with increment/decrement options
		HBox buyBox = new HBox(3);
		buyBox.setAlignment(Pos.CENTER);
		item.setBuyCount(0);		
		Label lblAmount = new Label("" + item.getBuyCount());
		lblAmount.setFont(baseFont);		
		Button[] shopBtnList = { new Button("+"), new Button("-"), new Button("Buy") };
		shopBtnList[0].setPrefSize(30, 15);// increment
		shopBtnList[1].setPrefSize(30, 15);// decrement
		shopBtnList[2].setPrefSize(60, 15);// buy button
		shopBtnList[2].setFont(baseFont);	
		buyBox.getChildren().addAll(shopBtnList[2], shopBtnList[1], lblAmount, shopBtnList[0]);			
		//Increment count
		shopBtnList[0].setOnMouseClicked(e -> {
			if(item.getBuyCount() == world.getInventory().getMaxBuy(item)) {
				shopBtnList[0].setDisable(true);
				shopBtnList[2].setDisable(false);
			}
			else {
				item.setBuyCount((item.getBuyCount() + 1));
				lblAmount.setText("" + item.getBuyCount());
				shopBtnList[2].setDisable(false);
			}
		});	
		//Decrement count
		shopBtnList[1].setOnMouseClicked(e -> {
			shopBtnList[0].setDisable(false);
			if(item.getBuyCount() == 0) {
				shopBtnList[2].setDisable(true);
			}
			else {
				item.setBuyCount((item.getBuyCount() - 1));
				lblAmount.setText("" + item.getBuyCount());
			}
		});	
		//Buy
		shopBtnList[2].setOnMouseClicked(e -> {
			world.buyItem(item, item.getBuyCount());
			world.removePopUp();
		});
		//Don't buy button- closes pop-up
		Button noBtn = new Button("Don't Buy");
		noBtn.setFont(baseFont);
		noBtn.setPrefSize(110, 15);
		noBtn.setOnMouseClicked(e -> {
			world.removePopUp();
		});
		popup.getChildren().addAll(view, buyBox, noBtn);
		return popup;
	}
//Death pop-up	
	public VBox deathPopUp(Game game) {
		popup = new VBox(10);
		popup.setPadding(new Insets(15, 15, 15, 15));
		popup.setAlignment(Pos.CENTER);
		popup.setPrefSize(500, 350);
		popup.setBackground(new Background(new BackgroundImage(new Image("assets/backgrounds/popup0.png"), BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		Label header = new Label("Game over");
		header.setFont(headerFont);
		Label prompt = new Label(world.getSprite().getName() + " died! You should be ashamed.");
		prompt.setFont(labelFont);
		Button ok = new Button("Return to intro");
		ok.setFont(labelFont);
		ok.setPrefSize(200, 30);
		ok.setOnMouseClicked(e -> {
			world.removePopUp();
			world.getTimer().cancel();
			game.switchScene(new Scene(game.createIntro(game.getStage())));
		});
		ok.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
				world.removePopUp();
				world.getTimer().cancel();
				game.switchScene(new Scene(game.createIntro(game.getStage())));
			}
		});
		popup.getChildren().addAll(header, prompt, ok);
		return popup;
	}
//Tutorial pop-up
	public VBox tutorialPopUp() {
		popup = new VBox(10);
		popup.setAlignment(Pos.CENTER);
		popup.setPrefSize(500, 350);
		popup.setBackground(new Background(new BackgroundImage(new Image("assets/backgrounds/popup0.png"), BackgroundRepeat.NO_REPEAT, 
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		Label header = new Label("How to play:");
		header.setFont(headerFont);
		popup.getChildren().add(header);
		Label[] tutorials = { new Label("Drag & drop items from your inventory to feed the sprite.\n	Don't let it starve!"),
			new Label("Buy new items from the shop tab if you run out of food!"),
			new Label("Check the book tab to see past sprite forms. \n	Click on the sprite to rename or select it!")
		};
		for(int i = 0; i < tutorials.length; i++) {
			tutorials[i].setFont(baseFont);
			popup.getChildren().add(tutorials[i]);
		}
		Button ok = new Button("Okay");
		ok.setFont(labelFont);
		ok.setPrefSize(200, 30);
		ok.setOnMouseClicked(e -> {
			world.removePopUp();
		});
		ok.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
				world.removePopUp();
			}
		});
		popup.getChildren().add(ok);
		return popup;
	}
}