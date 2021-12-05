
import java.util.ArrayList;

public class CspSudukuSolver {
    private House[][] mapDvidedByArea;
    private boolean forwardCheckingFlag = false;
    private boolean arcConsistancyFlag = false;
    private boolean mrvFlag = false;
    private boolean degreeFlag = false;
    private boolean lcvFlag = false;
    private int counter = 0;

    class Temp {
        int value;
        int numberOfConstrainedHouses;

        public Temp(int value, int numberOfConstrainedHouses) {
            this.value = value;
            this.numberOfConstrainedHouses = numberOfConstrainedHouses;
        }
    }

    public CspSudukuSolver(boolean forwardCheckingFlag, boolean arcConsistancyFlag, boolean mrvFlag, boolean degreeFlag, boolean lcvFlag) {
        this.forwardCheckingFlag = forwardCheckingFlag;
        this.arcConsistancyFlag = arcConsistancyFlag;
        this.mrvFlag = mrvFlag;
        this.degreeFlag = degreeFlag;
        this.lcvFlag = lcvFlag;
    }

    private CspSudukuVariable[][] initAllVariables(House[][] map) {
        CspSudukuVariable[][] result = new CspSudukuVariable[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j].getNumber() == 0) {
                    CspSudukuVariable v = new CspSudukuVariable(i, j, map[i][j].getAreaNumber());
                    fillVariablePossiblitiesAndDegree(v, map, mapDvidedByArea);
                    result[i][j] = v;
                }
            }
        }
        return result;
    }

    //ok tested
    private static House[][] divideMapByArea(House[][] map) {
        House[][] result = new House[9][9];
        int[] areaCounter = new int[9];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                House h = map[i][j];
                int areaNumber = h.getAreaNumber() - 1;
                result[areaNumber][areaCounter[areaNumber]] = h;
                areaCounter[areaNumber]++;
            }
        }
        return result;
    }

    public House[][] solveMap(House[][] map) {
        counter = 0;
        mapDvidedByArea = divideMapByArea(map);
        if (forwardCheckingFlag) {
            System.out.print("fc ");
        }
        if (arcConsistancyFlag) {
            System.out.print("arc ");
        }
        if (mrvFlag) {
            System.out.print("mrv ");
        }
        if (degreeFlag) {
            System.out.print("degree ");
        }
        if (lcvFlag) {
            System.out.print("lcv ");
        }
        System.out.println();
        if (UtilityFunctions.goalCheck(map, mapDvidedByArea)) {
            return map;
        }
        House[][] result = solve(map, 1);
        System.out.println(counter);
        return result;
    }

    private CspSudukuVariable selectVariable(House[][] map, House[][] mapDvidedByArea) {
        CspSudukuVariable result = null;

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map.length; col++) {
                if (map[row][col].getNumber() == 0) {

                    CspSudukuVariable v = new CspSudukuVariable(row, col, map[row][col].getAreaNumber());
                    fillVariablePossiblitiesAndDegree(v, map, mapDvidedByArea);
                    if (!mrvFlag && !degreeFlag) {
                        return v;
                    }
                    if (result == null) {
                        result = v;
                    } else {
                        if (mrvFlag) {
                            if (result.getPossibleValues().size() >= v.getPossibleValues().size()) {
                                if (result.getPossibleValues().size() == v.getPossibleValues().size()) {
                                    if (degreeFlag) {
                                        if (result.getNumberOfConstrainedVariables() < v.getNumberOfConstrainedVariables()) {
                                            result = v;
                                        }
                                    }
                                } else {
                                    result = v;
                                }
                            }
                        } else {
                            if (result.getNumberOfConstrainedVariables() < v.getNumberOfConstrainedVariables()) {
                                result = v;
                            }
                        }
                    }


                }

            }
        }
        return result;
    }

    private CspSudukuVariable selectVariable(CspSudukuVariable[][] allVariables) {
        CspSudukuVariable result = null;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (allVariables[i][j] != null) {
                    if (!degreeFlag && !mrvFlag) {
                        return allVariables[i][j];
                    }
                    if (result == null) {
                        result = allVariables[i][j];
                    } else {
                        if (mrvFlag) {
                            if (result.getPossibleValues().size() >= allVariables[i][j].getPossibleValues().size()) {
                                if (result.getPossibleValues().size() == allVariables[i][j].getPossibleValues().size()) {
                                    if (degreeFlag && result.getNumberOfConstrainedVariables() < allVariables[i][j].getNumberOfConstrainedVariables()) {
                                        result = allVariables[i][j];
                                    }
                                } else {
                                    result = allVariables[i][j];
                                }
                            }
                        } else {
                            if (result.getNumberOfConstrainedVariables() < allVariables[i][j].getNumberOfConstrainedVariables()) {
                                result = allVariables[i][j];
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void fillVariablePossiblitiesAndDegree(CspSudukuVariable v, House[][] map, House[][] devidedMap) {
        boolean[] notAvailableNumbers = new boolean[9];
        int numberOfConstrainedVariables = 0;
        for (int i = 0; i < 9; i++) {

            //checking row
            if (map[v.getRow()][i].getNumber() != 0) {
                notAvailableNumbers[map[v.getRow()][i].getNumber() - 1] = true;
            } else {
                if (i != v.getColumn()) {
                    numberOfConstrainedVariables++;
                }
            }


            //checking col
            if (map[i][v.getColumn()].getNumber() != 0) {
                notAvailableNumbers[map[i][v.getColumn()].getNumber() - 1] = true;
            } else {
                if (i != v.getRow()) {
                    numberOfConstrainedVariables++;
                }
            }

        }

        // checking area
        for (int i = 0; i < 9; i++) {
            int row  = devidedMap[v.getArea() - 1][i].getRow();
            int col  = devidedMap[v.getArea() - 1][i].getCol();
            House h = map[row][col];
            if (h.getNumber() != 0) {
                notAvailableNumbers[h.getNumber() - 1] = true;
            } else {
                if (h.getRow() != v.getRow() && h.getCol() != v.getColumn()) {
                    numberOfConstrainedVariables++;
                }
            }
        }

        // adding remaining values to variable
        for (int i = 0; i < 9; i++) {
            if (!notAvailableNumbers[i]) {
                v.getPossibleValues().add(i + 1);
            }
        }

        v.setNumberOfConstrainedValues(numberOfConstrainedVariables);

    }

    private boolean forwardCheckIsOk(House[][] map) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j].getNumber() == 0) {
                    CspSudukuVariable v = new CspSudukuVariable(i, j, map[i][j].getAreaNumber());
                    fillVariablePossiblitiesAndDegree(v, map, mapDvidedByArea);
                    if (v.getPossibleValues().size() == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private House[][] solve(House[][] map, int depth) {
        counter++;

        CspSudukuVariable selectedHouse;
        if (!arcConsistancyFlag) {
            selectedHouse = selectVariable(map, mapDvidedByArea);
        } else {

            CspSudukuVariable[][] allVariables = initAllVariables(map);
            ArcConsistyChecker ac = new ArcConsistyChecker();
            ac.AC3(map, mapDvidedByArea, allVariables);
            selectedHouse = selectVariable(allVariables);

        }

        if (selectedHouse == null) {
            return null;
        }
        System.out.println("selected house row: " + selectedHouse.getRow() + " selected col:" + selectedHouse.getColumn() +
                " and contrained vars are :" + selectedHouse.getNumberOfConstrainedVariables() + "in depth " + depth);

        ArrayList<Integer> possibleValuesList = selectedHouse.getPossibleValues();
        if (lcvFlag) {
            possibleValuesList = sortPossibleValuesList(possibleValuesList, map, selectedHouse.getRow(), selectedHouse.getColumn());
        }
        for (int i = 0; i < possibleValuesList.size(); i++) {
//            UtilityFunctions.printDebugMap(map);
//            UtilityFunctions.readKey();
            House[][] newMap = UtilityFunctions.createMapCopy(map);
            newMap[selectedHouse.getRow()][selectedHouse.getColumn()].setNumber(selectedHouse.getPossibleValues().get(i));
            //goal check - return if it is goal
            if (UtilityFunctions.goalCheck(newMap, mapDvidedByArea)) {
                return newMap;
            }
            if (forwardCheckingFlag) {
                if (!forwardCheckIsOk(newMap)) {
                    continue;
                }
            }
            House[][] result = solve(newMap, depth + 1);

            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private ArrayList<Integer> sortPossibleValuesList(ArrayList<Integer> possibleValuesList, House[][] map, int row, int col) {
        ArrayList<Temp> temps = new ArrayList<Temp>();
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < possibleValuesList.size(); i++) {
            int curVal = possibleValuesList.get(i);
            int n = giveNumberOfConstrainedHousesByValue(curVal, map, row, col);
            temps.add(new Temp(curVal, n));
        }
        sortTempArrayList(temps);
        for (int i = 0; i < temps.size(); i++) {
            result.add(temps.get(i).value);
        }

        return result;
    }

    private void sortTempArrayList(ArrayList<Temp> temps) {
        for (int i = 0; i < temps.size(); i++) {
            for (int j = i + 1; j < temps.size(); j++) {
                if (temps.get(i).numberOfConstrainedHouses > temps.get(j).numberOfConstrainedHouses) {

                    int n1 = temps.get(i).numberOfConstrainedHouses;
                    int value1 = temps.get(i).value;
                    int n2 = temps.get(j).numberOfConstrainedHouses;
                    int value2 = temps.get(j).value;

                    temps.get(i).numberOfConstrainedHouses = n2;
                    temps.get(i).value = value2;
                    temps.get(j).numberOfConstrainedHouses = n1;
                    temps.get(j).value = value1;
                }
            }
        }
    }

    private int giveNumberOfConstrainedHousesByValue(int curVal, House[][] map, int row, int col) {
        int result = 0;
        for (int i = 0; i < 9; i++) {
            if (i != col && map[row][i].getNumber() == 0) {
                CspSudukuVariable v = new CspSudukuVariable(row, i, map[row][i].getAreaNumber());
                fillVariablePossiblitiesAndDegree(v, map, mapDvidedByArea);
                if (UtilityFunctions.listContainsInteger(curVal, v.getPossibleValues())) {
                    result++;
                }
            }

            if (i != row && map[i][col].getNumber() == 0) {
                if (true) {
                }
                CspSudukuVariable v = new CspSudukuVariable(i, col, map[i][col].getAreaNumber());
                fillVariablePossiblitiesAndDegree(v, map, mapDvidedByArea);
                if (UtilityFunctions.listContainsInteger(curVal, v.getPossibleValues())) {
                    result++;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            int areaNumber = map[row][col].getAreaNumber();
            House h = mapDvidedByArea[areaNumber - 1][i];
            if (h.getCol() != col && h.getRow() != row) {
                CspSudukuVariable v = new CspSudukuVariable(h.getRow(), h.getCol(), map[h.getRow()][h.getRow()].getAreaNumber());
                fillVariablePossiblitiesAndDegree(v, map, mapDvidedByArea);
                if (UtilityFunctions.listContainsInteger(curVal, v.getPossibleValues())) {
                    result++;
                }
            }
        }
        return result;
    }


}
