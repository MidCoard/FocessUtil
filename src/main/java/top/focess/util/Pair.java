package top.focess.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is an Easy Util Class to store two Instances.
 *
 * @param <K> the type of first element of the Pair
 * @param <V> the type of second element of the Pair
 */
public class Pair<K, V> implements Serializable {

    /**
     * The first element
     */
    private final K key;

    /**
     * The second element
     */
    private final V value;

    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Constructs a new Pair with static method
     *
     * @param key   the first element
     * @param value the second element
     * @param <K>   the first element type
     * @param <V>   the second element type
     * @return the pair
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <K, V> Pair<K, V> of(final K key, final V value) {
        return new Pair<>(key, value);
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public K getLeft() {
        return this.key;
    }

    public V getRight() {
        return this.value;
    }

    public K getFirst() {
        return this.key;
    }

    public V getSecond() {
        return this.value;
    }

    @Override
    public String toString() {
        return "(" + this.key + ',' + this.value + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        if (!Objects.equals(key, pair.key)) return false;
        return Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
