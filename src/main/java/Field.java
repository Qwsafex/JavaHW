import java.util.ArrayList;
import java.util.Collections;

public class Field {
    private final int[][] field;

    Field(int n){
        if (n % 2 == 1) {
            throw new IllegalArgumentException("N should be even!");
        }
        field = new int[n][n];
        ArrayList<Integer> bits = new ArrayList<>();
        for (int i = 0; i < n*n; i++) {
            bits.add(i % 2);
        }
        Collections.shuffle(bits);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                field[i][j] = bits.get(i*n + j);
            }
        }
    }

    int get(int i, int j){
        return field[i][j];
    }


}
