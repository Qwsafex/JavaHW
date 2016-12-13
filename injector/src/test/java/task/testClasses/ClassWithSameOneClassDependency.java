package task.testClasses;

public class ClassWithSameOneClassDependency {
    public final ClassWithoutDependencies dependency;

    public ClassWithSameOneClassDependency(ClassWithoutDependencies dependency) {
        this.dependency = dependency;
    }
}
