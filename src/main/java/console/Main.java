package console;

import vcs.EmptyCommitMessageException;
import vcs.NothingToCommitException;
import vcs.VCS;
import vcs.VCSException;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        VCS vcs;
        try {
            vcs = new VCS();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        switch (args[0]) {
            case "add": {
                try {
                    vcs.add(args[1]);
                } catch (IOException e) {
                    printException(e);
                }
                break;
            }
            case "commit": {
                try {
                    vcs.commit(args[1]);
                } catch (Exception e) {
                    printException(e);
                }
                break;
            }
            case "branch": {
                if (args[1].equals("-d")) {
                    try {
                        vcs.deleteBranch(args[2]);
                    } catch (VCSException | IOException e) {
                        printException(e);
                    }
                }
                else {
                    try {
                        vcs.createBranch(args[1]);
                    } catch (IOException e) {
                        printException(e);
                    }
                }
                break;
            }
            case "log": {
                try {
                    List<String> log = vcs.log();
                    for (String record : log) {
                        System.out.print(record);
                        System.out.println("---------------------");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    printException(e);
                }
                break;
            }
            case "checkout": {
                try {
                    vcs.checkout(args[1]);
                } catch (Exception e) {
                    printException(e);
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
                } catch (Exception e) {
                    printException(e);
                }
            }
        }
    }

    private static void printException(Exception e) {
        System.out.println(e.getClass().toString() + " " + e.getMessage());
    }
}
