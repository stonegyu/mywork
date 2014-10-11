#include<stdio.h>
int main(){
        int i,a,max=0;
        for(i=0; i<7; i++){
                scanf("%d",&a);
                max = (max<a)?a:max;
        }
        printf("%d",max);
        return 0;
}
