
public class House {
    private int row = -1;
    private int col = -1;
    private int number = 0;
    private int areaNumber = -1;

    public House(int row, int col, int number, int areaNumber) {
        this.row = row;
        this.col = col;
        this.number = number;
        this.areaNumber = areaNumber;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNumber() {
        return number;
    }

    public int getAreaNumber() {
        return areaNumber;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
