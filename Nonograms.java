import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Nonograms {
    static int X = 20;
    static int[][] G0, G1, G = new int[X][X]; // 圖
    static String[][] ans = new String[2][X];
    static int statusG = 0; // -1 = CONFLICT; 0 = INCOMPLETE; 1 = COMPLETE.

    public static void main(String[] args) {
        try {
            File file = new File("Nonogram.txt");
            Scanner scanner = new Scanner(file);

            for (int i = 0; i < X; i++) {
                for (int j = 0; j < X; j++) {
                    G[i][j] = -1;
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < X; j++) {
                    ans[i][j] = scanner.nextLine();
                }
            }

            // 測試propagate
            G = PROPAGATE(G, ans);
            System.out.println("-------------------");
            for (int i = 0; i < X; i++) {
                for (int j = 0; j < X; j++) {
                    System.out.printf("%3d", G[i][j]);
                }
                System.out.println();
            }

            // 測試fix&paint
            // LinkedList<Integer> D = new LinkedList<>();
            // int[] L = { 1, 1, -1, -1, 1, -1, -1, -1, -1, -1 };
            // D.add(6);
            // D.add(3);
            // for (int p : Paint(L.length, D.size(), D, L)) {
            // System.out.printf("%3d", p);
            // }
        } catch (FileNotFoundException ex) {
            System.err.println("File Not Found.");
        }
    }

    // FP
    // static int[][] FP1(String[][] ans) {
    // int newp = 0;
    // do {
    // int[][] IIG = new int[X][X];
    // IIG = PROPAGATE(ans, IIG, G, statusG);
    // if (statusG == -1 || statusG == 1) {
    // return G;
    // }
    // newp = UPDATEONALLG(IIG);
    // System.out.println("PPG:\n-------------------");
    // for (int i = 0; i < X; i++) {
    // for (int j = 0; j < X; j++) {
    // System.out.printf("%3d", G[i][j]);
    // }
    // System.out.println();
    // }
    // while (!P.isEmpty()) {
    // // for (int i = 0; i < 20; i++) {
    // int[] p = P.removeFirst();
    // IIG = PROBE(p, ans, IIG);
    // // if (statusG == -1 || statusG == 1) {
    // // return G;
    // // }
    // newp += UPDATEONALLG(IIG);
    // }
    // // System.out.println("newp:"+newp);
    // } while (newp != 0);
    // return G;
    // }

    // static int UPDATEONALLG(int[][] IIG) {
    // // 更新所有 G 中像素的狀態
    // int p = 0;// 更新幾個
    // for (int i = 0; i < X; i++) {
    // for (int j = 0; j < X; j++) {
    // if (G[i][j] == -1 && IIG[i][j] != -1) {
    // G[i][j] = IIG[i][j];
    // Gp0[i][j] = IIG[i][j];
    // Gp1[i][j] = IIG[i][j];
    // p++;
    // } else if (G[i][j] == -1 && IIG[i][j] == -1) {
    // P.add(new int[] { i, j });
    // }
    // }
    // }
    // return p;
    // }

    // static int[][] PROBE(int[] p, String[][] ans, int[][] IIG) {
    // for (int i = 0; i < X; i++) {
    // for (int j = 0; j < X; j++) {
    // IIG[i][j] = -1;
    // }
    // }
    // // 1.
    // Gp0[p[0]][p[1]] = 0; // Gp0猜0
    // Gp0 = PROPAGATE(ans, IIG, Gp0, statusG0);
    // // System.out.println("Gp0:\n-------------------");
    // // for (int i = 0; i < X; i++) {
    // // for (int j = 0; j < X; j++) {
    // // System.out.printf("%3d", Gp0[i][j]);
    // // }
    // // System.out.println();
    // // }
    // // 2.
    // Gp1[p[0]][p[1]] = 1; // Gp0猜1
    // Gp1 = PROPAGATE(ans, IIG, Gp1, statusG1);
    // // System.out.println("Gp1:\n-------------------");
    // // for (int i = 0; i < X; i++) {
    // // for (int j = 0; j < X; j++) {
    // // System.out.printf("%3d", Gp1[i][j]);
    // // }
    // // System.out.println();
    // // }
    // // 3~10.
    // if (statusG0 == -1 && statusG1 == -1) {
    // statusG = -1;
    // // System.out.println("NOT:" + p[0] + p[1]);
    // return IIG;
    // } else if (statusG0 == -1) {
    // // System.out.println("G1:" + p[0] + p[1]);
    // for (int i = 0; i < Gp1.length; i++)
    // IIG[i] = Gp1[i].clone();
    // } else if (statusG1 == -1) {
    // // System.out.println("G0:" + p[0] + p[1]);
    // for (int i = 0; i < Gp0.length; i++)
    // IIG[i] = Gp0[i].clone();
    // } else {
    // // System.out.println("MG:" + p[0] + p[1]);
    // for (int i = 0; i < X; i++) {
    // for (int j = 0; j < X; j++) {
    // IIG[i][j] = MergeC(Gp0[i][j], Gp1[i][j]);
    // // if (IIG[i][j] != -1)
    // // System.out.print("{" + i + j + "} ");
    // }
    // }
    // // System.out.println();
    // }
    // return IIG;
    // }

    static int[][] PROPAGATE(int[][] Graph, String[][] ans) {
        int[][] IIG = new int[Graph.length][];// IIG: 改過的G
        for (int i = 0; i < Graph.length; i++)
            IIG[i] = Graph[i].clone();
        // put all rows & clos into LG
        LinkedList<int[]> LG = new LinkedList<>(); // 存G的行跟列
        LinkedList<String> DG = new LinkedList<>(); // 存每行答案
        LinkedList<int[]> indexList = new LinkedList<>(); // 存哪一行: 1是橫的, 0是直的

        // 加橫的
        for (int i = 0; i < X; i++) {
            LG.add(IIG[i]);
            DG.add(ans[1][i]);
            indexList.add(new int[] { 1, i });
        }
        // 加直的
        for (int j = 0; j < X; j++) {
            int[] a = new int[X];
            for (int i = 0; i < X; i++) {
                a[i] = IIG[i][j];
            }
            LG.add(a);
            DG.add(ans[0][j]);
            indexList.add(new int[] { 0, j });
        }

        // 做while
        while (!LG.isEmpty()) {
            // System.out.print("stack:");
            // for (int[] elem : indexList) {
            // for (int i : elem) {
            // System.out.print(i);
            // }
            // System.out.print(" ");
            // }
            // System.out.println("");
            // 拿一串格子
            int[] L = LG.removeFirst();// 從LG拿一行L

            // 拿一串條件
            String[] d = DG.removeFirst().split(" "); // 拆答案
            LinkedList<Integer> D = new LinkedList<>(); // 裝答案用的list
            System.out.print("D:");
            for (String p : d) {// 塞條件進去
                D.add(Integer.valueOf(p));
                System.out.print(p);
            }
            System.out.println();

            // 拿一個位置
            int[] indexInfo = indexList.removeFirst();

            int SR = L.length; // i
            int DR = D.size(); // j

            // 印看看
            System.out.print("index " + indexInfo[0] + indexInfo[1] + ": ");
            for (int i = 0; i < X; i++) {
                System.out.print(L[i] + " ");
            }
            System.out.println();

            // 6.
            if (!Fix0(SR, DR, D, L) && !Fix1(SR, DR, D, L)) {
                System.out.println("Error");
                statusG = -1;
                return IIG;
            }

            // 7.畫一行
            int[] newPainted = Paint(SR, DR, D, L);

            // 8. II = 新的點p
            List<Integer> II = new ArrayList<>(); // II存一維座標
            for (int i = 0; i < newPainted.length; i++) {
                if (newPainted[i] != L[i] && L[i] == -1) { // 如果有新的變化
                    II.add(i); // 那行的第幾個
                }
            }

            // 更新IIG
            if (indexInfo[0] == 1) { // 橫的
                int rowIndex = indexInfo[1];
                for (int p : II) {
                    IIG[rowIndex][p] = newPainted[p]; // 更新 IIG 的指定行
                }
            } else { // 直的
                int colIndex = indexInfo[1];
                for (int p : II) {
                    IIG[p][colIndex] = newPainted[p]; // 更新 IIG 的指定列
                }
            }

            // // 9. 把有改過p的那行放進去再檢查
            if (indexInfo[0] == 1) { // 橫的第幾個有變，加直的第幾行
                for (int p : II) {
                    int[] col = new int[X];
                    for (int i = 0; i < X; i++) {
                        col[i] = IIG[i][p];
                    }
                    if (!LG.contains(col)) {// 如果沒有就放進去
                        LG.add(col); // 放圖
                        DG.add(ans[0][p]); // 放題目
                        indexList.add(new int[] { 0, p }); // 放位置
                    }
                }
            } else { // 直的有變加橫的
                for (int p : II) {
                    if (!LG.contains(IIG[p])) {
                        LG.add(IIG[p]);
                        DG.add(ans[1][p]);
                        indexList.add(new int[] { 1, p });
                    }
                }
            }

            // 12.檢查填完沒
            if (allColorsFilled(Graph)) {
                statusG = 1;
            } else {
                statusG = 0;
            }
            // 試印看看
            System.out.println("index:" + indexInfo[0] + indexInfo[1]);
            for (int j = 0; j < X; j++) {
                System.out.printf("%3d", newPainted[j]);
            }
            System.out.println("\n");
            for (int i = 0; i < X; i++) {
                for (int j = 0; j < X; j++) {
                    System.out.printf("%3d", IIG[i][j]);
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
        // System.out.println("P");

        if (i <= 0) {
            return L;
        }
        // Paint'(i,j)
        if (L[i - 1] == 0) {
            return Paint0(i, j, D, L);
        } else if (L[i - 1] == 1) {
            return Paint1(i, j, D, L);
        } else {
            // System.out.println("i:" + i);
            Boolean f0 = Fix0(i, j, D, L);
            Boolean f1 = Fix1(i, j, D, L);
            int[] p0 = L.clone();
            int[] p1 = L.clone();
            // System.out.println("F0:" + f0 + " F1:" + f1);
            if (f0) {
                p0 = Paint0(i, j, D, L);
            }
            if (f1) {
                p1 = Paint1(i, j, D, L);
            }
            int[] p;
            if (f0 && f1) {
                p = Merge(p0, p1);
            } else if (f0 && !f1) {
                p = p0;
            } else if (f1 && !f0) {
                p = p1;
            } else {
                p = L.clone();
            }
            // System.out.println("P:" + p[0] + p[1] + p[2] + p[3] + p[4]);
            return p;
        }
    }

    static int[] Paint0(int i, int j, LinkedList<Integer> D, int[] L) {
        // System.out.println("P0");
        if (i <= 0) {
            return L;
        }
        int[] result = L.clone();
        result[i - 1] = (result[i - 1] == -1) ? 0 : result[i - 1];
        result = Paint(i - 1, j, D, result);
        // System.out.println("P0:" + result[0] + result[1] + result[2] + result[3] +
        // result[4] + result[5] + result[6] + result[7] + result[8] + result[9]);
        return result;
    }

    static int[] Paint1(int i, int j, LinkedList<Integer> D, int[] L) {
        // System.out.println("P1");
        if (j <= 0) {
            return L;
        }
        int dj = D.get(--j); // dj = 要填1的格子數
        int[] result = L.clone();
        for (int k = i - 1; k >= i - dj; k--) {
            if (result[k] == -1) {
                result[k] = 1;
            } else if (result[k] == 0) {
                return result;
            }
        }
        if (i != dj) {
            result = Paint0(i - dj, j, D, result);
        }
        // System.out.println("P1:" + result[0] + result[1] + result[2] + result[3] +
        // result[4] + result[5] + result[6] + result[7] + result[8] + result[9]);
        return result;
    }

    static int[] Merge(int[] s, int[] t) {
        int[] merged = new int[X];
        // System.out.println("MGp0:" + s[0] + s[1] + s[2] + s[3] +
        // s[4] + s[5] + s[6] + s[7] + s[8] + s[9]);
        // System.out.println("MGp1:" + t[0] + t[1] + t[2] + t[3] +
        // t[4] + t[5] + t[6] + t[7] + t[8] + t[9]);
        for (int k = 0; k < X; k++) {
            merged[k] = MergeC(s[k], t[k]);
        }
        // System.out.println("MG:" + merged[0] + merged[1] + merged[2] + merged[3] +
        // merged[4] + merged[5] + merged[6] + merged[7] + merged[8] + merged[9]);
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
        // System.out.println("Fix0 i=" + i + " j=" + j);
        if (i == 0 && j == 0) {
            // System.out.println("Fix0:T");
            return true;
        } else if (i == 0 && j > 0) {
            // System.out.println("Fix0:F1 ");
            return false;
        } else if (i < 0 || j < 0) {
            // System.out.println("Fix0:F2");
            return false; // 防止負值遞歸
        } else if (L[i - 1] == 1) {
            // System.out.println("Fix0:F3");
            return false;
        } else {
            return Fix0(i - 1, j, D, L) || Fix1(i - 1, j, D, L);
        }
    }

    static Boolean Fix1(int i, int j, LinkedList<Integer> D, int[] L) {
        // System.out.println("Fix1 i=" + i + " j=" + j);
        if (i == 0 && j == 0) {
            // System.out.println("Fix1:T1");
            return true;
        } else if (i == 0 && j > 0) {
            // System.out.println("Fix1:F1 ");
            return false;
        } else if (i < 0 || j <= 0) {
            // System.out.println("Fix1:F2");
            return false; // 防止負值遞歸或 D 為空
        } else {
            int dj = D.get(--j);
            // System.out.println("d=" + dj);

            // 檢查當前條件是否可滿足
            if (i < dj) {
                // System.out.println("Fix1:F3");
                return false;
            }

            // 確保在 i 到 i - dj 範圍內沒有填滿的格子
            for (int k = i - 1; k >= i - dj; k--) {
                if (L[k] == 0) {
                    // System.out.println("Fix1:F4");
                    return false;
                }
            }

            if (i == dj && j == 0) {
                // System.out.println("Fix1:T2");
                return true;
            }
            // System.out.println("F1 to F0");
            return Fix0(i - dj, j, D, L);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ///
    ///
    /// 下面還沒

    static void UPDATEONALLG(Object o) {

    }

    static void PROBE(int p) {

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