package task.testClasses;

public class ClassWithDiamondDependencies {
    public final ClassWithOneClassDependency a;
    public final ClassWithSameOneClassDependency b;

    public ClassWithDiamondDependencies(ClassWithOneClassDependency a,
                                        ClassWithSameOneClassDependency b) {
        this.a = a;
        this.b = b;
    }
}
