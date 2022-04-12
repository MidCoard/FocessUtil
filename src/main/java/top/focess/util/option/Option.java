package top.focess.util.option;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import top.focess.util.option.type.OptionType;

import java.util.Map;
import java.util.Queue;

/**
 * An option is a way to present arguments. It is convenient to use option to get arguments that we want.
 */
public class Option {

    private final OptionParserClassifier classifier;

    private final Map<OptionType<?>, Queue<String>> optionTypes = Maps.newHashMap();

    protected Option(final OptionParserClassifier classifier) {
        this.classifier = classifier;
    }

    /**
     * Get the name of the option
     * @return the name of the option
     */
    public String getName() {
        return this.classifier.getName();
    }

    protected void put(final OptionType<?> optionType, final String value) {
        this.optionTypes.compute(optionType, (k, v) -> {
            if (v == null)
                v = Queues.newConcurrentLinkedQueue();
            v.offer(value);
            return v;
        });
    }

    /**
     * Get argument by the option type
     * @param optionType the option type
     * @param <T> the target type
     * @return the argument in the target type
     */
    public <T> T get(final OptionType<T> optionType) {
        final Queue<String> options = this.optionTypes.getOrDefault(optionType, Queues.newConcurrentLinkedQueue());
        final String v = options.poll();
        final T t = optionType.parse(v == null ? "" : v);
        this.optionTypes.put(optionType, options);
        return t;
    }

    @Override
    public String toString() {
        return "Option{" +
                "optionTypes=" + this.optionTypes +
                '}';
    }
}
