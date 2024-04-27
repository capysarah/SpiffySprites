package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import items.Inventory;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sprites.Sprite;

public class Game {
	
	private Stage stage;
	private World world;
	
	//private MediaPlayer mp;
	//private Media[] media = {new Media("/assets/media/m1.mp3"), new Media("/assets/media/m2.mp3") };
	private ObjectMapper objMapper;
	
	private String spriteJson;
	private String invJson;
	
	private File sprFile = new File("sprite_data.txt");
	private File invFile = new File("inventory_data.txt");
	
	private boolean quit = false;
	
/////////////////////////////////////////////////////////////////////////////////////////////////			
	public Game() {
		objMapper = new ObjectMapper();
		//mp = new MediaPlayer(media[1]);
		//mp.setAutoPlay(true);
		//mp.play();	
	}
///////////////////////////////////////////////////////////////////////////////////////////////// 	
	public World getWorld() {
		return world;
	}
	public void switchScene(Scene scene) {
		stage.setScene(scene);
	}
	public Stage getStage() {
		return stage;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// INTRO SCENE
	public Parent createIntro(final Stage stage) {
		this.stage = stage;
		StackPane pane = new StackPane();
		pane.setMinSize(1200, 700);		
		//Bg image
		pane.setBackground(new Background(new BackgroundImage(new Image("/assets/backgrounds/intro_background.png"), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		//Start button
		Button btnStart = new Button("Start Game");
		btnStart.setPrefSize(575, 50);
		btnStart.setFont(new Font("Impact", 48));
		btnStart.setBackground(new Background(new BackgroundImage(new Image("/assets/backgrounds/start_button.png"), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		pane.getChildren().addAll(btnStart);
		btnStart.setOnMouseClicked(e -> {
			//mp = new MediaPlayer(media[1]);
			//mp.play();
			stage.setScene(new Scene(createGame()));
		});
		return pane;
	}
///////////////////////////////////////////////////////////////////////////////////////////////// GAME SCENE
	public Parent createGame() {
		world = new World(this);
		System.out.println("Sprite:\n" + getWorld().getSprite().toString());	
		return getWorld().createPane();
	}
///////////////////////////////////////////////////////////////////////////////////////////////// SAVE DATA
//Start
	public void start() {
		while(quit == false) {
			
		}
	}
//Quit	
	public void quit() {
		quit = true;
	}
//Deserialize
	public Inventory deserializeInventory(World world) {
		try {
			Scanner read = new Scanner(invFile);
			invJson = read.toString();
			read.close();
			System.out.println(objMapper.readValue(invJson, Inventory.class));
			Inventory inventory = objMapper.readValue(invJson, Inventory.class);
			System.out.println("Load successful\n" + inventory);
			return inventory;
		}
		catch(FileNotFoundException | JsonProcessingException e) {
			System.out.println("Load failed\nCreating new inventory...");
			Inventory inventory = world.createInventory();
			return inventory;
		}
	}
	public Sprite deserializeSprite(World world) {
		try {
			System.out.println("////////////////////////////////\nLoading data");
			Scanner read = new Scanner(sprFile);
			spriteJson = read.next();
			read.close();
			System.out.println(objMapper.readValue(spriteJson, Sprite.class));
			Sprite sprite = world.loadSprite(objMapper.readValue(spriteJson, Sprite.class));
			System.out.println("Load successful\n" + sprite);
			return sprite;
		}
		catch(FileNotFoundException | JsonProcessingException e) {
			e.printStackTrace();
			e.getCause();
			System.out.println("Load failed\nCreating new sprite...");
			Sprite sprite = world.createSprite(0, 0, 25, 25);
			return sprite;
		}
	}
//Serialize
	public void serialize(Sprite sprite, Inventory inventory) {
		try {
			System.out.println("////////////////////////////////\nSaving...");
			spriteJson = objMapper.writeValueAsString(sprite);
			invJson = objMapper.writeValueAsString(inventory);
			//Write to sprite file
			PrintWriter writer = new PrintWriter(sprFile);
			writer.println(spriteJson);
			writer.close();
			//Write to inventory file
			writer = new PrintWriter(invFile);
			writer.println(invJson);
			writer.close();
			System.out.println("Save successful");
		}
		catch(IOException e) {
			System.out.println("Save failed");
			e.printStackTrace();
		}		
	}
}