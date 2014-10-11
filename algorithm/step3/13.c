#include<stdio.h>
int main(){
        int a;
        scanf("%d",&a);
        while(a!=1){
				printf("%d ",a);
                if(a%2==0) a/=2;
                else a = a*3+1;
                
        }
		printf("1");
        return 0;
}
