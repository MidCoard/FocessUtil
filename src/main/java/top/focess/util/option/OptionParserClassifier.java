package top.focess.util.option;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.focess.util.option.type.OptionType;

/**
 * This class defines how to parse the arguments into Option
 */
public class OptionParserClassifier {

    /**
     * The name of the classifier
     */
    private final String name;

    /**
     * The type of the arguments
     */
    private final OptionType<?>[] optionTypes;

    /**
     * Initialize an OptionParserClassifier
     * @param name the name of the classifier
     * @param optionTypes the types of the arguments
     */
    public OptionParserClassifier(final String name, final OptionType<?>... optionTypes) {
        this.name = name;
        this.optionTypes = optionTypes;
    }

    public String getName() {
        return this.name;
    }

    public OptionType<?>[] getOptionTypes() {
        return this.optionTypes;
    }

    /**
     * Parse the arguments into Option
     * @param args the arguments
     * @return the parsed Option
     */
    @Nullable
    public Option parse(@NotNull final String[] args) {
        if (args.length != this.optionTypes.length)
            return null;
        final Option option = new Option(this);
        for (int i = 0; i < args.length; i++)
            if (this.optionTypes[i].accept(args[i]))
                option.put(this.optionTypes[i], args[i]);
            else return null;
        return option;
    }
}