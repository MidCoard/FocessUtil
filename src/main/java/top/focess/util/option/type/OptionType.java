package top.focess.util.option.type;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to define the type of argument
 *
 * @param <T> the target type
 */
public abstract class OptionType<T> {

    /**
     * Accept all values
     */
    public static final OptionType<String> DEFAULT_OPTION_TYPE = new OptionType<String>() {
        @Override
        public String parse(final String v) {
            return v;
        }

        @Override
        public boolean accept(final String v) {
            return true;
        }

        @NotNull
        @Contract(pure = true)
        @Override
        public String toString() {
            return "DEFAULT_OPTION_TYPE";
        }
    };

    /**
     * Parse the value to the target type.
     *
     * Note: called only when {@link #accept(String)} return true.
     *
     * @param v the value
     * @return the target type
     */
    public abstract T parse(String v);

    /**
     * Indicate whether this type can accept the value.
     * @param v the value
     * @return true if can accept, false otherwise
     */
    public abstract boolean accept(String v);
}
