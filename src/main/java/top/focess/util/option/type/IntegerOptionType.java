package top.focess.util.option.type;

/**
 * Accept Integer type option.
 */
public class IntegerOptionType extends ExceptionOptionType<Integer> {

    /**
     * Single Instance for IntegerOptionType
     */
    public static final IntegerOptionType INTEGER_OPTION_TYPE = new IntegerOptionType();

    @Override
    public Integer parse(final String v) {
        return Integer.parseInt(v);
    }

    @Override
    public String toString() {
        return "INTEGER_OPTION_TYPE";
    }
}
