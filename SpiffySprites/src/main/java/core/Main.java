package core;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
		
	@Override
	public void start(Stage stage){
		Game game = new Game();				
		stage.setTitle("Spiffy Sprites");
		Scene intro = new Scene(game.createIntro(stage));
		stage.setScene(intro);
		stage.setResizable(false);
		stage.show();
		stage.setOnCloseRequest(e -> {
			if(game.getWorld() != null) {
				game.serialize(game.getWorld().getSprite(), game.getWorld().getInventory());
			}
			stage.close();
		});
		}		

	public static void main(String[] args) {
		Application.launch(args);
	}
}