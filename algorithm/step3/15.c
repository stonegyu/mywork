#include<stdio.h>
int sum(int a){
        if(a == 1) return 1;
        return a+sum(a-1);
}
int main(){
        int a;
        scanf("%d",&a);
        printf("%d",sum(a));
        return 0;
}
