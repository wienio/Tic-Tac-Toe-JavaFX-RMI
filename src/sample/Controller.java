package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private TextField textField;
    @FXML
    private Button serverButton;
    @FXML
    private Button clientButton;
    @FXML
    private AnchorPane panel;
    @FXML
    private Label endGame;
    @FXML
    private Label whoWin;

    private List<Rectangle> rectangles = new ArrayList<>(9);
    private List<Cell> cellList = new ArrayList<>(9);
    private boolean yourTurn;

    private Image imgX = new Image("/sample/X.png");
    private Image imgO = new Image("/sample/O.png");

    private TicTacToeServer server = null;
    private TicTacToeClient client = null;

    @FXML
    public void initialize() {
        createTable();
        endGame.setVisible(false);
        whoWin.setVisible(false);

        for (Rectangle rectangle : rectangles) {
            rectangle.setOnMouseClicked(event -> {
                if (cellList.get(rectangles.indexOf(rectangle)).getStatus() == 0 && client != null && yourTurn) {
                    cellList.get(rectangles.indexOf(rectangle)).setStatus(1);
                    rectangle.setFill(new ImagePattern(imgX));
                    rectangle.setDisable(true);
                    client.sendToServer(1, rectangles.indexOf(rectangle));
                    yourTurn = false;
                } else if (cellList.get(rectangles.indexOf(rectangle)).getStatus() == 0 && server != null && yourTurn) {
                    cellList.get(rectangles.indexOf(rectangle)).setStatus(2);
                    rectangle.setFill(new ImagePattern(imgO));
                    rectangle.setDisable(true);
                    server.sendToClient(2, rectangles.indexOf(rectangle));
                    yourTurn = false;
                }
                if (checkForEndGame(1) == 1) {
                    endGame.setVisible(true);
                    whoWin.setVisible(true);
                    whoWin.setText("WYGRAŁEŚ!");
                    for (Rectangle rect : rectangles) {
                        rect.setDisable(true);
                    }
                } else if (checkForEndGame(2) == 2) {
                    endGame.setVisible(true);
                    whoWin.setVisible(true);
                    whoWin.setText("WYGRAŁEŚ!");
                    for (Rectangle rect : rectangles) {
                        rect.setDisable(true);
                    }
                }
            });
        }
    }

    @FXML
    private void handleServerButton() {
        try {
            server = new TicTacToeServer();
            clientButton.setDisable(true);
            serverButton.setDisable(true);
            textField.setDisable(true);
            yourTurn = true;
            server.setGuiCellListener(event -> {
                rectangles.get(event.getIndex()).setFill(new ImagePattern(imgX));
                rectangles.get(event.getIndex()).setDisable(true);
                cellList.get(event.getIndex()).setStatus(event.getStatus());
                yourTurn = true;
                if (checkForEndGame(1) == 1) {
                    for (Rectangle rect : rectangles) {
                        rect.setDisable(true);
                    }
                    endGame.setVisible(true);
                    whoWin.setVisible(true);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClientButton() {
        try {
            client = new TicTacToeClient(textField);
            clientButton.setDisable(true);
            serverButton.setDisable(true);
            textField.setDisable(true);
            yourTurn = false;
            client.setGuiCellListener(event -> {
                rectangles.get(event.getIndex()).setFill(new ImagePattern(imgO));
                rectangles.get(event.getIndex()).setDisable(true);
                cellList.get(event.getIndex()).setStatus(event.getStatus());
                yourTurn = true;
                if (checkForEndGame(2) == 2) {
                    for (Rectangle rect : rectangles) {
                        rect.setDisable(true);
                    }
                    endGame.setVisible(true);
                    whoWin.setVisible(true);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                Rectangle rect = new Rectangle(145, 145);
                rect.setLayoutX(14 + 160 * j);
                rect.setLayoutY(14 + 156 * i);
                rect.setFill(Color.GREEN);
                rectangles.add(rect);
            }
        }
        for (int i = 0; i < 9; ++i) {
            cellList.add(new Cell(this, 0, i));
        }
        panel.getChildren().addAll(rectangles);
    }

    private int checkForEndGame(int status) {
        int counter = 0;
        for (int i = 0; i < 3; ++i) {    // poziomo dla O
            for (int j = 3 * i; j < 3 + 3 * i; ++j) {
                if (cellList.get(j).getStatus() == status) {
                    counter++;
                } else {
                    counter = 0;
                    break;
                }
                if (counter == 3) {
                    return status;
                }
            }
        }

        counter = 0;
        for (int i = 0; i < 3; ++i) {     // pionowo dla 0
            for (int j = i; j < i + 7; j += 3) {
                if (cellList.get(j).getStatus() == status) {
                    counter++;
                } else {
                    counter = 0;
                    break;
                }
                if (counter == 3) {
                    return status;
                }
            }
        }
        if (cellList.get(0).getStatus() == status && cellList.get(4).getStatus() == status && cellList.get(8).getStatus() == status) {   // ukośnie
            return status;
        }
        if (cellList.get(2).getStatus() == status && cellList.get(4).getStatus() == status && cellList.get(6).getStatus() == status) {   // ukośnie
            return status;
        }

        for (int i = 0; i < 9; ++i) {
            if (cellList.get(i).getStatus() == 0) return 0;
        }

        Platform.runLater(() -> {
            endGame.setVisible(true);
            whoWin.setVisible(true);
            whoWin.setText("REMIS!");
        });
        return 0;
    }
}
