#include<stdio.h>
int main(){
        int max,min;
        scanf("%d %d",&max, &min);
        int cnt = 0, i;
        for( i=1; i<=max ; i++){
                if(max%i == 0){
                    cnt++;
                    if(cnt == min) break;
                }
        }
        if(cnt == min){
                printf("%d",i);
        }else{
                printf("0");
        }
        return 0;
}
