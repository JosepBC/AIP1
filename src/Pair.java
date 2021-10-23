import java.util.Objects;

public record Pair<K, V>(K key, V value) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
