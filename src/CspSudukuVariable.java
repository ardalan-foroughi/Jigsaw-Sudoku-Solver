import java.util.ArrayList;
import java.util.LinkedList;

public class CspSudukuVariable {
    private int row = -1;
    private int column = -1;
    private ArrayList<Integer> possibleValues;
    private int area = -1;
    private int numberOfConstrainedValues = -1;
    private int value = 0;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNumberOfConstrainedVariables() {
        return numberOfConstrainedValues;
    }

    public void setNumberOfConstrainedValues(int numberOfConstrainedValues) {
        this.numberOfConstrainedValues = numberOfConstrainedValues;
    }

    public CspSudukuVariable(int row, int column, int area){
        this.row = row;
        this.column = column;
        this.area = area;
        possibleValues = new ArrayList<>();
    }

    public void addPossibleValue(int number){
        possibleValues.add(number);
    }
//    public void removePossibleValue(int number){
//        possibleValues.remove(possibleValues.)
//    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public ArrayList<Integer> getPossibleValues() {
        return possibleValues;
    }

    public int getArea() {
        return area;
    }
}
