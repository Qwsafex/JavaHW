package vcs;

class VCSRef {
    private static final String REF_PREFIX = "ref: ";
    private String name;
    static boolean isRef(String name) {
        return name.startsWith(REF_PREFIX);
    }
    VCSRef(String name) {
        this.name = name;
    }
    static VCSRef fromString(String refString) {
        return new VCSRef(refString.substring(REF_PREFIX.length()));
    }

    @Override
    public String toString() {
        return REF_PREFIX + name;
    }

    String getName() {
        return name;
    }
}
