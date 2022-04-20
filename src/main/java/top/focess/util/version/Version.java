package top.focess.util.version;

import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.focess.util.serialize.FocessSerializable;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a version of a plugin.
 */
public class Version implements FocessSerializable,Comparable<Version> {
    /**
     * Represents an alpha version of a plugin.
     */
    public static final Version ALPHA_VERSION = new Version("alpha");

    /**
     * Represents a beta version of a plugin.
     */
    public static final Version BETA_VERSION = new Version("beta");

    /**
     * Represents a build version of a plugin.
     */
    public static final Version BUILD_VERSION = new Version("build");

    /**
     * Represents a default release version of a plugin.
     */
    public static final Version DEFAULT_VERSION = new Version("1.0.0");
    private final int length;
    /**
     * The major version
     */
    private int major;
    /**
     * The minor version
     */
    private int minor;
    /**
     * The revision version
     */
    private int revision;
    /**
     * The build version
     */
    private String build;

    /**
     * Constructs a new version with the specified version numbers and build.
     *
     * @param major    the major version number
     * @param minor    the minor version number
     * @param revision the revision version number
     * @param build    the build version
     */
    public Version(final int major, final int minor, final int revision, final String build) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.build = build;
        this.length = 4;
    }

    /**
     * Constructs a new version with the specified version numbers.
     *
     * @param major    the major version number
     * @param minor    the minor version number
     * @param revision the revision version number
     */
    public Version(final int major, final int minor, final int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.length = 3;
    }

    /**
     * Constructs a new version with the specified version numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     */
    public Version(final int major, final int minor) {
        this.major = major;
        this.minor = minor;
        this.length = 2;
    }

    /**
     * Constructs a new version with the specified version.
     *
     * @param version the version to be parsed.
     */
    public Version(@NotNull final String version) {
        final String[] temp = version.split("\\.");
        try {
            if (temp.length == 1)
                this.build = temp[0];
            else if (temp.length == 2) {
                this.major = Integer.parseInt(temp[0]);
                final String[] temp2 = temp[1].split("-");
                this.minor = Integer.parseInt(temp2[0]);
                if (temp2.length == 2)
                    this.build = temp2[1];
                else if (temp2.length > 2) throw new VersionFormatException(version);
            } else if (temp.length == 3) {
                this.major = Integer.parseInt(temp[0]);
                this.minor = Integer.parseInt(temp[1]);
                final String[] temp2 = temp[2].split("-");
                this.revision = Integer.parseInt(temp2[0]);
                if (temp2.length == 2)
                    this.build = temp2[1];
                else if (temp2.length > 2) throw new VersionFormatException(version);
            } else if (temp.length == 4) {
                this.major = Integer.parseInt(temp[0]);
                this.minor = Integer.parseInt(temp[1]);
                this.revision = Integer.parseInt(temp[2]);
                this.build = temp[3];
            } else throw new VersionFormatException(version);
        } catch (final NumberFormatException e) {
            throw new VersionFormatException(version);
        }
        this.length = temp.length;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getRevision() {
        return this.revision;
    }

    public String getBuild() {
        return this.build;
    }

    /**
     * Indicate whether this version is greater than the specified version
     *
     * <p>
     * Note: This method is equivalent to {@code this.compareTo(version) > 0}.
     * this method return true also indicate {@link #lower(Version)} and {@link #equals(Object)} return false.
     *
     *
     * @param version the version to compare to
     * @return true if this version is greater than the specified version, false otherwise
     * @see #lower(Version)
     * @see #equals(Object)
     * @see #compareTo(Version)
     */
    public boolean higher(@NonNull final Version version) {
        return this.compareTo(version) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (major != version.major) return false;
        if (minor != version.minor) return false;
        if (revision != version.revision) return false;
        return Objects.equals(build, version.build);
    }

    /**
     * Indicate whether this version is lower than the specified version
     *
     * <p>
     * Note: This method is equivalent to {@code version.higher(this)}.
     * this method return true also indicate {@link #higher(Version)} (Version)} and {@link #equals(Object)} return false.
     *
     * @param version the version to compare to
     * @return true if this version is lower than the specified version, false otherwise
     * @see #higher(Version)
     * @see #equals(Object)
     * @see #compareTo(Version)
     */
    public boolean lower(@NonNull final Version version) {
        return version.higher(this);
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + revision;
        result = 31 * result + (build != null ? build.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (this.length == 1)
            return this.build;
        else if (this.length == 2)
            return this.major + "." + this.minor + (this.build == null ? "" : "-" + this.build);
        else if (this.length == 3)
            return this.major + "." + this.minor + "." + this.revision + (this.build == null ? "" : "-" + this.build);
        else if (this.length == 4)
            return this.major + "." + this.minor + "." + this.revision + "." + this.build;
        throw new IllegalStateException("Invalid version length: " + this.length);
    }

    @Nullable
    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("version", toString());
        return map;
    }

    public static Version deserialize(Map<String,Object> map) {
        return new Version((String) map.get("version"));
    }

    @Override
    public int compareTo(@NotNull Version version) {
        if (this.getMajor() != version.getMajor())
            return Integer.compare(this.getMajor(), version.getMajor());
        if (this.getMinor() != version.getMinor())
            return Integer.compare(this.getMinor() , version.getMinor());
        if (this.getRevision() != version.getRevision())
            return Integer.compare(this.getRevision() , version.getRevision());
        if (Objects.equals(this.getBuild(), version.getBuild()))
            return 0;
        if (this.getBuild() == null)
            return -1;
        if (version.getBuild() == null)
            return 1;
        return this.getBuild().compareTo(version.getBuild());
    }
}
