#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void copy_block(int block_size, float *src, float *dst, int src_row_stride, int dst_row_stride, int rows, int cols)
{
    // 根據實際的行列數進行拷貝，避免越界
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            dst[i * dst_row_stride + j] = src[i * src_row_stride + j];
        }
    }
}

void block_matrix_multiply_with_copy(int m, int n, int p, float A[500][400], float B[400][600], float C[500][600], int block_size)
{
    // 初始化結果矩陣 C 為 0
    for (int i = 0; i < m; i++)
    {
        for (int j = 0; j < p; j++)
        {
            C[i][j] = 0;
        }
    }

    // 臨時子矩陣
    float *A_block = malloc(block_size * block_size * sizeof(float));
    float *B_block = malloc(block_size * block_size * sizeof(float));
    float *C_block = malloc(block_size * block_size * sizeof(float));

    for (int ii = 0; ii < m; ii += block_size)
    {
        for (int jj = 0; jj < p; jj += block_size)
        {
            int block_rows_c = (ii + block_size > m) ? (m - ii) : block_size;
            int block_cols_c = (jj + block_size > p) ? (p - jj) : block_size;

            // 複製 C 的區塊，考慮行列邊界
            copy_block(block_size, &C[ii][jj], C_block, p, block_size, block_rows_c, block_cols_c);

            for (int kk = 0; kk < n; kk += block_size)
            {
                int block_rows_a = (ii + block_size > m) ? (m - ii) : block_size;
                int block_cols_a = (kk + block_size > n) ? (n - kk) : block_size;
                int block_rows_b = block_cols_a;  // B 的行數與 A 的列數相同
                int block_cols_b = (jj + block_size > p) ? (p - jj) : block_size;

                // 複製 A 和 B 的區塊
                copy_block(block_size, &A[ii][kk], A_block, n, block_size, block_rows_a, block_cols_a);
                copy_block(block_size, &B[kk][jj], B_block, p, block_size, block_rows_b, block_cols_b);

                // 子區塊相乘
                for (int i = 0; i < block_rows_c; i++)
                {
                    for (int j = 0; j < block_cols_c; j++)
                    {
                        float sum = 0;
                        for (int k = 0; k < block_cols_a; k++) // 此處應使用 block_cols_a，因為 A_block 的列數為 block_cols_a
                        {
                            sum += A_block[i * block_size + k] * B_block[k * block_size + j];
                        }
                        C_block[i * block_size + j] += sum;
                    }
                }
            }

            // 將計算後的 C_block 複製回 C 的區塊
            copy_block(block_size, C_block, &C[ii][jj], block_size, p, block_rows_c, block_cols_c);
        }
    }

    // 釋放臨時子矩陣記憶體
    free(A_block);
    free(B_block);
    free(C_block);
}

int main()
{
    int m = 500, n = 400, p = 600;
    int block_size = 32;
    clock_t start_t, finish_t;

    // 分配記憶體給矩陣
    float(*A)[400] = malloc(sizeof(float[500][400]));
    float(*B)[600] = malloc(sizeof(float[400][600]));
    float(*C)[600] = malloc(sizeof(float[500][600]));

    // 初始化矩陣 A 和 B
    for (int i = 0; i < m; i++)
    {
        for (int j = 0; j < n; j++)
        {
            A[i][j] = 1.0f;
        }
    }

    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < p; j++)
        {
            B[i][j] = 1.0f;
        }
    }

    start_t = clock();
    // 進行區塊矩陣乘法（有子矩陣複製）
    block_matrix_multiply_with_copy(m, n, p, A, B, C, block_size);
    finish_t = clock();

    double total_t = (double)(finish_t - start_t) / CLOCKS_PER_SEC;
    printf("Program 5 Time: %lf seconds\n", total_t);
    fflush(stdout);

    // 釋放記憶體
    free(A);
    free(B);
    free(C);

    return 0;
}
