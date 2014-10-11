#include<stdio.h>
int main(){
        int min=999999, a, i,sum=0;
        for(i=0; i<7; i++){
                scanf("%d",&a);
                if(a%2==1){
                   sum+=a;
                   min=(min>a)?a:min;
                }
        }
        if(sum == 0) printf("-1");
        else printf("%d\n%d",sum,min);
        return 0;
}
