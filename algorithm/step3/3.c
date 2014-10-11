#include<stdio.h>
int main(){
        int a,b;
        scanf("%d %d",&a,&b);
        if(a>b){
                int t=a;
                a=b;
                b=t;
        }
        int i;
        for( i=a; i<=b; i++){
                printf("%d ",i);
        }
        return 0;
}
