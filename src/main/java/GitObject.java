import java.io.Serializable;

public class GitObject implements Serializable{
    public enum ObjectType {TREE, BLOB, COMMIT}
    public String name;
    public ObjectType type;
}
