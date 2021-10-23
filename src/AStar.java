import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class AStar {
    private LinkedList<Three<State, LinkedList<State>, Double>> pending;
    private HashMap<State, Three<State, LinkedList<State>, Double>> treated;
    private BiFunction<Pair<State, LinkedList<State>>, State, Double> estimatedCostFoo;

    public AStar(BiFunction<Pair<State, LinkedList<State>>, State, Double> estimatedCostFoo) {
        this.pending = new LinkedList<>();
        this.treated = new HashMap<>();
        this.estimatedCostFoo = estimatedCostFoo;
    }

    //Calcula el que costa anar de l'estat act al seguent, te en conte tipus de carretera i canvis
    private int realCost(State act, State next) {
        return act.getCost() == next.getCost() ? next.getCost() : next.getCost() + 5;
    }

    private void addNode(Three<State, LinkedList<State>, Double> n) {
        this.pending.add(n);
        this.pending.sort(Comparator.comparing(Three::cost));
    }

    private double listCost(List<State> path) {
        double cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            State c = path.get(i);
            State c2 = path.get(i + 1);
            cost += c.getCost() == c2.getCost() ? c2.getCost() : c2.getCost() + 5;
        }
        return cost;
    }

    public Three<State, LinkedList<State>, Double> search(State ini, State end) {
        int nodesTreated = 0;
        Three<State, LinkedList<State>, Double> initial = new Three<>(ini, new LinkedList<>(), this.estimatedCostFoo.apply(new Pair<>(ini, new LinkedList<>()), end));
        pending.add(initial);
        while (!pending.isEmpty()) {
            nodesTreated++;
            //System.out.println("pending = " + pending);
            Three<State, LinkedList<State>, Double> current = pending.remove(0);
            if(current.key().equals(end)) {
                LinkedList<State> l = current.value();
                l.add(current.key());
                System.out.println("\t\tnodesTreated = " + nodesTreated);
                return new Three<>(current.key(), l, current.cost());
            }

            for(State succ : current.key().getSuccesors()) {
                LinkedList<State> pathToSucc = (LinkedList) current.value().clone();
                pathToSucc.add(current.key());
                double costSucc = listCost(pathToSucc) + this.estimatedCostFoo.apply(new Pair<>(succ, pathToSucc), end);
                //Cost per anar de current a succ + cost estimat desde succ fins al final
                if(!this.treated.containsKey(succ) || listCost(this.treated.get(succ).value()) > costSucc) {
                    Three<State, LinkedList<State>, Double> succThree = new Three<>(succ, pathToSucc, costSucc);
                    addNode(succThree);
                }
            }
            this.treated.put(current.key(), current);
        }
        System.out.println("\t\t no path found nodesTreated = " + nodesTreated);
        return null;
    }
}
