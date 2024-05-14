package com.example.xo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainCPUActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private boolean playerIsX = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xo_cpu);

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
            player1Turn = true;  // Let the player still make the first move
            playerIsX = false;
        }
        // Disable choice buttons after choosing
        findViewById(R.id.buttonChooseX).setEnabled(false);
        findViewById(R.id.buttonChooseO).setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (playerIsX) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }
        roundCount++;

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

        // Try to win
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
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


        if (bestMove != null) {
            if (playerIsX) {
                bestMove.setText("O");
            } else {
                bestMove.setText("X");
            }
            roundCount++;

            if (checkForWin()) {
                player2Wins();
            } else if (roundCount == 9) {
                draw();
            }
        }
    }

    private void player1Wins() {
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void player2Wins() {
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
    }
}
