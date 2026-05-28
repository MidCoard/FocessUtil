package top.focess.util.yaml;

import java.io.IOException;

/**
 * Thrown to indicate there is any exception thrown in the yaml loading process
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class YamlLoadException extends IOException {

    /**
     * Constructs a YamlLoadException
     *
     * @param e the exception
     */
    public YamlLoadException(final Exception e) {
        super(e);
    }
}
