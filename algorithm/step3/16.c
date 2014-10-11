#include<stdio.h>
int main(){
        int a;
        int i=0;
        int sum=0;
        scanf ("%d",&a);
        while(sum != a){
                i++;
                sum += i;
        }
        printf("%d",i);
        return 0;
}
