import vcs.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        VCS vcs;
        try {
            vcs = new VCS();
        } catch (IOException | VCSException e) {
            System.out.println(e.getMessage());
            return;
        }
        switch (args[0]) {
            case "add": {
                try {
                    vcs.add(Paths.get(args[1]));
                } catch (IOException | VCSException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "commit": {
                try {
                    vcs.commit(args[1]);
                } catch (NothingToCommitException e) {
                    System.out.println("Nothing to commit.");
                } catch (IOException | VCSException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "branch": {
                if (args[1].equals("-d")) {
                    try {
                        vcs.deleteBranch(args[2]);
                    } catch (VCSException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    try {
                        vcs.createBranch(args[1]);
                    } catch (VCSException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
            }
            case "log": {
                try {
                    List<Commit> log = vcs.log();
                    for (Commit record : log) {
                        System.out.print(record.toString());
                        System.out.println("---------------------");
                    }
                } catch (IOException | VCSFilesCorruptedException | RefNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "checkout": {
                try {
                    vcs.checkout(args[1]);
                } catch (IOException | BranchNotFoundException | VCSFilesCorruptedException | RefNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "merge": {
                try {
                    List<String> conflicts = vcs.merge(args[1]);
                    if (conflicts.isEmpty()) {
                        System.out.println("Merged successfully");
                    }
                    else {
                        System.out.println("Following files conflict:");
                        conflicts.forEach(System.out::println);
                    }
                } catch (VCSException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
