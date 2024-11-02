import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Nonograms {
    static int[][] G0, G1, G2 = new int[5][5]; // 圖
    static String[][] ans = new String[2][5]; // 題目
    static int statusG = 0; // -1 = CONFLICT; 0 = INCOMPLETE; 1 = COMPLETE.

    public static void main(String[] args) {
        int[][] G = new int[5][5];
        PROPAGATE(G);
    }

    static void FP(int[][] G) {
        G0 = G.clone();
        G1 = G.clone();
        // status 0 = 無解; 1=有解; 2=沒解完...不知道啦我先這樣定義
        PROPAGATE(G);
        if (status(G) == 0 || status(G) == 1) {
            return;
        }
        UPDATEONALLG(G);
        // for (所有p) {
        // PROBE(p);
        // if (status(G) == 0 || status(G) == 1) {
        // return;
        // }
        // }
    }

    static void PROPAGATE(int[][] G) {
        int[][] IIG = new int[G.length][];// IIG: 改過的G
        for (int i = 0; i < G.length; i++)
            IIG[i] = G[i].clone();
        // put all rows & clos into LG
        LinkedList<int[]> LG = new LinkedList<>(); // 存G的行跟列
        LinkedList<String> DG = new LinkedList<>(); // 存每行答案
        LinkedList<int[]> indexList = new LinkedList<>(); // 存哪一行

        // 加橫的
        for (int i = 0; i < 5; i++) {
            LG.add(G[i]);
            DG.add(ans[0][i]);
            indexList.add(new int[] { i, 0 });
        }
        // 加直的
        for (int j = 0; j < 5; j++) {
            int[] a = new int[5];
            for (int i = 0; i < 5; i++) {
                a[i] = G[i][j];
            }
            LG.add(a);
            DG.add(ans[1][j]);
            indexList.add(new int[] { j, 1 });
        }

        // 做while
        while (!LG.isEmpty()) {
            int[] L = LG.poll();// 從LG拿一行L
            String[] D = DG.getFirst().split(" "); // 拆答案
            int[] indexInfo = indexList.poll(); // 拿一行位置
            int SR = L.length;
            int DR = D.length;

            // 6.
            if (!Fix(SR, DR)) {
                statusG = -1;
                return;
            }

            // 7.畫一行
            int[] newPainted = Paint(SR, DR, D, L);

            // 8. II = 新的點p
            List<Integer> II = new ArrayList<>(); // II存一維座標
            for (int i = 0; i < newPainted.length; i++) {
                if (newPainted[i] != L[i]) { // 如果有新的變化
                    II.add(i); // 那行的第幾個
                }
            }

            // 9. 把有改過p的那行放進去再檢查
            if (indexInfo[1] == 0) { // 橫的第幾個有變，加直的第幾行
                for (int p : II) {
                    int[] a = new int[5];
                    for (int i = 0; i < 5; i++) {
                        a[i] = G[i][p];
                    }
                    if (!LG.contains(a)) {// 如果G沒有就放進去
                        LG.add(a); // 放圖
                        DG.add(ans[1][p]); // 放題目
                        indexList.add(new int[] { p, 1 }); // 放位置
                    }
                }
            } else { // 直的有變加橫的
                for (int p : II) {
                    if (!LG.contains(G[p])) {
                        LG.add(G[p]);
                        DG.add(ans[0][p]);
                        indexList.add(new int[] { p, 0 });
                    }
                }
            }

            // 10.更新IIG
            if (indexInfo[1] == 0) { // 橫的
                int rowIndex = indexInfo[0];
                for (int p : II) {
                    IIG[rowIndex][p] = newPainted[p]; // 更新 IIG 的指定行
                }
            } else { // 直的
                int colIndex = indexInfo[0];
                for (int p : II) {
                    G[p][colIndex] = newPainted[p]; // 更新 IIG 的指定列
                }
            }

            // 12.檢查填完沒
            if (allColorsFilled(G)) {
                statusG = 1;
            } else {
                statusG = 0;
            }
        }
    }

    static int[] Paint(int i, int i, String[] D, int[] L) {
        int[] l = L;
        if (i == 0) {
            return l;
        }
        return l;
    }

    static int status(Object o) {
        // 等等再寫我不知道這要幹嘛
        return 0;
    }

    static void UPDATEONALLG(Object o) {
        // 這我也不知道是啥先放放
    }

    static boolean Fix(int i, int j) {
        if (i == 0 && j == 0) {
            return true;
        } else if (i == 0 && j > 0) {
            return false;
        } else {
            return 
        }
    }

    static void PROBE(int p) {
        PROPAGATE(G0);
        PROPAGATE(G1);
        if (status(G0) == 0 || status(G1) == 0) {

        }
    }

    static boolean allColorsFilled(int[][] G) {
        for (int[] row : G) {
            for (int cell : row) {
                if (cell == -1) { // 假設 -1 代表未填色
                    return false;
                }
            }
        }
        return true;
    }
}