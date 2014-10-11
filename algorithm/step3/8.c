#include<stdio.h>

int main(){
        int a;
        scanf("%d",&a);
        int i,sum=0;
        for(i = 1; i<=a; i++){
           if(i!=a) printf("%d+",i);
           sum+=i;
        }
        printf("%d=%d",a,sum);
        return 0;
}
