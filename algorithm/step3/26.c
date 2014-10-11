#include<stdio.h>
int main(){
        int a, i, sum=0;
        scanf("%d",&a);
        for(i=1; i<a; i++){
                sum+=i;
        }
        int ssum=0;
        for(i=(a+1); ssum<sum; i++){
                ssum+=i;
        }
        if(sum== ssum) printf("O");
        else printf("X");
        return 0;
}
