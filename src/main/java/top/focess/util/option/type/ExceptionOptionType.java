package top.focess.util.option.type;


/**
 * Simplify the {@link OptionType} class.
 * Implement the accept method. The accept method returns true if there is no exception in parsing the String argument, false otherwise.
 *
 * @param <T> the target type
 */
public abstract class ExceptionOptionType<T> extends OptionType<T> {

    @Override
    public boolean accept(final String v) {
        try {
            this.parse(v);
            return true;
        } catch (final Exception e) {
            return false;
        }
    }
}
