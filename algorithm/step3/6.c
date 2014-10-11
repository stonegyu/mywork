#include<stdio.h>
int main(){
        int a;
        scanf("%d",&a);
        int i,j;
        for( i=1; i<=a; i++){
           printf("1..%d ",i);
           int sum=0;
           for(j=1; j<=i; j++){
                sum+=j;
           }
           printf("%d\n",sum);
        }
      return 0;
}
