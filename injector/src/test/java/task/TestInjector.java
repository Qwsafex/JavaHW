package task;

import org.junit.Test;
import task.testClasses.*;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertTrue;


public class TestInjector {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("task.testClasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "task.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("task.testClasses.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "task.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("task.testClasses.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    private static final String TEST_CLASSES = "task.testClasses.";

    @Test(expected = InjectionCycleException.class)
    public void cyclicDependency() throws Exception {
         Object object = Injector.initialize(TEST_CLASSES + "Cyclic1", Arrays.asList(TEST_CLASSES + "Cyclic2",
                                                                                     TEST_CLASSES + "Cyclic3"));
    }

    @Test
    public void checkDiamondDependencies() throws Exception {
        ClassWithDiamondDependencies object = (ClassWithDiamondDependencies)
                        Injector.initialize(TEST_CLASSES + "ClassWithDiamondDependencies",
                                            Arrays.asList(TEST_CLASSES + "ClassWithOneClassDependency",
                                                          TEST_CLASSES + "ClassWithSameOneClassDependency"));
        if (!object.a.dependency.equals(object.b.dependency)){
            throw new Exception("Different objects of same class");
        }
    }

    @Test(expected = AmbiguousImplementationException.class)
    public void checkAmbiguousImplementations() throws Exception {
            Object object = Injector.initialize(TEST_CLASSES + "ClassWithAmbiguousImplementations",
                                                Arrays.asList(TEST_CLASSES + "FirstImplementation",
                                                              TEST_CLASSES + "SecondImplementation"));
    }

    @Test(expected = ImplementationNotFoundException.class)
    public void checkImplementationNotFound() throws Exception {
        Object object = Injector.initialize(TEST_CLASSES + "ClassWithNoImplementationDependency",
                                            Collections.singletonList(TEST_CLASSES + "InterfaceWithoutImplementation"));
    }
}
