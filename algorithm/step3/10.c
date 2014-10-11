#include<stdio.h>
int main(){
        int a;
        int i;
        int min=9999999999999999;
        for(i=0; i<7; i++){
                scanf("%d",&a);
                min=(min>a)?a:min;
        }
        printf("%d",min);
        return 0;
}
