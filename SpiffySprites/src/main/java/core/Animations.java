package core;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import sprites.Sprite;

public class Animations {
	
	private Sprite sprite;
	
	private double destinationX;
	private double destinationY;
	
	private double maxX = 300;
	private double minX = -225;
	
	private double maxY = -50;
	private double minY = 50;
	
	private int last = -1;
	
	public Animations() {}

	public double getX() {
		return destinationX;
	}
	public double getY() {
		return destinationY;
	}
	public TranslateTransition getPath(ImageView sprite) {
		TranslateTransition[] paths = {
				new TranslateTransition(Duration.millis(3000), sprite),
				new TranslateTransition(Duration.millis(3000), sprite),
				new TranslateTransition(Duration.millis(3000), sprite),
				new TranslateTransition(Duration.millis(3000), sprite),
				new TranslateTransition(Duration.millis(3000), sprite),
				new TranslateTransition(Duration.millis(3000), sprite),
				new TranslateTransition(Duration.millis(3000), sprite),
				new TranslateTransition(Duration.millis(3000), sprite)
		};
	//Set direction for each tt path
		for(int i = 0; i < 7; i++) {
			paths[i].setFromX(sprite.getX());
			paths[i].setFromY(sprite.getY());
		}
	//0
		paths[0].setToX(maxX / 2);
		paths[0].setToY(minY);
	//1
		paths[1].setToX(maxX);
		paths[1].setToY(0);
	//2
		paths[2].setToX(maxX / 2);
		paths[2].setToY(maxY);
		paths[7].getNode().resize(-(sprite.getScaleX()), -(sprite.getScaleY()));
	//3
		paths[3].setToX(0);
		paths[3].setToY(maxY);
	//4
		paths[4].setToX(minX / 2);
		paths[4].setToY(0);
	//5
		paths[5].setToX(minX);
		paths[5].setToY(0);
	//6
		paths[6].setToX(minX / 2);
		paths[6].setToY(minY);
		paths[7].getNode().resize(-(sprite.getScaleX()), -(sprite.getScaleY()));
	//7
		paths[7].setToX(0);
		paths[7].setToY(0);
		return getNext(paths);
	}
	//Get current path index
	public int getCurIndex() {
		return last;
	}
	//Get next path
	public TranslateTransition getNext(TranslateTransition[] paths) {
		last++;
		if(last > 7) {
			last = 0;
			System.out.println("Path index: " + last);
			destinationX = paths[last].getToX();
			destinationY = paths[last].getToY();
			return paths[last];
		}
		else {
			System.out.println("Path index: " + last);
			destinationX = paths[last].getToX();
			destinationY = paths[last].getToY();
			return paths[last];
		}
	}
	//Jump animation 
	public TranslateTransition[] getJumpPath(ImageView sprite) {
		TranslateTransition[] paths = {
				new TranslateTransition(Duration.millis(350), sprite),
				new TranslateTransition(Duration.millis(350), sprite)
		};
		//Jump
		paths[0].setFromX(sprite.getX());
		paths[0].setFromY(sprite.getY());
		paths[0].setToX(sprite.getX());
		paths[0].setToY(sprite.getY() - 50);
		//Land
		paths[1].setFromX(paths[0].getFromX());
		paths[1].setFromY(paths[0].getFromY() - 50);
		paths[1].setToX(paths[0].getFromX());
		paths[1].setToY(paths[0].getFromY());
		
		return paths;	
	}
	//Transform animation
	public FadeTransition[] getCocoon(ImageView cocoon) {
		FadeTransition[] fts = {new FadeTransition(Duration.millis(2000), cocoon), new FadeTransition(Duration.millis(2000), cocoon)};
		fts[0].setFromValue(0);
		fts[0].setToValue(500);
		fts[1].setFromValue(500);
		fts[1].setToValue(0);	
		return fts;
	}
	public ScaleTransition scaleCocoon(ImageView cocoon) {
		ScaleTransition st = new ScaleTransition(Duration.millis(2500), cocoon);
		st.setByX(0.25f);
		st.setByY(0.25f);
		st.setAutoReverse(true);
		st.setCycleCount(4);
		return st;
	}
	//Death animation
	public Timeline getDeath(ImageView sprite) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		KeyValue keyX = new KeyValue(sprite.rotateProperty(), 90);
		KeyFrame kf = new KeyFrame(Duration.millis(3000), keyX);
		timeline.getKeyFrames().add(kf);
		return timeline;
	}
}
