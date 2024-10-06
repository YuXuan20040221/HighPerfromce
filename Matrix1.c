#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(void)
{
    clock_t start_t, finish_t;
    int m = 500, p = 600, n = 700;

    // 使用動態記憶體分配
    int **A = (int **)malloc(m * sizeof(int *));
    int **B = (int **)malloc(n * sizeof(int *));
    int **C = (int **)malloc(m * sizeof(int *));
    
    for (int i = 0; i < m; i++) {
        A[i] = (int *)malloc(n * sizeof(int));
        C[i] = (int *)malloc(p * sizeof(int));
    }
    for (int i = 0; i < n; i++) {
        B[i] = (int *)malloc(p * sizeof(int));
    }

    // 初始化 A 和 B
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            A[i][j] = 1;
        }
    }

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < p; j++) {
            B[i][j] = 1;
        }
    }

    // 計算時間
    start_t = clock();
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < p; j++) {
            C[i][j] = 0; // 初始化 C[i][j]
            for (int k = 0; k < n; k++) {
                C[i][j] += A[i][k] * B[k][j]; // 矩陣乘法
            }
        }
    }
    finish_t = clock();

    // 輸出時間並強制刷新緩衝區
    double total_t = (double)(finish_t - start_t) / CLOCKS_PER_SEC;
    printf("Program 1 Time: %lf seconds\n", total_t);
    fflush(stdout); // 強制輸出

    // 釋放動態記憶體
    for (int i = 0; i < m; i++) {
        free(A[i]);
        free(C[i]);
    }
    for (int i = 0; i < n; i++) {
        free(B[i]);
    }
    free(A);
    free(B);
    free(C);

    return 0;
}
