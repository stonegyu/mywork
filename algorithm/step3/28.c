#include<stdio.h>
int main(){
        int x,m,i,flag=0;
        scanf("%d %d",&x,&m);
        for(i=1; i<100; i++){
                if((m*i+1)%x == 0){
                        flag=1;
                        break;
                }
        }
        if(flag==1) printf("%d",(m*i+1)/x);
        else printf("No such integer exists.");
        return 0;
}
