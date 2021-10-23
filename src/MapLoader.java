import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MapLoader {
    private List<List<State>> map = new LinkedList<>();

    public MapLoader(String pathToMap) throws FileNotFoundException {
        File fileObj = new File(pathToMap);
        Scanner reader = new Scanner(fileObj);
        int lineNum = 0;
        while(reader.hasNextLine()) {
            processRow(reader.nextLine(), lineNum);
            lineNum++;
        }
        processSucc();

        reader.close();
    }

    private void processRow(String row, int lineNum) {
        List<State> rowList = new LinkedList<>();
        int col = 0;
        for(String elem : row.split(",")) {
            State curr = new State(Integer.parseInt(elem), lineNum, col);
            rowList.add(curr);
            col++;
        }

        map.add(rowList);
    }

    private State succUp(int i, int j) {
        //Si ja estem a la fila de dalt o el que hi ha a sobre es 0, no tenim succesor a sobre
        if(i == 0 || map.get(i - 1).get(j).getCost() == 0) return null;
        return map.get(i - 1).get(j);
    }

    private State succDown(int i, int j) {
        //Si la j es la ultima fila, ja estem al final, no tenim succesor a sota, si el de sota es 0 tampoc
        if(map.size() - 1 == i || map.get(i + 1).get(j).getCost() == 0) return null;
        return map.get(i + 1).get(j);
    }

    private State succRight(int i, int j) {
        //Si ja estem a la dreta, o el de la dreta es 0 no tenim succesor a la dreta
        if(map.get(i).size() - 1 == j || map.get(i).get(j + 1).getCost() == 0) return null;
        return map.get(i).get(j + 1);
    }

    private State succLeft(int i, int j) {
        //Si ja estem a l'esquerra o el de l'esquerra es 0 no tenim succesor a l'esquerra
        if(j == 0 || map.get(i).get(j - 1).getCost() == 0) return null;
        return map.get(i).get(j - 1);
    }

    private void processSucc() {
        State succ;
        for(int i = 0; i < map.size(); i++) {
            for(int j = 0; j < map.get(i).size(); j++) {
                if(map.get(i).get(j).getCost() != 0) {
                    succ = succUp(i, j);
                    if(succ != null) map.get(i).get(j).appendSuccesor(succ);
                    succ = succDown(i, j);
                    if(succ != null) map.get(i).get(j).appendSuccesor(succ);
                    succ = succLeft(i, j);
                    if(succ != null) map.get(i).get(j).appendSuccesor(succ);
                    succ = succRight(i, j);
                    if(succ != null) map.get(i).get(j).appendSuccesor(succ);
                }
            }
        }

    }

    public void printMap() {
        for(List<State> row : map) {
            for(State elem: row) {
                System.out.print(elem.getCost()+"  ");
            }
            System.out.println();
        }
    }

    public void printVerboseMap() {
        for(List<State> row : map) {
            for(State elem: row) {
                System.out.print(elem+"  |  ");
            }
            System.out.println();
        }
    }

    public State getStateRowCol(int row, int col) {
        if(row < 0 || col < 0 || row  > this.map.get(0).size() - 1 || col > this.map.size() - 1) return null;
        return this.map.get(row).get(col);
    }
}
