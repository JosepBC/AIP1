import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class TestMain {
    private static double listCost(List<State> path) {
        double cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            State c = path.get(i);
            State c2 = path.get(i + 1);
            cost += c.getCost() == c2.getCost() ? c2.getCost() : c2.getCost() + 5;
        }
        return cost;
    }

    public static void main(String[] args) throws FileNotFoundException {
        MapLoader map = new MapLoader("myMap.txt");
        State ini = map.getStateRowCol(0, 0);
        State end = map.getStateRowCol(6, 0);
        System.out.println("Initial node = " + ini);
        System.out.println("End node = " + end);

        //Heuristic 1 the cost until the end is proportional with the cost until current
        BiFunction<Pair<State, LinkedList<State>>, State, Double> heuristic1 = (current, endState) -> {
            int currentCost = 0;
            for (int i = 0; i < current.value().size() - 1; i++) {
                State c = current.value().get(i);
                State c2 = current.value().get(i + 1);
                currentCost += c.getCost() == c2.getCost() ? c2.getCost() : c2.getCost() + 5;
            }

            double avgCostCurrent = current.value().size() == 0 ? 0 : currentCost / current.value().size();
            double manhattanDistToEnd = Math.abs((endState.getCol() - current.key().getCol()) + (endState.getRow() - current.key().getRow()));
            return manhattanDistToEnd * avgCostCurrent;
        };

        //Heuristic 2, priorize endNode road type, diff of time between end and current
        BiFunction<Pair<State, LinkedList<State>>, State, Double> heuristic2 =
                (current, endNode) -> (double)Math.abs(endNode.getCost() - current.key().getCost());

        //Heuristic 3, calc number of jumps needed to be done to get to the end, multiply by 1, all motor way
        BiFunction<Pair<State, LinkedList<State>>, State, Double> heuristic3 =
                (current, endNode) -> (double)Math.abs((endNode.getCol() - current.key().getCol()) + (endNode.getRow() - current.key().getRow()));

        System.out.println("Best first:");
        System.out.println("\tHeuristic 1, cost proportional:");
        BestFirst<Double> bestFirst = new BestFirst<>(heuristic1);
        Three<State, LinkedList<State>, Double> bestFirstResult = bestFirst.search(ini, end);
        System.out.println("\t\tcost = " + listCost(bestFirstResult.value()));
        System.out.println("\t\tPath found: "+bestFirstResult.value());
        System.out.println("\t\tPath len: "+bestFirstResult.value().size());

        System.out.println("\tHeuristic 2, priorize end road type:");
        BestFirst<Double> bestFirst2 = new BestFirst<>(heuristic2);
        Three<State, LinkedList<State>, Double> bestFirst2Result = bestFirst2.search(ini, end);
        System.out.println("\t\tcost = " + listCost(bestFirst2Result.value()));
        System.out.println("\t\tPath found: "+bestFirst2Result.value());
        System.out.println("\t\tPath len: "+bestFirst2Result.value().size());

        System.out.println("\tHeuristic 3, manhattan distance");
        BestFirst<Double> bestFirst3 = new BestFirst<>(heuristic3);
        Three<State, LinkedList<State>, Double> bestFirst3Result = bestFirst3.search(ini, end);
        System.out.println("\t\tcost = " + listCost(bestFirst3Result.value()));
        System.out.println("\t\tPath found: "+bestFirst3Result.value());
        System.out.println("\t\tPath len: "+bestFirst3Result.value().size());

        System.out.println("A star:");
        System.out.println("\tHeuristic 1, cost proportional:");
        AStar aStar = new AStar(heuristic1);
        Three<State, LinkedList<State>, Double> aStar1Res = aStar.search(ini, end);
        System.out.println("\t\tcost = " + listCost(aStar1Res.value()));
        System.out.println("\t\tPath found: " + aStar1Res.value());
        System.out.println("\t\tPath len: "+aStar1Res.value().size());

        System.out.println("\tHeuristic 2, priorize end road type:");
        AStar aStar2 = new AStar(heuristic2);
        Three<State, LinkedList<State>, Double> aStar2Res = aStar2.search(ini, end);
        System.out.println("\t\tcost = " + listCost(aStar2Res.value()));
        System.out.println("\t\tPath found: " + aStar2Res.value());
        System.out.println("\t\tPath len: "+aStar2Res.value().size());

        System.out.println("\tHeuristic 3, manhattan distance:");
        AStar aStar3 = new AStar(heuristic3);
        Three<State, LinkedList<State>, Double> aStar3Res = aStar3.search(ini, end);
        System.out.println("\t\tcost = " + listCost(aStar3Res.value()));
        System.out.println("\t\tPath found: " + aStar3Res.value());
        System.out.println("\t\tPath len: "+aStar3Res.value().size());
    }
}
