package top.focess.util.yaml;

import java.io.IOException;

/**
 * Thrown to indicate that fail to save yaml file.
 */
public class YamlSaveException extends RuntimeException {

    public YamlSaveException(IOException e) {
        super(e);
    }
}
