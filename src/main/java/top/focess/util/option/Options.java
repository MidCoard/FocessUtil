package top.focess.util.option;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Used to parse the arguments into options
 */
public class Options {

    private final Map<String, Option> options = Maps.newHashMap();

    /**
     * Use {@link #parse(String[], OptionParserClassifier...)} to parse the arguments into options
     */
    private Options(){}

    /**
     * Used to parse the arguments into options
     * @param args the arguments
     * @param classifiers the classifiers which define how to parse the arguments into option
     * @return the options
     */
    @NotNull
    public static Options parse(@NotNull final String[] args, final OptionParserClassifier... classifiers) {
        final List<String> temp = Lists.newArrayList();
        final List<OptionParserClassifier> defaultClassifier = Lists.newArrayList();
        final Options options = new Options();
        for (final String arg : args) {
            if (arg.startsWith("--")) {
                for (final OptionParserClassifier classifier : defaultClassifier)
                    options.add(classifier.parse(temp.toArray(new String[0])));
                defaultClassifier.clear();
                temp.clear();
                for (final OptionParserClassifier classifier : classifiers)
                    if (arg.equals("--" + classifier.getName()))
                        defaultClassifier.add(classifier);
            } else
                temp.add(arg);
        }
        for (final OptionParserClassifier classifier : defaultClassifier)
            options.add(classifier.parse(temp.toArray(new String[0])));
        return options;
    }

    private void add(@Nullable final Option option) {
        if (option == null)
            return;
        this.options.put(option.getName(), option);
    }

    /**
     * Get option named key
     * @param key the key of option
     * @return the option named key
     */
    @Nullable
    public Option get(final String key) {
        return this.options.get(key);
    }

    @Override
    public String toString() {
        return "Options{" +
                "options=" + this.options +
                '}';
    }
}
