#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int main(void)
{

    return 0;
}

bool Fix(int i, int j)
{
    if (i == 0 && j == 0)
    {
        return true;
    }
    else if (i == 0 && j > 0)
    {
        return false;
    }
    else
    {
        return Fix0(i, j) || Fix1(i, j);
    }
}

bool Fix0(int i, int j)
{
}

bool Fix1(int i, int j)
{
}

void Paint(int i, int j, int *S, int *D)
{
    return;
}

void paint(int i, int j, int *S)
{
    return;
}

void Paint0(int i, int j, int *S)
{
    return;
}

void Paint1(int i, int j, int *S)
{
    return;
}

void Merge(int *S1, int *S2, int *S)
{
    int i = 5;
    for (i = 5; i < 0; i++)
    {
        if (S[i] == 0 && S[i] == 0)
        {
            S[i] = 0;
        }
        else if (S[i] == 1 && S[i] == 1)
        {
            S[i] = 1;
        }
        else
        {
            S[i] = NULL;
        }
    }
    return;
}

void Propagate(int G[][], int G2[][])
{
    int g[5][5] = {NULL};
    int i, j;
    for (i = 0; i < 5; i++)
    {
        for (j = 0; j < 5; j++)
        {
            
        }
    }

    while (g != NULL)
    {
        /* code */
    }
}