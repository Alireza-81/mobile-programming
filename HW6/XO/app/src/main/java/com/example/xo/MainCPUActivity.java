package com.example.xo;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainCPUActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private boolean playerIsX = true;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xo_cpu);

        dbHelper = new DatabaseHelper(this);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + (i * 3 + j + 1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }


    }
    public void onPlayerChoice(View v) {
        if (v.getId() == R.id.buttonChooseX) {
            player1Turn = true;
            playerIsX = true;
        } else if (v.getId() == R.id.buttonChooseO) {
            playerIsX = false;
            player1Turn = false;
            cpuTurn();
        }
        // Disable choice buttons after choosing
        findViewById(R.id.buttonChooseX).setEnabled(false);
        findViewById(R.id.buttonChooseO).setEnabled(false);
        saveGameState();
    }





    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        if (!button.getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            button.setText(playerIsX ? "X" : "O");
            roundCount++;
            player1Turn = false;
        }

        if (checkForWin()) {
            if (playerIsX) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            cpuTurn();
        }
        saveGameState();
    }

    private boolean moveWinsGame(Button button, String player) {
        button.setText(player);
        boolean wins = checkForWin();
        button.setText("");  // Reset the button text after the check
        return wins;
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
                if (field[i][j].equals("")) {
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }



        return false;
    }

    private void cpuTurn() {
        Button bestMove = null;
        boolean allCellsFilled = true;

        // Try to win
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    allCellsFilled = false;
                    if (playerIsX) {
                        if (moveWinsGame(buttons[i][j], "O")) {
                            bestMove = buttons[i][j];
                            break;
                        }
                    } else {
                        if (moveWinsGame(buttons[i][j], "X")) {
                            bestMove = buttons[i][j];
                            break;
                        }
                    }
                }
            }
            if (bestMove != null) break;
        }

        // Block Player 1's winning move
        if (bestMove == null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        allCellsFilled = false;
                        if (playerIsX) {
                            if (moveWinsGame(buttons[i][j], "X")) {
                                bestMove = buttons[i][j];
                                break;
                            }
                        } else {
                            if (moveWinsGame(buttons[i][j], "O")) {
                                bestMove = buttons[i][j];
                                break;
                            }
                        }
                    }
                }
                if (bestMove != null) break;
            }
        }


        if (bestMove == null) {
            if (!allCellsFilled) {
                List<Button> emptyButtons = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (buttons[i][j].getText().toString().equals("")) {
                            emptyButtons.add(buttons[i][j]);
                        }
                    }
                }
                if (!emptyButtons.isEmpty()) {
                    int index = (int) (Math.random() * emptyButtons.size());
                    bestMove = emptyButtons.get(index);
                }
            }
        }



        if (bestMove != null) {
            bestMove.setText(playerIsX ? "O" : "X");
            roundCount++;
        }

        if (checkForWin()) {
            player2Wins();
        } else if (roundCount == 9 || allCellsFilled) {
            draw();
        } else {
            player1Turn = true;
        }
    }

    private void player1Wins() {
        Toast.makeText(this, "Player 1 won against CPU", Toast.LENGTH_SHORT).show();
        dbHelper.saveGameResult("Player 1 won against CPU");
        resetBoard();
    }

    private void player2Wins() {
        Toast.makeText(this, "CPU won", Toast.LENGTH_SHORT).show();
        dbHelper.saveGameResult("CPU won");
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        dbHelper.saveGameResult("Draw against player 1 and CPU");
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        if (playerIsX) {
            player1Turn = true;
        } else {
            player1Turn = false;
        }

        roundCount = 0;
    }

    private void saveGameState() {
        StringBuilder stateBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                stateBuilder.append(buttons[i][j].getText()).append(",");
            }
        }
        stateBuilder.append(player1Turn ? "1" : "0");

        dbHelper.saveGameState(stateBuilder.toString(), playerIsX);
    }

    private void loadGameState() {
        String state = dbHelper.loadGameState();
        if (state != null && !state.isEmpty()) {
            String[] parts = state.split(",");
            int index = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText(parts[index++]);
                }
            }
            player1Turn = "1".equals(parts[9]);
            playerIsX = Boolean.parseBoolean(parts[10]);
        }
    }

    private void updateUIFromGameState() {
        String state = dbHelper.loadGameState();
        if (state != null && !state.isEmpty()) {
            String[] parts = state.split(",");
            int index = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText(parts[index++]);
                }
            }
            player1Turn = "1".equals(parts[9]);
            if (!player1Turn && !playerIsX) {
                cpuTurn();
            }
        }
    }

    public void loadGameOnClick(View view) {
        loadGameState();
        updateUIFromGameState();
    }




}
