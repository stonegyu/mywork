#include<stdio.h>
int main(){
        int a;
        scanf("%d",&a);
        int i,sum=0;
        for(i=1; i<=a; i++){
                sum += i * (i+1)*(i+2)*0.5;
        }
        printf("%d",sum);
        return 0;
}
