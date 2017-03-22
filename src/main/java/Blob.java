import java.util.List;

public class Blob extends GitTreeObject {
    private List<Byte> content;
    Blob(){
        type = ObjectType.BLOB;
    }
}
