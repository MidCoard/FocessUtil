package top.focess.util.serialize;

/**
 * Used to find class by its name.
 */
public interface ClassFinder {

    default Class<?> forName0(final String className) throws ClassNotFoundException {
        if (className.equals("int"))
            return int.class;
        if (className.equals("long"))
            return long.class;
        if (className.equals("float"))
            return float.class;
        if (className.equals("double"))
            return double.class;
        if (className.equals("boolean"))
            return boolean.class;
        if (className.equals("byte"))
            return byte.class;
        if (className.equals("char"))
            return char.class;
        if (className.equals("short"))
            return short.class;
        return this.forName(className);
    }

    /**
     * Finds the class by its name.
     * @param className the name of the class
     * @return the class
     * @throws ClassNotFoundException if the class is not found
     */
    Class<?> forName(final String className) throws ClassNotFoundException;
}
