import java.util.ArrayList;
import java.util.List;

public class Tree extends GitTreeObject {
    private List<GitTreeObject> children;
    Tree(){
        children = new ArrayList<>();
        type = ObjectType.TREE;
    }


}
