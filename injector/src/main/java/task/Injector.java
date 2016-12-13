package task;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Injector {

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        return recursivelyInitialize(rootClassName, implementationClassNames, new HashMap<>(), new HashMap<>());
    }

    private static Object recursivelyInitialize(String className, List<String> implementationClassNames,
                                                HashMap<String, Boolean> visited,
                                                HashMap<String, Object> instances) throws Exception {
        if (visited.getOrDefault(className, false)){
            if (instances.containsKey(className)) { // We have already initialized this class
                return instances.get(className);
            }
            else { // Cyclic dependency
                throw new InjectionCycleException();
            }
        }
        visited.put(className, Boolean.TRUE);

        Class<?> rootClass = Class.forName(className);
        if (rootClass.isInterface() || Modifier.isAbstract(rootClass.getModifiers())) { // Let's find an implementation
            String implementation = null;
            for (String implementationName : implementationClassNames) {
                Class<?> implementationClass = Class.forName(implementationName);
                if (!rootClass.equals(implementationClass) && rootClass.isAssignableFrom(implementationClass)){
                    if (implementation == null){
                        implementation = implementationName;
                    }
                    else{
                        throw new AmbiguousImplementationException();
                    }
                }
            }
            if (implementation == null) {
                throw new ImplementationNotFoundException();
            }
            Object implementationObject = recursivelyInitialize(implementation, implementationClassNames,
                                                                visited, instances);
            instances.put(className, implementationObject);
            return implementationObject;
        }

        Constructor<?> constructor = rootClass.getConstructors()[0];
        Class<?>[] constructorParamTypes = constructor.getParameterTypes();
        ArrayList<Object> constructorParams = new ArrayList<>();
        for (Class<?> param : constructorParamTypes) {
            String paramClassName = param.getCanonicalName(); // TODO: double check this
            Class<?> paramClass = Class.forName(paramClassName);
            if (!paramClass.isInterface() && !Modifier.isAbstract(paramClass.getModifiers())){ // Check if class is in list
                boolean found = false;
                for (String implementationName : implementationClassNames) {
                    if (paramClassName.equals(implementationName)){
                        found = true;
                        break;
                    }
                }
                if (!found){
                    throw new ImplementationNotFoundException();
                }
            }
            constructorParams.add(recursivelyInitialize(paramClassName, implementationClassNames,
                                                        visited, instances));
        }
        Object classInstance = constructor.newInstance(constructorParams.toArray());
        instances.put(className, classInstance);
        return classInstance;
    }
}