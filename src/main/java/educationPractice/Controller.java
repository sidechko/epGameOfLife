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

public class Controller implements Initializable {
    public static Board mainBoard;
    public static GridPane Grid;
    public static Rules mainRules = new Rules(2, 3);

    public Button STARTBUTTON;
    public TextField BWIDTH;
    public TextField BHEIGHT;
    public TextField LCC;
    public ScrollPane SCROLLPANEL;
    public Slider SLIDER;
    public TextField UPDATEPERIOD;

    public static boolean isStarted;
    /**
     * Параметры полученые из текста текстовых полей или слайдеров.
     * bW - {@link Controller#BWIDTH};
     * bH - {@link Controller#BHEIGHT};
     * lCC - {@link Controller#LCC};
     * scale - {@link Controller#SLIDER};
     * period - {@link Controller#UPDATEPERIOD};
     */
    static int bW;
    static int bH;
    static int lCC;
    static int scale;
    static int period;

    /**
     * Массив прямоугольников, которые используються для выведения в Grid.
     */
    public static Rectangle[][] rectangles;
    public static Thread thread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reloadData();
    }

    /**
     * Перезагружает параметры доски, сетки, размера.
     */
    public void reloadData() {
        isStarted = false;

        bW = Integer.parseInt(BWIDTH.getText());
        bH = Integer.parseInt(BHEIGHT.getText());
        lCC = Integer.parseInt(LCC.getText());
        period = Integer.parseInt(UPDATEPERIOD.getText());
        getScale();

        mainBoard = new Board(bW, bH);
        mainBoard.generate(lCC);

        rectangles = new Rectangle[bW][bH];
        Grid = newGridPane();
        generateGrid(Grid);
        reloadRectangles();

        SCROLLPANEL.setContent(Grid);
    }

    /**
     * Метод вызываемый кнопкой Next Step, который переведет доску в следующую итерацию.
     */
    public void next() {
        if (isStarted) return;
        stepper();
    }

    /**
     * Метод для выполнения сдледующий итерации доски.
     */
    public void stepper() {
        mainBoard.nextStep(mainRules);
        reloadRectangles();
    }

    /**
     * Метод вызываемый при клике кнопки старта/остановки ({@link Controller#STARTBUTTON}).
     */
    public synchronized void startOrStop() {
        String buttonStyle = "-fx-background-color: ";
        isStarted = !isStarted;

        String buttonText = isStarted ? "Stop" : "Start";
        buttonStyle += isStarted ? "#ffa1a1" : "#a5ffa1";

        this.STARTBUTTON.setText(buttonText);
        this.STARTBUTTON.setStyle(buttonStyle);

        thread = new Thread(() -> {
            while(isStarted){
                try {
                    Thread.sleep(period);
                    stepper();
                } catch (Exception ignored) {
                }
            }
        });

        if(!isStarted) thread.stop();
        else thread.start();
    }

    /**
     * Метод для изменения цвета всех элементов {@link Controller#rectangles} изходя из {@link Controller#mainBoard}
     */
    public void reloadRectangles() {
        for (int h = 0; h < bH; h++) {
            for (int w = 0; w < bW; w++) {
                String fill = mainBoard.cellsData[w][h] ? "#a5ffa1" : "#111111";
                rectangles[w][h].setFill(Paint.valueOf(fill));
            }
        }
    }

    /**
     * Метод для заполнения нашего {@link Controller#Grid} квадратами.
     *
     * @param grid Панель на которую мы хотим отобразить данные из {@link Controller#mainBoard}.
     */
    public void generateGrid(GridPane grid) {
        grid.setPadding(new Insets(0));
        grid.setHgap(2);
        grid.setVgap(2);

        for (int h = 0; h < bH; h++) {
            for (int w = 0; w < bW; w++) {
                rectangles[w][h] = new Rectangle();
                rectangles[w][h].setHeight(scale);
                rectangles[w][h].setWidth(scale);
                grid.add(rectangles[w][h], h, w);
            }
        }

        for (Node n : grid.getChildren()) {
            GridPane.setHalignment(n, HPos.RIGHT);
            GridPane.setValignment(n, VPos.BOTTOM);
        }

    }

    /**
     * Метод для создания новой GridPane.
     * На ней в последвтие будут отображаться клетки из {@link Controller#mainBoard}.
     *
     * @return Возвращает GridPane
     */
    public GridPane newGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPrefSize(800, 800);
        grid.setStyle("-fx-background-color: #121212;");
        grid.setHgap(2);
        grid.setVgap(2);
        return grid;
    }

    /**
     * Метод, для получение размера из {@link Controller#SLIDER} и последующая запись в {@link Controller#scale}.
     */
    public void getScale() {
        scale = (int) SLIDER.getValue();
    }

    /**
     * Метод для изменение размера ВСЕХ клеток в {@link Controller#rectangles} исходя из {@link Controller#scale}, некий зум.
     */
    public void cellResize() {
        for (int h = 0; h < bH; h++) {
            for (int w = 0; w < bW; w++) {
                rectangles[w][h].setHeight(scale);
                rectangles[w][h].setWidth(scale);
            }
        }
    }

    /**
     * Метод, который вызываеться при изменение {@link Controller#SLIDER}
     */
    public void resize() {
        getScale();
        cellResize();
    }
}
