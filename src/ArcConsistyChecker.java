import java.util.LinkedList;
import java.util.Queue;


public class ArcConsistyChecker {
    private class CspSudukuVariablePair{
        private CspSudukuVariable cspSudukuVariable1;
        private CspSudukuVariable cspSudukuVariable2;

        public CspSudukuVariablePair(CspSudukuVariable cspSudukuVariable1, CspSudukuVariable cspSudukuVariable2) {
            this.cspSudukuVariable1 = cspSudukuVariable1;
            this.cspSudukuVariable2 = cspSudukuVariable2;
        }

        public CspSudukuVariable getCspSudukuVariable1() {
            return cspSudukuVariable1;
        }

        public CspSudukuVariable getCspSudukuVariable2() {
            return cspSudukuVariable2;
        }
    }

    private void initQueue(Queue<CspSudukuVariablePair> queue,House[][] devidedMap,CspSudukuVariable[][] allVariables){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(allVariables[i][j] != null){

                    for(int offset =0;offset<9;offset++){
                        if(offset != j && allVariables[i][offset]!= null){
                            CspSudukuVariablePair pair = new CspSudukuVariablePair(allVariables[i][j],allVariables[i][offset]);
                            queue.add(pair);
                        }
                    }

                    for(int offset =0;offset<9;offset++){
                        if(offset != i && allVariables[offset][j]!= null){
                            CspSudukuVariablePair pair = new CspSudukuVariablePair(allVariables[i][j],allVariables[offset][j]);
                            queue.add(pair);
                        }
                    }
                    for(int offset =0;offset<9;offset++){
                        int areaNumber = allVariables[i][j].getArea();
                        House h = devidedMap[areaNumber - 1][offset];
                        int row = h.getRow();
                        int col = h.getCol();
                        if(i != row && col != j && allVariables[row][col]!=null){
                            CspSudukuVariablePair pair = new CspSudukuVariablePair(allVariables[i][j],allVariables[row][col]);
                            queue.add(pair);
                        }

                    }
                }
            }
        }
    }

    public  void AC3(House[][] map,House[][] devidedMap,CspSudukuVariable[][] allVariables)  {
        Queue<CspSudukuVariablePair> queue = new LinkedList();
        // fill queue with pair of CspSudukuVariables from given CspSudukuVariable[][]
        initQueue(queue,devidedMap,allVariables);
        while (!queue.isEmpty()){
            CspSudukuVariablePair variablePair = queue.poll();
            CspSudukuVariable v1 = variablePair.getCspSudukuVariable1();
            CspSudukuVariable v2 = variablePair.getCspSudukuVariable2() ;
            if(RMInconsistantValues( v1 , v2, map ,devidedMap)){
                // adding row neighbours pair to queue
                for(int i =0;i<9;i++){
                    if(i != v1.getColumn() && allVariables[v1.getRow()][i] != null){
                        CspSudukuVariablePair pair = new CspSudukuVariablePair( allVariables[v1.getRow()][i] , v1 );
                        queue.add(pair);
                    }
                }
                // adding col neighbours pair to queue
                for(int i =0;i<9;i++){
                    if(i != v1.getRow() && allVariables[i][v1.getColumn()] != null){
                        CspSudukuVariablePair pair = new CspSudukuVariablePair( allVariables[i][v1.getColumn()] , v1 );
                        queue.add(pair);
                    }
                }
                // adding area neighbours pair to queue
                for(int i =0;i<9;i++){
                    int areaNumber = v1.getArea();
                    int row = devidedMap[areaNumber-1][i].getRow();
                    int col = devidedMap[areaNumber-1][i].getCol();
                    if(row != v1.getRow() && col!= v1.getColumn()){
                        if(allVariables[row][col] != null){
                            CspSudukuVariablePair pair = new CspSudukuVariablePair(allVariables[row][col],v1);
                            queue.add(pair);
                        }
                    }
                }
            }
        }
    }

    public  boolean RMInconsistantValues(CspSudukuVariable h1,CspSudukuVariable h2,House[][] map,House[][] devidedMap)  {
        boolean removed = false;

        for(int i=0; i <h1.getPossibleValues().size();i++){
            House[][] newMap = UtilityFunctions.createMapCopy(map);
            newMap[h1.getRow()][h1.getColumn()].setNumber( h1.getPossibleValues().get(i) );

            CspSudukuVariable newV = new CspSudukuVariable(h2.getRow(),h2.getColumn(),h2.getArea());
            if(!CspSudukuVariableDomainNotEmpty(newMap,devidedMap,newV)){
                h1.getPossibleValues().remove(i);
                i--;
                removed = true;
            }
        }
        return removed;
    }

    private  boolean CspSudukuVariableDomainNotEmpty(House[][] map,House[][] devidedMap,CspSudukuVariable v) {

        CspSudukuSolver.fillVariablePossiblitiesAndDegree(v,map,devidedMap);
        if(v.getPossibleValues().size() == 0){
            return false;
        }
        return true;
    }


}
