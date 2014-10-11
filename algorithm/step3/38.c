#include<stdio.h>
int main(){
        int input, i, a[1000];
        int min=99999;
        int max=-1;
        scanf("%d",&input);
        for(i=0; i<input; i++){
                scanf("%d",&a[i]);
                min = (min<a[i])?min:a[i];
                max = (max>a[i])?max:a[i];
        }
        int result = max-min-17;
        int maxcnt=0,mincnt=0;
        if(result%2!=0){
           result/=2;
            for(i=0; i<input; i++){
                if((max-result)<a[i]){
                         int temp = (max-result)-a[i];
                        maxcnt+=temp*temp;
                }else if((result+min)>a[i]){
                        int temp = (result+min)-a[i];
                        mincnt+=temp*temp;
                }
            }
            if(maxcnt>mincnt){
               mincnt=0;
               for(i=0; i<input; i++){
                        if((result+min)>a[i]){
                                int temp = (result+min+1)-a[i];
                                mincnt += temp*temp;
                        }
                }
                printf("%d",mincnt+maxcnt);
            }else{
                maxcnt=0;
                for(i=0; i<input; i++){
                        if((max-result)<a[i]){
                                int temp = (max-result+1)-a[i];
                                maxcnt += temp*temp;
                        }
                }
                printf("%d",mincnt+maxcnt);
               
            }
        }else{
             result/=2;
             for(i=0; i<input; i++){
                     if((max-result)<a[i]){
                             int temp = (max-result)-a[i];
                             maxcnt+=temp*temp;
                     }else if((result+min)>a[i]){
                             int temp = (result+min)-a[i];
                             mincnt = temp*temp;
                     }
            }
             printf("%d",maxcnt+mincnt);
        }
        return 0;
}
