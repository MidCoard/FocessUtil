package top.focess.util.yaml;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Section of YamlConfiguration.
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class YamlConfigurationSection extends YamlConfiguration {


    private final YamlConfiguration parent;

    public YamlConfigurationSection(final YamlConfiguration parent, @Nullable final Map<String, Object> values) {
        super(values);
        this.parent = parent;
    }

    /**
     * Get the parent section
     *
     * @return the parent section
     */
    public YamlConfiguration getParent() {
        return this.parent;
    }

}
