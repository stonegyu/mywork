#include<stdio.h>
int main(){
        int a;
        int max=0,min=999999;
        int i;
        for(i = 0; i<7; i++){
                scanf("%d",&a);
                max = (max<a)?a:max;
                min = (min>a)?a:min;
        }
        printf("%d %d",max,min);
        return 0;
}
