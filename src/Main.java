import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args){
        String numbersFilePath = "numbers.txt";
        String areaFilePath = "area.txt";
//        map is saved in a 2d array of houses in which row is first parameter and column is the second
//        like this : map[row][column]
//        house is made up of row and col number,its area number and a number that shows it value in suduku map
        House[][] originalMap = null;
        try {
            originalMap = UtilityFunctions.readMap(numbersFilePath,areaFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("original map");

        System.out.println("press any key to continue");
        UtilityFunctions.printDebugMap(originalMap);
        UtilityFunctions.readKey();

        House[][] newMap = UtilityFunctions.createMapCopy(originalMap);
        CspSudukuSolver solver = new CspSudukuSolver(true,true,true,true,true);
        UtilityFunctions.printDebugMap(solver.solveMap(newMap));
    }
}
