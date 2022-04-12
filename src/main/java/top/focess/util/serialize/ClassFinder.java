package top.focess.util.serialize;

/**
 * Used to find class by its name.
 */
public interface ClassFinder {

    /**
     * Finds the class by its name.
     * @param className the name of the class
     * @return the class
     * @throws ClassNotFoundException if the class is not found
     */
    Class<?> forName(final String className) throws ClassNotFoundException;
}
