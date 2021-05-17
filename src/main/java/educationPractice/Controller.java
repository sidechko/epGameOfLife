package educationPractice;

import educationPractice.model.Board;
import educationPractice.model.Rules;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {
    public static Board mainBoard;
    public static GridPane Grid;

    public Button STARTBUTTON;
    public TextField BWIDTH;
    public TextField BHEIGHT;
    public TextField LCC;
    public ScrollPane SCROLLPANEL;
    public Slider SLIDER;

    public static boolean isStarted = false;
    static int bW;
    static int bH;
    static int lCC;
    static int scale;
    public static Rules mainRules = new Rules(2, 3);
    public static Rectangle[][] rectangles;
    public static int period = 50;


    public Timer timer = new Timer(true);
    public TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (!isStarted) return;
            stepper();
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reloadData();
        timer.scheduleAtFixedRate(timerTask, 0, period);
    }

    public void reloadData() {
        isStarted = false;

        bW = Integer.parseInt(BWIDTH.getText());
        bH = Integer.parseInt(BHEIGHT.getText());
        lCC = Integer.parseInt(LCC.getText());
        getScale();

        mainBoard = new Board(bW, bH);
        mainBoard.generate(lCC);

        rectangles = new Rectangle[bW][bH];
        Grid = newGridPane();
        generateGrid(Grid);

        SCROLLPANEL.setContent(Grid);
    }

    //###########################################

    public void startOrStop() {
        buttonRevert();
    }

    public void next(){
        if (isStarted) return;
        stepper();
    }

    public void stepper(){
        mainBoard.nextStep(mainRules);
        reloadRectangles();
    }

    public void buttonRevert() {
        String buttonStyle = "-fx-background-color: ";
        isStarted = !isStarted;

        String buttonText = isStarted ? "Stop" : "Start";
        buttonStyle += isStarted ? "#ffa1a1" : "#a5ffa1";

        this.STARTBUTTON.setText(buttonText);
        this.STARTBUTTON.setStyle(buttonStyle);
    }

    //###########################################

    public void reloadRectangles() {
        for (int h = 0; h < bH; h++) {
            for (int w = 0; w < bW; w++) {
                String fill = mainBoard.cellsData[w][h] ? "#a5ffa1":"#111111";
                rectangles[w][h].setFill(Paint.valueOf(fill));
            }
        }
    }

    public void generateGrid(GridPane grid) {
        grid.setPadding(new Insets(0));
        grid.setHgap(2);
        grid.setVgap(2);

        for (int h = 0; h < bH; h++) {
            for (int w = 0; w < bW; w++) {
                rectangles[w][h] = new Rectangle();
                rectangles[w][h].setHeight(scale);
                rectangles[w][h].setWidth(scale);

                if (mainBoard.cellsData[w][h]) rectangles[w][h].setFill(Paint.valueOf("#a5ffa1"));
                else rectangles[w][h].setFill(Paint.valueOf("#111111"));

                grid.add(rectangles[w][h], h, w);
            }
        }

        for (Node n: grid.getChildren()) {
            GridPane.setHalignment(n, HPos.RIGHT);
            GridPane.setValignment(n, VPos.BOTTOM);
        }

    }

    public GridPane newGridPane(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPrefSize(800,800);
        grid.setStyle("-fx-background-color: #121212;");
        grid.setHgap(2);
        grid.setVgap(2);
        return grid;
    }

    //###########################################

    public void getScale(){
        scale = (int)SLIDER.getValue();
    }

    public void resize() {
        getScale();
        cellResize();
    }

    public void cellResize() {
        for (int h = 0; h < bH; h++) {
            for (int w = 0; w < bW; w++) {
                rectangles[w][h].setHeight(scale);
                rectangles[w][h].setWidth(scale);
            }
        }
    }
}
