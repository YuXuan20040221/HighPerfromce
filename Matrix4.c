#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void block_matrix_multiply(int m, int n, int p, float A[500][700], float B[700][600], float C[500][600], int block_size)
{
    // Initialize result matrix C to 0
    for (int i = 0; i < m; i++)
    {
        for (int j = 0; j < p; j++)
        {
            C[i][j] = 0;
        }
    }

    // Perform block-based multiplication
    for (int ii = 0; ii < m; ii += block_size)
    {
        for (int jj = 0; jj < p; jj += block_size)
        {
            for (int kk = 0; kk < n; kk += block_size)
            {
                // For each block (ii, jj, kk), multiply sub-blocks directly
                for (int i = ii; i < ii + block_size && i < m; i++)
                {
                    for (int j = jj; j < jj + block_size && j < p; j++)
                    {
                        float sum = 0;
                        for (int k = kk; k < kk + block_size && k < n; k++)
                        {
                            sum += A[i][k] * B[k][j];
                        }
                        C[i][j] += sum;
                    }
                }
            }
        }
    }
}

int main()
{
    int m = 500, n = 700, p = 600;
    int block_size = 32;
    clock_t start_t, finish_t;
    // Allocate memory for matrices
    float(*A)[700] = malloc(sizeof(float[500][700]));
    float(*B)[600] = malloc(sizeof(float[700][600]));
    float(*C)[600] = malloc(sizeof(float[500][600]));

    // Initialize matrices A and B with example values
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
    // Perform block matrix multiplication
    block_matrix_multiply(m, n, p, A, B, C, block_size);
    finish_t = clock();

    double total_t = (double)(finish_t - start_t) / CLOCKS_PER_SEC;
    printf("Program 4 Time: %lf seconds\n", total_t);
    fflush(stdout); // 強制輸出

    // Free allocated memory
    free(A);
    free(B);
    free(C);

    return 0;
}
