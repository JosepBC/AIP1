import java.util.LinkedList;
import java.util.Objects;

public class State {
    private int cost;
    private final int row;
    private final int col;
    private LinkedList<State> succesors;

    public State(int cost, int row, int col) {
        this.cost = cost;
        this.row = row;
        this.col = col;
        this.succesors = new LinkedList<>();
    }

    public State(State st) {
        this.cost = st.getCost();
        this.row = st.getRow();
        this.col = st.getCol();
        this.succesors = new LinkedList<>(st.getSuccesors());
    }

    public int getCost() {
        return this.cost;
    }

    public LinkedList<State> getSuccesors() {
        return succesors;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void appendSuccesor(State succ) {
        this.succesors.add(succ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return cost == state.cost && row == state.row && col == state.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, row, col);
    }

    public State clone() {
        return new State(this);
    }

    @Override
    public String toString() {
        return "State{" +
                "cost=" + cost +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
