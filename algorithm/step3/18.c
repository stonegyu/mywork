#include<stdio.h>
int main(){
        double a;
        scanf("%d",&a);
        int i=1;
        while(i!=a+1){
                if(a%i==0) printf("%d\n",i);
                i++;
        }
        return 0;
}
