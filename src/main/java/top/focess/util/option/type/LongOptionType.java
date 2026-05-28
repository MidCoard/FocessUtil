package top.focess.util.option.type;

/**
 * Accept long type argument
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class LongOptionType extends ExceptionOptionType<Long> {


    /**
     * Single Instance for LongOptionType
     */
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
