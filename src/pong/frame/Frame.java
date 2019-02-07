package pong.frame;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pong.ball.Ball;
import pong.controller.Controller;

/**
 *
 * Beschreibung
 *
 * @version 0.1git vom 07.02.2019
 * @author Maximilian Bieleke
 * @author Moritz Lindner
 */

public class Frame extends Application {
	private Circle circle = new Circle();
	private Rectangle rectangle1 = new Rectangle();
	private Rectangle rectangle2 = new Rectangle();
	private Controller controller1 = new Controller(50, 360);
	private Controller controller2 = new Controller(1205, 360);
	private Text score1 = new Text();
	private Text score2 = new Text();
	private int score1Temp = 0;
	private int score2Temp = 0;
	private Ball ball = new Ball(640, 360);
	private boolean upIsPressed = false;
	private boolean downIsPressed = false;
	private boolean wIsPressed = false;
	private boolean sIsPressed = false;
	private boolean randGetroffen = false;
	private boolean controllerGetroffen = false;
	private boolean coutofScreen1Up = false; // controller 1 nach oben aus dem Bild
	private boolean cCutOfScreen2Up = false; // controller 2 nach oben aus dem Bild
	private boolean coutOfScreen1Down = false; // controller 1 nach untem aus dem Bild
	private boolean coutOfScreen2Down = false; // controller 2 nach untem aus dem Bild
	private boolean firstStart = true;
	private long start = 0;
	private long iteration = 0;
	private long seconds = 0;
	private Color forShapes = Color.WHITE;

	// ai
	// private Ai ComputerAi = new Ai();

	public void start(Stage primaryStage) {
		Pane root = new Pane();
		Scene scene = new Scene(root, 520, 304, Color.BLACK);

		primaryStage.setFullScreen(true);
		root.getChildren().add(circle);
		root.getChildren().add(rectangle1);
		root.getChildren().add(rectangle2);
		root.getChildren().add(score1);
		root.getChildren().add(score2);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					upIsPressed = true;
					break; // controller2.move(1);
				case DOWN:
					downIsPressed = true;
					break; // controller2.move(-1);
				case W:
					wIsPressed = true;
					break; // controller1.move(1);
				case S:
					sIsPressed = true;
					break; // controller1.move(-1);
				case ESCAPE:
					System.exit(0);
				default:
					break;
				}
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent evt) {
				switch (evt.getCode()) {
				case UP:
					upIsPressed = false;
					break;
				case DOWN:
					downIsPressed = false;
					break;
				case W:
					wIsPressed = false;
					break;
				case S:
					sIsPressed = false;
					break;
				default:
					break;
				}
			}
		});
		prepareScore();
		setColors();
		start = System.currentTimeMillis();
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		primaryStage.setTitle("Pong");
		primaryStage.setScene(scene);
		primaryStage.show();
		score1.setLayoutX(250);
		score2.setLayoutX((primaryStage.getWidth() * 0.75));
		controller2.setX(Math.round((primaryStage.getWidth()) * 0.95));
		controller1.setX(primaryStage.getWidth() * 0.05);

		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent wevt) {
				System.exit(0);
			}
		});

		AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				seconds = Math.round((iteration - start) / 1000);
				ball.increaseSpeed();

				if (seconds == 15) {
					if (ball.getRadius() > 5) {
						controller1.setDeltaY(controller1.getnewDeltaY());
						controller2.setDeltaY(controller2.getnewDeltaY());
						ball.decreaseRadius();
						circle.setRadius(ball.getRadius());
						circle.setFill(colorChange());
						start = System.currentTimeMillis();
					}
				}
				if (score1.getText().equals("" + 5) || score2.getText().equals("" + 5)) {
					// new Thread(sleeper).run();
					System.exit(0);
				}
				if (firstStart) {
					show(); // deckt objekte auf
					//w
					firstStart = false;
				}

				checkInScreenController(primaryStage); // guckt ob alle
														// controller im Bild
														// sind
				if (upIsPressed && !cCutOfScreen2Up) 
					controller2.move(1);
				if (downIsPressed && !coutOfScreen2Down) 
					controller2.move(-1);
				if (wIsPressed && !coutofScreen1Up) 
					controller1.move(1);
				if (sIsPressed && !coutOfScreen1Down) 
					controller1.move(-1);
				
				// Kontroller Bounce
				if (cCutOfScreen2Up || coutOfScreen2Down)
					controller2.bounce();

				if (coutOfScreen1Down || coutofScreen1Up)
					controller1.bounce();

				randGetroffen = rGetroffen(primaryStage); // randgetroffen?
				
				if (randGetroffen) 
					directionChange(ball.getDirectionX(), ball.getDirectionY());
				
				controllerGetroffen = cGetroffen();
				
				if (controllerGetroffen) 
					directionChange(ball.getDirectionX());
				
				// ComputerAi.makeDecision(ball, controller2);
				updateScore(primaryStage);

				draw();
			}
		};
		animator.start();
	}

	public static void main(String[] args) {
		launch(args);
	} // end of main

	public void checkInScreenController(Stage primaryStage) {
		if (controller1.getY() <= 10)
			coutofScreen1Up = true;
		else
			coutofScreen1Up = false;

		if (controller1.getY() >= primaryStage.getHeight() - 90)
			coutOfScreen1Down = true;
		else
			coutOfScreen1Down = false;

		if (controller2.getY() <= 10)
			cCutOfScreen2Up = true;
		else
			cCutOfScreen2Up = false;

		if (controller2.getY() >= primaryStage.getHeight() - 90)
			coutOfScreen2Down = true;
		else
			coutOfScreen2Down = false;
	}

	public void directionChange(int directionx, int directiony) {
		// ball.setDirectionx(directionx);
		ball.setDirectionY(directiony);
	}

	public void directionChange(int directionx) {
		ball.setDirectionX(directionx);
	}

	public boolean cGetroffen() { // hat der Ball den Controller getroffen?
		if ((ball.getCX() - ball.getRadius()) <= (controller1.getX() + controller1.getW())
				&& (ball.getCX() - ball.getRadius()) >= controller1.getX() && ball.getCY() >= controller1.getY()
				&& ball.getCY() <= (controller1.getY() + controller1.getH())) {
			return true; // linker controller
		}
		if ((ball.getCX() + ball.getRadius()) >= controller2.getX()
				&& (ball.getCX() + ball.getRadius()) <= controller2.getX() + controller2.getW()
				&& ball.getCY() >= controller2.getY() && ball.getCY() <= (controller2.getY() + controller2.getH())) {
			return true;
		} else
			return false;
	}

	public boolean rGetroffen(Stage primaryStage) {
		double y = ball.getCY() + ball.getRadius(); // radius mit beachten

		if (y <= 0) {
			return true;
		}
		if (y >= primaryStage.getHeight()) {
			return true;
		}
		return false;
	}

	public boolean goalOne(Stage primaryStage) { // spieler eins schießt ein Tor
		if (ball.getCX() > primaryStage.getWidth()) {
			return true;
		}
		return false;
	}

	public boolean goalTwo(Stage primaryStage) {
		if (ball.getCX() < 0) {
			return true;
		}
		return false;
	}

	public void show() {
		rectangle1.setX(controller1.getX());
		rectangle1.setY(controller1.getY() - 50);
		rectangle1.setHeight(controller1.getH());
		rectangle1.setWidth(controller1.getW()); // controller 1

		rectangle2.setX(controller2.getX());
		rectangle2.setY(controller2.getY() - 50);
		rectangle2.setHeight(controller2.getH());
		rectangle2.setWidth(controller2.getW()); // controller 2

		circle.setCenterX(ball.getCX());
		circle.setCenterY(ball.getCY());
		circle.setRadius(ball.getRadius()); // ball
	}

	public void draw() {
		controller1.updateVel();
		controller2.updateVel();
		rectangle1.setY(controller1.getY());
		rectangle2.setY(controller2.getY()); // bewegen der Controller
		circle.setCenterX(ball.moveCX(ball.getDirectionX()));
		circle.setCenterY(ball.moveCY(ball.getDirectionY())); // bewegen des Balles
	}

	public void updateScore(Stage prim) {
		if (goalOne(prim)) {
			score1Temp++;
			score1.setText("" + score1Temp);
			firstStart = true;
			ball.reset();
		}
		
		if (goalTwo(prim)) {
			score2Temp++;
			score2.setText("" + score2Temp);
			firstStart = true;
			ball.reset();
		}
	}

	public void prepareScore() {
		score1.setText("" + score1Temp);
		score2.setText("" + score2Temp);
		score1.setLayoutY(125);
		score2.setLayoutY(125);
		score1.setFont(new Font(50));
		score2.setFont(new Font(50));
	}

	public void setColors() {
		rectangle1.setFill(forShapes);
		circle.setFill(forShapes);
		rectangle2.setFill(forShapes);
		score1.setFill(forShapes);
		score2.setFill(forShapes);
	}

	public Color colorChange() {
		Random random = new Random();
		double r = random.nextDouble();
		double g = random.nextDouble();
		double b = random.nextDouble();
		return new Color(r, g, b, 1);
	}
}