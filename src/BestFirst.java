import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 *
 * @param <T> Return type of the heuristic function, must implement Comparable
 */
public class BestFirst<T extends Comparable<T>> {
    private LinkedList<Three<State, LinkedList<State>, T>> pending;
    private HashMap<State, Three<State, LinkedList<State>, T>> treated;
    private BiFunction<Pair<State, LinkedList<State>>, State, T> heuristic;

    public BestFirst(BiFunction<Pair<State, LinkedList<State>>, State, T> heuristicFoo) {
        this.pending = new LinkedList<>();
        this.treated = new HashMap<>();
        this.heuristic = heuristicFoo;
    }


    private void addNode(Three<State, LinkedList<State>, T> n) {
        this.pending.add(n);
        this.pending.sort(Comparator.comparing(Three::cost));
    }

    public Three<State, LinkedList<State>, T> search(State ini, State end) {
        int nodesTreated = 0;
        Three<State, LinkedList<State>, T> initial = new Three<>(ini, new LinkedList<>(), this.heuristic.apply(new Pair<>(ini, new LinkedList<>()), end));
        pending.add(initial);
        while (!pending.isEmpty()) {
            nodesTreated++;
            //System.out.println("pending = " + pending);
            Three<State, LinkedList<State>, T> current = pending.remove(0);
            if(current.key().equals(end)) {
                LinkedList<State> l = current.value();
                l.add(current.key());
                System.out.println("\t\tnodesTreated = " + nodesTreated);
                return new Three<>(current.key(), l, current.cost());
            }
            for(State succ : current.key().getSuccesors()) {
                if(!this.treated.containsKey(succ) && !this.pending.contains(succ)) {
                    LinkedList<State> pathToSucc = (LinkedList) current.value().clone();
                    pathToSucc.add(current.key());
                    T costSucc = this.heuristic.apply(new Pair<>(succ, pathToSucc), end);
                    //Cost per anar de current a succ + cost estimat desde succ fins al final
                    Three<State, LinkedList<State>, T> succThree = new Three<>(succ, pathToSucc, costSucc);
                    addNode(succThree);
                }

            }
            this.treated.put(current.key(), current);
        }
        System.out.println("\t\t no path found nodesTreated = " + nodesTreated);
        return null;
    }
}
