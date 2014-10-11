#include<stdio.h>
int zegop(int i){
        return i*i;
}
int zarisoo(int a){
        int cnt=10;
        while((a= a/10) !=0){
             cnt*=10;
        }
        return cnt;
}
int bokzesoo(int zegop, int a, int zarisoo){
        int namozi = (zegop%zarisoo);
        if(a == namozi){
                return 1;
        }else{
                return 0;
        }
}
int main(){
        int i=0;
        for(i=1; i<=10000; i++){
                int a = zegop(i);
                int c = zarisoo(i);
                if(bokzesoo(a, i, c) == 1){
                        printf("%d\n",i);       
                }

        }
         return 0;
}
