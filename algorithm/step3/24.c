#include<stdio.h>
int main(){
        int a,sum=0;
        scanf("%d",&a);
        int i;
        for(i=1; i<a; i++){
                if(a%i==0){
                        sum+=i;
                }
        }
        if(sum < a){
                printf("%5d  DEFICIENT",a);
        }else if(sum > a){
                printf("%5d  ABUNDANT",a);
        }else{
                printf("%5d  PERFECT",a);
        }
        return 0;
}
