import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiFunction;

public class AStar {
    private LinkedList<Three<State, LinkedList<State>, Integer>> pending;
    private HashMap<State, Three<State, LinkedList<State>, Integer>> treated;
    private BiFunction<State, State, Integer> estimatedCostFoo;

    public AStar(BiFunction<State, State, Integer> estimatedCostFoo) {
        this.pending = new LinkedList<>();
        this.treated = new HashMap<>();
        this.estimatedCostFoo = estimatedCostFoo;
    }

    //Calcula el que costa anar de l'estat act al seguent, te en conte tipus de carretera i canvis
    private int realCost(State act, State next) {
        int extra = 5;
        if (act.getCost() == next.getCost()) extra = 0;
        return next.getCost() + extra;
    }

    private void addNode(Three<State, LinkedList<State>, Integer> n) {
        int idx = this.pending.indexOf(n);
        Three<State, LinkedList<State>, Integer> treatedElem = this.treated.get(n);

        //Si el node esta a pendents i el seu cost es inferior al de n skip
        //Si el node esta a tractacts i el seu cost es inferior al de n skip
        if((idx != -1 && this.pending.get(idx).cost() < n.cost()) || (treatedElem != null && treatedElem.cost() < n.cost())) return;


        if(idx != -1) this.pending.remove(idx);
        this.pending.add(n);
        this.pending.sort(Comparator.comparing(Three::cost));

    }

    public Three<State, LinkedList<State>, Integer> search(State ini, State end) {
        int nodesTreated = 0;
        Three<State, LinkedList<State>, Integer> initial = new Three<>(ini, new LinkedList<>(), this.estimatedCostFoo.apply(ini, end));
        pending.add(initial);
        while (!pending.isEmpty()) {
            nodesTreated++;
            //System.out.println("pending = " + pending);
            Three<State, LinkedList<State>, Integer> current = pending.remove(0);

            for(State succ : current.key().getSuccesors()) {
                LinkedList<State> pathToSucc = (LinkedList) current.value().clone();
                pathToSucc.add(current.key());
                int costSucc = current.cost() + realCost(current.key(), succ) + this.estimatedCostFoo.apply(succ, end);
                if(succ.equals(end)) {
                    System.out.println("nodesTreated = " + nodesTreated);
                    pathToSucc.add(succ);
                    return new Three<>(succ, pathToSucc, costSucc);
                }
                //Cost per anar de current a succ + cost estimat desde succ fins al final
                Three<State, LinkedList<State>, Integer> succThree = new Three<>(succ, pathToSucc, costSucc);
                addNode(succThree);
            }
            this.treated.put(current.key(), current);
        }
        System.out.println("nodesTreated = " + nodesTreated);
        return null;
    }
}
