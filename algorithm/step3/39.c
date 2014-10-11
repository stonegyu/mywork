#include<stdio.h>
int main(){
        int i;
        int a;
        int sum=0;
        scanf("%d",&a);
        for(i=1; i<=a; i++){
                sum+=((2*i-1)*(2*(a-i+1)-1));
        }
        printf("%d\n",sum);
        return 0;
}
