import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;

import java.util.Random;
import javafx.animation.AnimationTimer;

/**
 *
 * Beschreibung
 *
 * @version 1.4 vom 21.11.2018
 * @author Maximilian Bieleke
 */

public class Oberflaeche extends Application {
	private Circle circle1 = new Circle();
	private Rectangle rectangle1 = new Rectangle();
	private Rectangle rectangle2 = new Rectangle();
	private Controller controller1 = new Controller(50, 360);
	private Controller controller2 = new Controller(1205, 360);
	private Text score1 = new Text();
	private int score1temp = 0;
	private int score2temp = 0;
	private Text score2 = new Text();
	private Ball ball1 = new Ball(640, 360);
	private boolean upispressed = false;
	private boolean downispressed = false;
	private boolean wispressed = false;
	private boolean sispressed = false;
	private boolean randGetroffen = false;
	private boolean controllerGetroffen = false;
	private boolean coutofScreen1up = false; // controller 1 nach oben aus dem Bild
	private boolean coutofScreen2up = false; // controller 2 nach oben aus dem Bild
	private boolean coutofScreen1down = false; // controller 1 nach untem aus dem Bild
	private boolean coutofScreen2down = false; // controller 2 nach untem aus dem Bild
	private boolean firststart = true;
	private long start = 0;
	private long iteration = 0;
	private long seconds = 0;
	private Color forshapes = Color.WHITE;

	private ai ComputerAi = new ai();

	//meine extras
	private long renderingStart = 0;

	public void start(Stage primaryStage) {
		Pane root = new Pane();
		Scene scene = new Scene(root, 520, 304, Color.BLACK);

		primaryStage.setFullScreen(true);
		root.getChildren().add(circle1);
		root.getChildren().add(rectangle1);
		root.getChildren().add(rectangle2);
		root.getChildren().add(score1);
		root.getChildren().add(score2);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					upispressed = true;
					break; // controller2.move(1);
				case DOWN:
					downispressed = true;
					break; // controller2.move(-1);
				case W:
					wispressed = true;
					break; // controller1.move(1);
				case S:
					sispressed = true;
					break; // controller1.move(-1);
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
					upispressed = false;
					break;
				case DOWN:
					downispressed = false;
					break;
				case W:
					wispressed = false;
					break;
				case S:
					sispressed = false;
					break;
				default:
					break;
				}
			}
		});
		preparescore();
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
				iteration = System.currentTimeMillis();
				System.out.println(iteration - start);

				seconds = Math.round((iteration - start) / 1000);

				if (seconds == 15) {
					if (ball1.getRADIUS() > 5) {
						controller1.setDeltaY(controller1.getnewDeltaY());
						controller2.setDeltaY(controller2.getnewDeltaY());
						ball1.decreaseRadius();
						circle1.setRadius(ball1.getRADIUS());
						circle1.setFill(colorChange());
						start = System.currentTimeMillis();
					}
				}
				if (score1.getText().equals("" + 5) || score2.getText().equals("" + 5)) {
					new Thread(sleeper).run();
				}
				if (firststart) {
					show(); // deckt objekte auf
					//
					firststart = false;
				}

				checkinScreencontroller(primaryStage); // guckt ob alle
														// controller im Bild
														// sind
				if (upispressed && !coutofScreen2up) {
					controller2.move(1);
				}
				if (downispressed && !coutofScreen2down) {
					controller2.move(-1);
				}
				if (wispressed && !coutofScreen1up) {
					controller1.move(1);
				}
				if (sispressed && !coutofScreen1down) {
					controller1.move(-1);
				}
				randGetroffen = rgetroffen(primaryStage); // randgetroffen?
				if (randGetroffen) {
					directionChange(ball1.getDirectionx(), ball1.getDirectiony());
				}
				controllerGetroffen = cgetroffen();
				if (controllerGetroffen) {
					directionChange(ball1.getDirectionx());
				}
				ComputerAi.makeDecision(ball1, controller2);
				updateScore(primaryStage);

				draw();
			}
		};
		animator.start();
	}

	public static void main(String[] args) {
		launch(args);
	} // end of main

	public void show() {
		rectangle1.setX(controller1.getX());
		rectangle1.setY(controller1.getY() - 50);
		rectangle1.setHeight(controller1.getH());
		rectangle1.setWidth(controller1.getW()); // controller 1

		rectangle2.setX(controller2.getX());
		rectangle2.setY(controller2.getY() - 50);
		rectangle2.setHeight(controller2.getH());
		rectangle2.setWidth(controller2.getW()); // controller 2

		circle1.setCenterX(ball1.getCX());
		circle1.setCenterY(ball1.getCY());
		circle1.setRadius(ball1.getRADIUS()); // ball 1
	}

	public void checkinScreencontroller(Stage primaryStage) {
		if (controller1.getY() <= 0) {
			coutofScreen1up = true;
		} else
			coutofScreen1up = false;
		if (controller1.getY() >= primaryStage.getHeight() - 100) {
			coutofScreen1down = true;
		} else
			coutofScreen1down = false;
		if (controller2.getY() <= 0) {
			coutofScreen2up = true;
		} else
			coutofScreen2up = false;
		if (controller2.getY() >= primaryStage.getHeight() - 100) {
			coutofScreen2down = true;
		} else
			coutofScreen2down = false;
	}

	public boolean rgetroffen(Stage primaryStage) {
		double y = ball1.getCY();
		if (y <= 0) {
			return true;
		}
		if (y >= primaryStage.getHeight()) {
			return true;
		}
		return false;
	}

	public void directionChange(int directionx, int directiony) {
		// ball1.setDirectionx(directionx);
		ball1.setDirectiony(directiony);
	}

	public void directionChange(int directionx) {
		ball1.setDirectionx(directionx);
	}

	public boolean cgetroffen() { // hat der Ball den Controller getroffen?
		if ((ball1.getCX() - ball1.getRADIUS()) <= (controller1.getX() + controller1.getW())
				&& (ball1.getCX() - ball1.getRADIUS()) >= controller1.getX() && ball1.getCY() >= controller1.getY()
				&& ball1.getCY() <= (controller1.getY() + controller1.getH())) {
			return true; // linker controller
		}
		if ((ball1.getCX() + ball1.getRADIUS()) >= controller2.getX()
				&& (ball1.getCX() + ball1.getRADIUS()) <= controller2.getX() + controller2.getW()
				&& ball1.getCY() >= controller2.getY() && ball1.getCY() <= (controller2.getY() + controller2.getH())) {
			return true;
		} else
			return false;
	}

	public void updateScore(Stage prim) {
		if (goalOne(prim)) {
			score1temp++;
			score1.setText("" + score1temp);
			firststart = true;
			ball1.reset();
		}
		if (goalTwo(prim)) {
			score2temp++;
			score2.setText("" + score2temp);
			firststart = true;
			ball1.reset();
		}
	}

	public boolean goalOne(Stage primaryStage) { // spieler eins schießt ein Tor
		if (ball1.getCX() > primaryStage.getWidth()) {
			return true;
		}
		return false;
	}

	public boolean goalTwo(Stage primaryStage) {
		if (ball1.getCX() < 0) {
			return true;
		}
		return false;
	}

	public void draw() {
		rectangle1.setY(controller1.getY());
		rectangle2.setY(controller2.getY()); // bewegen der Controller
		circle1.setCenterX(ball1.movecx(ball1.getDirectionx()));
		circle1.setCenterY(ball1.movecy(ball1.getDirectiony())); // bewegen des
																	// Balles
	}

	public void preparescore() {
		score1.setText("" + score1temp);
		score2.setText("" + score2temp);
		score1.setLayoutY(125);
		score2.setLayoutY(125);
		score1.setFont(new Font(50));
		score2.setFont(new Font(50));
	}

	public void setColors() {
		rectangle1.setFill(forshapes);
		circle1.setFill(forshapes);
		rectangle2.setFill(forshapes);
		score1.setFill(forshapes);
		score2.setFill(forshapes);
	}

	public Color colorChange() {
		Random random = new Random();
		double r = random.nextDouble();
		double g = random.nextDouble();
		double b = random.nextDouble();
		return new Color(r, g, b, 1);
	}
}