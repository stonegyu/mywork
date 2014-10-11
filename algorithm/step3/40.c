#include<stdio.h>
int main(){
        int a1,a2;
        scanf("%d %d",&a1,&a2);
        int i;
        int max=1;
        for(i=1; i<=a2; i++){
                if(a1%i == 0 && a2%i == 0){
                     max=i;
                }
        }
        printf("%d ",max);
        printf("%d ",max*(a1/max)*(a2/max));
        return 0;
}
