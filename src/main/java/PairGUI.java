import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.reactfx.util.FxTimer;

import java.util.ArrayList;

public class PairGUI extends Application {
    private static final double MIN_WIDTH = 0;
    private static final double SPACING = 10;
    private static final String DEFAULT_CHAR = "*";
    private static final long HIDE_DELAY = 200;
    private static final double BUTTON_SIDE = 40;
    private static int N = 2;

    private Field field;
    private int prevClickI, prevClickJ;
    private boolean clicked = false;
    private Button[][] buttons;
    private int score;
    private Label scoreLabel;
    private int clicks;
    private Label clicksLabel;

    public void start(Stage stage) throws Exception {
        ArrayList<Node> nodes = new ArrayList<>();
        scoreLabel = new Label();
        clicksLabel = new Label();
        nodes.add(packToRow(new Label("Your score: "), scoreLabel));
        nodes.add(packToRow(new Label("Clicks made: "), clicksLabel));

        buttons = new Button[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                buttons[i][j] = new Button(DEFAULT_CHAR);
                buttons[i][j].setFocusTraversable(false);
                buttons[i][j].setMinWidth(BUTTON_SIDE);
                buttons[i][j].setMinHeight(BUTTON_SIDE);
                int i1 = i;
                int j1 = j;
                buttons[i][j].setOnMouseClicked(event -> buttonClicked(i1, j1));
            }
            nodes.add(new HBox(SPACING, buttons[i]));
        }

        Node[] nodeArray = new Node[1];
        nodeArray = nodes.toArray(nodeArray);
        VBox vbox = new VBox(nodeArray);
        vbox.setMinWidth(MIN_WIDTH);
        vbox.setSpacing(SPACING);

        startNewGame();

        stage.setScene(new Scene(vbox));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void setClicks(int clicks) {
        this.clicks = clicks;
        clicksLabel.setText(String.valueOf(clicks));
    }

    private void startNewGame() {
        field = new Field(N);
        setScore(0);
        setClicks(0);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                buttons[i][j].setText(DEFAULT_CHAR);
                buttons[i][j].setDisable(false);
            }
        }
    }

    private void setScore(int score) {
        this.score = score;
        scoreLabel.setText(score + "/" + String.valueOf(N*N/2));
    }

    private Node packToRow(Node... nodes) {
        return new HBox(SPACING, nodes);
    }

    private void buttonClicked(int i, int j) {
        setClicks(clicks+1);
        buttons[i][j].setText(String.valueOf(field.get(i,j)));
        buttons[i][j].setDisable(true);

        if (clicked) {
            if (field.get(i, j) != field.get(prevClickI, prevClickJ)) {
                final int i1 = prevClickI;
                final int j1 = prevClickJ;
                FxTimer.runLater(java.time.Duration.ofMillis(HIDE_DELAY),
                        () -> {
                            buttons[i][j].setDisable(false);
                            buttons[i1][j1].setDisable(false);

                            buttons[i][j].setText(DEFAULT_CHAR);
                            buttons[i1][j1].setText(DEFAULT_CHAR);
                        }
                );
            }
            else {
                setScore(score+1);
                if (score == N*N / 2) {
                    Button yesButton = new Button("Yes");
                    yesButton.setOnMouseClicked(event -> startNewGame());
                    Button noButton = new Button("Yes");
                    noButton.setOnMouseClicked(event -> startNewGame());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Congratulations! You have finished this game in " +
                                    clicks + " clicks. Wanna play another one?",
                            ButtonType.NO, ButtonType.YES);
                    alert.setResizable(true);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            startNewGame();
                        }
                        if (response == ButtonType.NO) {
                            Platform.exit();
                        }
                    });
                }
            }
            clicked = false;
        }
        else {
            clicked = true;
            prevClickI = i;
            prevClickJ = j;
        }
    }

}


