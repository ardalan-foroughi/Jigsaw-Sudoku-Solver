import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UtilityFunctions {
    private static House[][] mapDvidedByArea = null ;
     static JFrame frame = null;
     private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void initJframe() {
        frame = new JFrame();
        frame.setLayout(new GridLayout(9,9));
        frame.setSize(900,900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static House[][] readMap(String numbersFilePath, String areaFilePath) throws FileNotFoundException {
        File numbersFile = new File(numbersFilePath);
        File areaFile = new File(areaFilePath);
        House[][] result = new House[9][9];

        //setting the houses area numbers and its numbers
        int[][] houseAreaNumbers = readMapFromFile(areaFilePath);
        int[][] houseNumbers = readMapFromFile(numbersFilePath);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = new House(i, j, houseNumbers[i][j], houseAreaNumbers[i][j]);
            }
        }
        return result;
    }

    private static int[][] readMapFromFile(String filePath) throws FileNotFoundException, IllegalStateException {
        int[][] map = new int[9][9];
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        int lineCounter = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.length() == 9) {
                for (int i = 0; i < 9; i++) {
                    int tmp = Integer.parseInt(line.substring(i, i + 1));
                    map[lineCounter][i] = tmp;
                }
            } else {
                throw new IllegalStateException();
            }
            lineCounter++;
        }
        return map;
    }

    public static void printMap(House[][] tmpMap) {
        if(tmpMap == null){
            System.out.println("null map");
            return;
        }
        for (int i = 0; i < tmpMap.length; i++) {
            for (int j = 0; j < tmpMap[i].length; j++) {
                System.out.print(tmpMap[i][j].getNumber() + " ");
            }
            System.out.println();
        }
    }

//    private static boolean checkMapSection(House house, int sectionCounter, int houseCounter, boolean[] sectionUsedNumbers) {
//        if (house.getNumber() != 0) {
//            if (sectionUsedNumbers[houseCounter] == true) {
//                return false;
//            }
//            sectionUsedNumbers[houseCounter] = true;
//        }
//        //found an empty house
//        else {
//            return false;
//        }
//        return true;
//    }

    public static boolean goalCheck(House[][] map, House[][] devidedMap) {
        if(map == null){
            return false;
        }


        for (int i = 0; i < 9; i++) {
            boolean[] rowUsedNumbers = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if(map[i][j].getNumber() == 0){
                    return false;
                }
                if( rowUsedNumbers[map[i][j].getNumber() - 1] ){
                    return  false;
                }
                rowUsedNumbers[map[i][j].getNumber() - 1] = true;
            }
        }


        for (int i = 0; i < 9; i++) {
            boolean[] colUsedNumbers = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if(map[j][i].getNumber() == 0){
                    return false;
                }
                if(true){}
                if(colUsedNumbers[map[j][i].getNumber() - 1]){
                    return false;
                }
                colUsedNumbers[map[j][i].getNumber() - 1] = true;
            }

        }

        for(int i=0;i<9;i++){
            boolean[] AreaUsedNumbers = new boolean[9];
            for (int j = 0; j < 9; j++) {
                int rowNumber = devidedMap[i][j].getRow();
                int colNumber = devidedMap[i][j].getCol();
                if(map[rowNumber][colNumber].getNumber() == 0){
                    return false;
                }
                if(AreaUsedNumbers[map[rowNumber][colNumber].getNumber() - 1]){
                    return false;
                }
                AreaUsedNumbers[map[rowNumber][colNumber].getNumber() - 1] = true;
            }
        }
        return true;
    }

    private static House[][] divideMapByArea(House[][] map) {
        House[][] result = new House[9][9];
        int[] areaCounter = new int[9];
        for (int i=0;i < map.length;i++){
            for (int j=0; j <map[i].length;j++){
                House h = map[i][j];
                int areaNumber = h.getAreaNumber() -1;
                result[areaNumber][ areaCounter[areaNumber] ] = h;
                areaCounter[areaNumber]++;
            }
        }
        return result;
    }
    public static void printDebugMap(House[][] map){
        if(map == null){
            System.out.println("null map");
            return;
        }
        mapDvidedByArea = divideMapByArea(map);
        if(frame != null){
            frame.dispose();
        }
        initJframe();
//        frame.remove(0);
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){

                if(map[i][j].getNumber() != 0){
                    JPanel p =new JPanel();
                    p.setLayout(new GridLayout(1,1));
                    JLabel l = new JLabel(map[i][j].getNumber()+"");
                    p.setBorder(BorderFactory.createLineBorder(Color.black,1));
                    l.setBorder(BorderFactory.createBevelBorder(0));
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    l.setBackground(giveColorForArea(map[i][j].getAreaNumber()));
                    l.setOpaque(true);
                    p.add(l);
                    frame.add(p);
                }
                else {
                    JPanel panel = new JPanel();
                    panel.setBorder(BorderFactory.createLineBorder(Color.black,1));
                    panel.setLayout(new GridLayout(3,3));
                    boolean[] possibleNumbers = givePossibleAnswersForAHouse(map,i,j);
                    for (int k=1;k<=9;k++){
                        JLabel l = new JLabel();
                        l.setBorder(BorderFactory.createBevelBorder(1));
                        l.setHorizontalAlignment(SwingConstants.CENTER);
                        panel.add(l);
                        l.setOpaque(true);
                        l.setBackground(giveColorForArea(map[i][j].getAreaNumber()));
                        if(possibleNumbers[k-1]){
                            l.setText(""+k);
                        }
                    }

                    frame.add(panel);

                }
            }
        }
        frame.setVisible(true);
    }


    private static boolean[] givePossibleAnswersForAHouse(House[][] map,int row,int col){
        boolean[] numbersThatArePossible = new boolean[9];
        Arrays.fill(numbersThatArePossible,true);
        // comparing available house numbers with numbers in its row and column to check what numbers are valid
        for (int offset = 0; offset < 9; offset++) {
            //moving through column
            if (map[offset][col].getNumber() != 0) {
                numbersThatArePossible[map[offset][col].getNumber() - 1] = false;
            }
            // moving through row
            if (map[row][offset].getNumber() != 0) {
                numbersThatArePossible[map[row][offset].getNumber() - 1] = false;
            }
        }
        //comparing available house numbers with numbers in its area
        int houseAreaNumber = map[row][col].getAreaNumber();
        for (int i = 0; i < 9; i++) {
            House h = mapDvidedByArea[houseAreaNumber - 1][i];
            if (h.getNumber() != 0) {
                numbersThatArePossible[h.getNumber()-1] = false;
            }
        }
        return numbersThatArePossible;
    }

    private static Color giveColorForArea(int i){
        if(i==1){
            return Color.CYAN;
        }
        else if(i==2){
            return Color.lightGray;
        }
        else if(i==3){
            return Color.orange;
        }
        else if(i==4){
            return Color.pink;
        }
        else if(i==5){
            return Color.green;
        }
        else if(i==6){
            return Color.yellow;
        }
        else if(i==7){
            return new Color(200, 94, 200);
        }
        else if(i==8){
            return Color.red;
        }
        else if(i==9){
            return new Color(196, 200, 138);
        }
        return Color.white;

    }
    public static void readKey(){
        try {
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static House[][] createMapCopy(House[][] map){
        House[][] result = new House[9][9];
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                result[i][j] = new House(i,j,map[i][j].getNumber(),map[i][j].getAreaNumber());
            }
        }
        return result;
    }
    public static  CspSudukuVariable[][] creatVariableMapCopy(CspSudukuVariable[][] allVariables){
        throw new UnsupportedOperationException();
    }

    public static void printDebugMap2(CspSudukuVariable[][] allVariables, House[][] map) {
        if(map == null){
            System.out.println("null map");
            return;
        }
        mapDvidedByArea = divideMapByArea(map);
        if(frame != null){
            frame.dispose();
        }
        initJframe();
        for(int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if(allVariables[i][j] == null){
                    JPanel p =new JPanel();
                    p.setLayout(new GridLayout(1,1));
                    JLabel l = new JLabel(map[i][j].getNumber()+"");
                    p.setBorder(BorderFactory.createLineBorder(Color.black,1));
                    l.setBorder(BorderFactory.createBevelBorder(0));
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    l.setBackground(giveColorForArea(map[i][j].getAreaNumber()));
                    l.setOpaque(true);
                    p.add(l);
                    frame.add(p);

                    if(true){}
                }
                else {
                    JPanel panel = new JPanel();
                    panel.setBorder(BorderFactory.createLineBorder(Color.black,1));
                    panel.setLayout(new GridLayout(3,3));
                    for (int k=1;k<=9;k++){
                        if(true){}
                        JLabel l = new JLabel();
                        l.setBorder(BorderFactory.createBevelBorder(1));
                        l.setHorizontalAlignment(SwingConstants.CENTER);
                        panel.add(l);
                        l.setOpaque(true);
                        l.setBackground(giveColorForArea(map[i][j].getAreaNumber()));
                        if(listContainsInteger(k,allVariables[i][j].getPossibleValues())){
                            l.setText(""+k);
                        }
                    }

                    frame.add(panel);
                }
            }
        }

        frame.setVisible(true);
    }

    public static boolean listContainsInteger(int curVal, ArrayList<Integer> possibleValues) {
        for (int i = 0; i < possibleValues.size(); i++) {
            if (possibleValues.get(i) == curVal) {
                return true;
            }
        }
        return false;
    }

}
