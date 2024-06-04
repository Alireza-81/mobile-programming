package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText calculatorDisplay;
    private Button buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour,
            buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine,
            buttonAdd, buttonSubtract, buttonMultiply, buttonDivide, buttonEqual, buttonClear;
    private Button buttonClearHistory;
    private double operand = 0;
    private String operator = "";
    private UUID userUUID;
    private File historyFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        userUUID = UUID.randomUUID();
        historyFile = new File(getFilesDir(), userUUID + "_history.txt");

        if (!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("FileDir", "Files Directory: " + getFilesDir().getAbsolutePath());

        displayHistory();

        calculatorDisplay = findViewById(R.id.calculator_display);
        buttonZero = findViewById(R.id.button_zero);
        buttonOne = findViewById(R.id.button_one);
        buttonTwo = findViewById(R.id.button_two);
        buttonThree = findViewById(R.id.button_three);
        buttonFour = findViewById(R.id.button_four);
        buttonFive = findViewById(R.id.button_five);
        buttonSix = findViewById(R.id.button_six);
        buttonSeven = findViewById(R.id.button_seven);
        buttonEight = findViewById(R.id.button_eight);
        buttonNine = findViewById(R.id.button_nine);

        buttonZero.setOnClickListener(this);
        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);
        buttonSix.setOnClickListener(this);
        buttonSeven.setOnClickListener(this);
        buttonEight.setOnClickListener(this);
        buttonNine.setOnClickListener(this);

        buttonAdd = findViewById(R.id.button_add);
        buttonSubtract = findViewById(R.id.button_minus);
        buttonMultiply = findViewById(R.id.button_multiply);
        buttonDivide = findViewById(R.id.button_divide);
        buttonEqual = findViewById(R.id.button_equals);
        buttonClear = findViewById(R.id.button_AC);

        buttonAdd.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonEqual.setOnClickListener(this);
        buttonClear.setOnClickListener(this);

        buttonClearHistory = findViewById(R.id.clear_history);
        buttonClearHistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String currentText = calculatorDisplay.getText().toString();

        if (v.getId() == R.id.button_zero) {
            calculatorDisplay.append("0");
        } else if (v.getId() == R.id.button_one) {
            calculatorDisplay.append("1");
        } else if (v.getId() == R.id.button_two) {
            calculatorDisplay.append("2");
        } else if (v.getId() == R.id.button_three) {
            calculatorDisplay.append("3");
        } else if (v.getId() == R.id.button_four) {
            calculatorDisplay.append("4");
        } else if (v.getId() == R.id.button_five) {
            calculatorDisplay.append("5");
        } else if (v.getId() == R.id.button_six) {
            calculatorDisplay.append("6");
        } else if (v.getId() == R.id.button_seven) {
            calculatorDisplay.append("7");
        } else if (v.getId() == R.id.button_eight) {
            calculatorDisplay.append("8");
        } else if (v.getId() == R.id.button_nine) {
            calculatorDisplay.append("9");
        } else if (v.getId() == R.id.button_add) {
            operator = "+";
            operand = Double.parseDouble(currentText);
            calculatorDisplay.setText("");
        } else if (v.getId() == R.id.button_minus) {
            operator = "-";
            operand = Double.parseDouble(currentText);
            calculatorDisplay.setText("");
        } else if (v.getId() == R.id.button_multiply) {
            operator = "*";
            operand = Double.parseDouble(currentText);
            calculatorDisplay.setText("");
        } else if (v.getId() == R.id.button_divide) {
            operator = "/";
            operand = Double.parseDouble(currentText);
            calculatorDisplay.setText("");
        } else if (v.getId() == R.id.button_equals) {
            performCalculation(Double.parseDouble(currentText));
            operator = "";
        } else if (v.getId() == R.id.button_AC) {
            operand = 0;
            operator = "";
            calculatorDisplay.setText("");
        } else if (v.getId() == R.id.clear_history) {
            clearHistory();
        }

    }

    private void performCalculation(double secondOperand) {
        double result = 0;
        boolean error = false;

        switch (operator) {
            case "+":
                result = operand + secondOperand;
                break;
            case "-":
                result = operand - secondOperand;
                break;
            case "*":
                result = operand * secondOperand;
                break;
            case "/":
                if (secondOperand != 0) {
                    result = operand / secondOperand;
                } else {
                    calculatorDisplay.setText("Error");
                    error = true;
                }
                break;
        }

        if (!error) {
            double poop = operand;
            operand = result;
            calculatorDisplay.setText(String.valueOf(result));
            appendHistory(String.format("%f %s %f = %f%n", poop, operator, secondOperand, result));
        } else {
            operand = 0;
            operator = "";
        }

        calculatorDisplay.setText(String.valueOf(operand));

        displayHistory();
    }

    private void appendHistory(String text) {
        try (FileWriter writer = new FileWriter(historyFile, true)) {
            writer.append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearHistory() {
        if (historyFile.exists()) {
            if (historyFile.delete()) {
                try {
                    historyFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        displayHistory();
    }

    private void displayHistory() {
        StringBuilder historyContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyContent.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e("ReadHistory", "Error reading history file", e);
        }

        TextView historyDisplay = findViewById(R.id.history_display);
        historyDisplay.setText(historyContent.toString());
    }

}