package top.focess.util.yaml;

import java.io.IOException;

/**
 * Thrown to indicate that fail to save yaml file.
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class YamlSaveException extends RuntimeException {

    public YamlSaveException(IOException e) {
        super(e);
    }
}
