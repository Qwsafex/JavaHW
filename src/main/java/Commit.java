public class Commit extends GitObject {
    private String message;
    private Tree tree;
    private Commit prevCommit;
    Commit(){
        type = ObjectType.COMMIT;
    }
}
