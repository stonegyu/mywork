#include<stdio.h>
int main(){
        int a;
        scanf("%d",&a);
        int i,cnt=0,sum=0,ssum=1,min=1;
        for(i=1; i<=a; i++){
                if(a%i == 0){
                   cnt++;
                   sum+=i;
                   ssum*=i;
				   ssum%=10;
                   printf("%d ",i);
                }
        }
        printf("\n%d",cnt);
        printf("\n%d",sum);
        printf("\n%d",ssum%10);
        return 0;
}
