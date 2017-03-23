package vcs;

public class VCSRef {
    private static final String REF_PREFIX = "ref: ";
    private String name;
    public static boolean isRef(String name) {
        return name.startsWith(REF_PREFIX);
    }
    public VCSRef(String name) {
        this.name = name;
    }
    static VCSRef fromString(String refString) {
        return new VCSRef(refString.substring(REF_PREFIX.length()));
    }

    @Override
    public String toString() {
        return REF_PREFIX + name;
    }

    public String getName() {
        return name;
    }
}
