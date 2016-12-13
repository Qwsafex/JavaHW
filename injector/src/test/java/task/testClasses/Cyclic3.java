package task.testClasses;

public class Cyclic3 extends Cyclic1 implements Cyclic2 {

    public Cyclic3(Cyclic2 b) {
        super(b);
    }
}
