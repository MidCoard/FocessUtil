package top.focess.util.option.type;

/**
 * Accept long type argument
 */
public class LongOptionType extends ExceptionOptionType<Long> {

    public static final LongOptionType LONG_OPTION_TYPE = new LongOptionType();

    @Override
    public Long parse(final String v) {
        return Long.parseLong(v);
    }

    @Override
    public String toString() {
        return "LONG_OPTION_TYPE";
    }
}
