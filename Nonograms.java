import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Nonograms {
    static int[][] G0, G1, G2 = new int[5][5]; // 圖

    static int statusG = 0; // -1 = CONFLICT; 0 = INCOMPLETE; 1 = COMPLETE.

    public static void main(String[] args) {
        int[][] G = { { -1, -1, -1, -1, -1 }, { -1, -1, -1, -1, -1 }, { -1, -1, -1, -1, -1 }, { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 } };
        String[][] ans = { { "4", "2 2", "1", "2", "1 1" }, { "2 1", "2", "1 1", "2 1", "3" } };

        // 測試propagate
        G = PROPAGATE(G, ans);
        System.out.println("-------------------");
        for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
        System.out.printf("%3d", G[i][j]);
        }
        System.out.println();
        }

        // 測試fix&paint
        // LinkedList<Integer> D = new LinkedList<>();
        // int[] L = { -1, -1, -1, 1, -1 };
        // // D.add(1);
        // D.add(3);
        // for (int p : Paint(L.length, D.size(), D, L)) {
        //     System.out.print(p);
        // }
    }

    // 這我還沒動
    static void FP(int[][] G) {
        G0 = G.clone();
        G1 = G.clone();
        // status 0 = 無解; 1=有解; 2=沒解完...不知道啦我先這樣定義
        // PROPAGATE(G);
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

    static int[][] PROPAGATE(int[][] G, String[][] ans) {
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
            indexList.add(new int[] { i, 1 });
        }
        // 加直的
        for (int j = 0; j < 5; j++) {
            int[] a = new int[5];
            for (int i = 0; i < 5; i++) {
                a[i] = G[i][j];
            }
            LG.add(a);
            DG.add(ans[1][j]);
            indexList.add(new int[] { j, 0 });
        }

        // 做while
        while (!LG.isEmpty()) {

            // 拿一串格子
            int[] L = LG.removeFirst();// 從LG拿一行L

            // 拿一串條件
            String[] d = DG.removeFirst().split(" "); // 拆答案
            LinkedList<Integer> D = new LinkedList<>(); // 裝答案用的list
            for (String p : d) {// 塞條件進去
                D.add(Integer.valueOf(p));
            }

            // 拿一個位置
            int[] indexInfo = indexList.removeFirst();

            int SR = L.length; // i
            int DR = D.size(); // j

            // 6.
            if (!Fix0(SR, DR, D, L) && !Fix1(SR, DR, D, L)) {
                statusG = -1;
                return G;
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
            // if (indexInfo[1] == 0) { // 橫的第幾個有變，加直的第幾行
            // for (int p : II) {
            // int[] col = new int[5];
            // for (int i = 0; i < 5; i++) {
            // col[i] = G[i][p];
            // }
            // if (!LG.contains(a)) {// 如果G沒有就放進去
            // LG.add(col); // 放圖
            // DG.add(ans[1][p]); // 放題目
            // indexList.add(new int[] { p, 0 }); // 放位置
            // }
            // }
            // } else { // 直的有變加橫的
            // for (int p : II) {
            // if (!LG.contains(G[p])) {
            // LG.add(G[p]);
            // DG.add(ans[0][p]);
            // indexList.add(new int[] { p, 1 });
            // }
            // }
            // }

            // 10.更新IIG
            if (indexInfo[1] == 0) { // 橫的
                int rowIndex = indexInfo[0];
                for (int p : II) {
                    IIG[rowIndex][p] = newPainted[p]; // 更新 IIG 的指定行
                }
            } else { // 直的
                int colIndex = indexInfo[0];
                for (int p : II) {
                    IIG[p][colIndex] = newPainted[p]; // 更新 IIG 的指定列
                }
            }

            // 12.檢查填完沒
            if (allColorsFilled(G)) {
                statusG = 1;
            } else {
                statusG = 0;
            }
            // 試印看看
            System.out.println("index:" + indexInfo[0] + indexInfo[1]);
            for (int j = 0; j < 5; j++) {
                System.out.printf("%2d", newPainted[j]);
            }
            System.out.println("\n");
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    System.out.printf("%2d", IIG[i][j]);
                }
                System.out.println();
            }
            System.out.println("\n");
        }
        return IIG;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////// 這邊我寫好了不要動了啊啊啊啊啊啊啊啊啊啊啊//////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    static int[] Paint(int i, int j, LinkedList<Integer> D, int[] L) {
        if (i <= 0) {
            return L;
        }
        // Paint'(i,j)
        if (L[i - 1] == 0) {
            i--;
            return Paint(i, j, D, L);
        } else if (L[i - 1] == 1) {
            return Paint1(i, j, D, L);
        } else {

            Boolean f0 = Fix0(i, j, D, L);
            Boolean f1 = Fix1(i, j, D, L);
            int[] p0 = L.clone();
            int[] p1 = L.clone();
            if (f0) {
                p0 = Paint0(i, j, D, L);
            }
            if (f1) {
                p1 = Paint1(i, j, D, L);
            }
            int[] p;
            if (f0 && f1) {
                p = Merge(p1, p0);
            } else if (f0 && !f1) {
                p = p0;
            } else if (f1 && !f0) {
                p = p1;
            } else {
                p = L.clone();
            }
            return p;
        }
    }

    static int[] Paint0(int i, int j, LinkedList<Integer> D, int[] L) {
        if (i <= 0) {
            return L;
        }
        int[] result = L.clone();
        result[i - 1] = 0;
        result = Paint(i - 1, j, D, result);
        // System.out.println("P0:" + result[0] + result[1] + result[2] + result[3] + result[4]);
        return result;
    }

    static int[] Paint1(int i, int j, LinkedList<Integer> D, int[] L) {
        if (D.isEmpty()) {
            return L;
        }
        LinkedList<Integer> d = new LinkedList<>(D);
        int dj = d.removeLast(); // dj = 要填1的格子數
        if (i < dj) {
            return L;
        }
        int[] result = L.clone();
        for (int k = i - 1; k >= i - dj; k--) {
            result[k] = 1;
        }
        if (i == dj) {
            result = Paint(i - dj, j - 1, d, result);
        } else {
            result[i - dj - 1] = 0;
            result = Paint(i - dj - 1, j - 1, d, result);
        }
        // System.out.println("P1:" + result[0] + result[1] + result[2] + result[3] + result[4]);
        return result;
    }

    static int[] Merge(int[] s, int[] t) {
        int[] merged = new int[5];
        for (int k = 0; k < 5; k++) {
            merged[k] = MergeC(s[k], t[k]);
        }
        return merged;
    }

    static int MergeC(int sk, int tk) {
        if (sk == 0 && tk == 0) {
            return 0;
        } else if (sk == 1 && tk == 1) {
            return 1;
        } else {
            return -1; // -1 = 還沒填
        }
    }

    static Boolean Fix0(int i, int j, LinkedList<Integer> D, int[] L) {
        if (i == 0 && j == 0) {
            return true;
        } else if (i == 0 && j > 0) {
            return false;
        } else if (i < 0 || j < 0) {
            return false; // 防止負值遞歸
        } else {
            return Fix0(i - 1, j, D, L) || Fix1(i - 1, j, D, L);
        }
    }

    static Boolean Fix1(int i, int j, LinkedList<Integer> D, int[] L) {
        if (i == 0 && j == 0) {
            return true;
        } else if (i == 0 && j > 0) {
            return false;
        } else if (i < 0 || j < 0 || D.isEmpty()) {
            return false; // 防止負值遞歸或 D 為空
        } else {
            LinkedList<Integer> d = new LinkedList<>(D);
            int dj = d.removeFirst();
            if (i < dj) {
                return false;
            }
            for (int k = i - 1; k >= i - dj; k--) {
                if (L[k] == 0)
                    return false;
            }
            if (i == dj || d.isEmpty()) {// 最後一個要填1的就不用留空位了直接塞
                return Fix1(i - dj, j - 1, d, L) || Fix0(i - dj, j - 1, d, L);
            }
            return Fix1(i - dj - 1, j - 1, d, L) || Fix0(i - dj - 1, j - 1, d, L);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ///
    ///
    /// 下面還沒
    static int status(Object o) {
        // 等等再寫我不知道這要幹嘛
        return 0;
    }

    static void UPDATEONALLG(Object o) {
        // 這我也不知道是啥先放放
    }

    // static void PROBE(int p) {
    // PROPAGATE(G0);
    // PROPAGATE(G1);
    // if (status(G0) == 0 || status(G1) == 0) {

    // }
    // }

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