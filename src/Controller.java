import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button dPRollButton;
    public Button initRollButton;
    public Button d100RollButton;
    public Label dPRessLabel;
    public Label initRessLabel;
    public TextField dPGetField;
    public TextField diffGetField;
    public CheckBox botchGetBox;
    public CheckBox specGetBox;
    public TextField initBonusGetField;
    public Label d100RessLabel;
    public javafx.scene.layout.Pane Pane;
    boolean spec = false;
    boolean botchable = true;
    //Optional key combinations for Roll buttons
    final KeyCombination keyCombinationCtrlEnter = new KeyCodeCombination(
            KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
    final KeyCombination keyCombinationShiftEnter = new KeyCodeCombination(
            KeyCode.ENTER, KeyCombination.SHIFT_DOWN);

    //roll methods of buttons
    public void rollDP() {
        try {
            int dAmount = Integer.parseInt(dPGetField.getText());
            if (dAmount<23&&dAmount>0){
                ArrayList<Integer> dP = dRoller(dAmount);
                botchable = botchGetBox.isSelected();
                spec = specGetBox.isSelected();
                int ressDP = result(dP, botchable, spec);
                String ress;

                if (ressDP < 0 && botchable) {
                    ress = "Botch!";
                } else if (ressDP <= 0) {
                    ress = "No Successes!";
                } else {
                    if (ressDP == 1) {
                        ress = ressDP + " Success";
                    } else {
                        ress = ressDP + " Successes";
                    }
                }
                dPRessLabel.setFont(Font.font("Claws", 12));
                dPRessLabel.setText(String.valueOf(dP) + '\n' + ress);
            }else throw new NumberFormatException();
        } catch (NumberFormatException e) {
            dPRessLabel.setFont(Font.font("Malkavian Font", 12));
            dPRessLabel.setText("Please write an acceptable Dice Pool or Difficulty!");
        }
    }
    public void rollInit() {
        try {
            Random random = new Random();
            int ressInit = Integer.parseInt(initBonusGetField.getText()) + random.nextInt(10) + 1;
            initRessLabel.setFont(Font.font("Claws", 12));
            initRessLabel.setText("Initiative: " + ressInit);
        } catch (NumberFormatException e) {
            initRessLabel.setFont(Font.font("Malkavian Font", 12));
            initRessLabel.setText("Please write an acceptable initiative bonus!");
        }
    }
    public void rollD100() {
        Random random = new Random();
        int ressD100 = random.nextInt(100) + 1;
        d100RessLabel.setText(Integer.toString(ressD100));
    }

    //Creates a sorted Dice Pool Arraylist<Integer>
    public ArrayList<Integer> dRoller(int diceAmount) {
        Random random = new Random();
        int i;
        ArrayList<Integer> resultArrayList = new ArrayList<>();
        for (i = 0; i < diceAmount; i++) {
            resultArrayList.add(random.nextInt(10) + 1);
        }
        Collections.sort(resultArrayList);
        return resultArrayList;
    }
    public int numOfSuccess(ArrayList<Integer> dicePool, int difficulty) {
        int numOS = 0;
        for (Integer integer : dicePool) {
            if (integer >= difficulty) {
                numOS++;
            }
        }
        return numOS;
    }
    //Returns Dice Pool result as int
    public int result(ArrayList<Integer> DP, boolean botchable, boolean specialized) {
        int result;
        try {
            String tempDiff = diffGetField.getText();
            if(tempDiff.equals("")||(Integer.parseInt(tempDiff) < 11 && Integer.parseInt(tempDiff) > 1)) {
                int diff = (tempDiff.equals("") ? 6 : Integer.parseInt(tempDiff));
                result = numOfSuccess(DP, diff);
                int ones = Collections.frequency(DP, 1);
                int tens = Collections.frequency(DP, 10);

                if (botchable && specialized) {
                    result -= ones;
                    if (tens >= ones) {
                        int leftTens = tens - ones;
                        result += leftTens;
                    }
                } else {
                    if (botchable) result -= ones;
                    if (specialized) result += tens;
                }
            } else throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return result;
    }

    public void shortCutsForPane() {
        Pane.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (keyCombinationShiftEnter.match(key)) {
                rollD100();
            } else if (keyCombinationCtrlEnter.match(key)) {
                rollInit();
            } else if (key.getCode() == KeyCode.ENTER) {
                rollDP();
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}