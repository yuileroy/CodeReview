package pattern.singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;

public final class EasterBunny implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Private constructor prevents construction outside this class.
    private EasterBunny() {
    }

    // Single instance created upon class loading.
    private static final EasterBunny fINSTANCE = new EasterBunny();

    public static EasterBunny getInstance() {
        return fINSTANCE;
    }

    // If the singleton implements Serializable, then this
    // method must be supplied.
    private Object readResolve() throws ObjectStreamException {
        return fINSTANCE;
    }
}
