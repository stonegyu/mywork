#include<stdio.h>
int main(){
        int a;
        scanf("%d",&a);
        int i;
        int sum=0;
        for(i=1; i<a; i++){
                if(a%i==0){
                   sum+=i;     
                }
        }
        if(sum == a){
                printf("yes");
                return 0;
        }
        printf("no");
        return 0;
}
