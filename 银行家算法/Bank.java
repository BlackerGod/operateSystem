package Experiment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
class Process{
    String name;          //进程名字
    int allocation[];    //已分配的资源数
    int MaxNeed[];       //最大需求数量
    int needs[];         //仍然需要
    boolean finshined=false;  //状态

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", allocation=" + Arrays.toString(allocation) +
                //", MaxNeed=" + Arrays.toString(MaxNeed) + 可选项，最大需求矩阵不输出
                ", needs=" + Arrays.toString(needs) +
                ", finshined=" + finshined +
                '}';//重写tostring方法，用来输出进程信息
    }
}
public class Bank {

    private static  int KINDS=0;       //资源种类
    private static  int[] resource;   //总资源数
    private static  int ProcessCount;  //进程数量
    private static  List<Process> team;//进程数组
    private static  int[] avaliable;  //当前可分配资源

    public static void InitAllResource(){          //初始化总资源数和进程数量
        System.out.println("请输入资源种类数量:");
        Scanner sc=new Scanner(System.in);
        KINDS=sc.nextInt();
        resource=new int[KINDS]; //资源总类数

        //各资源数量
        for(int i=0;i<resource.length;i++){
            System.out.println("请输入第"+i+"种总资源数");
            resource[i]=sc.nextInt();
        }

    }
    public static void InitProcess(){//初始化进程
        if(KINDS==0){
            System.out.println("请初始化总资源数");
            return;
        }
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入进程的个数");
        ProcessCount=scanner.nextInt();   //初始化进程个数
        team=new ArrayList<>();

        for(int i=0;i<ProcessCount;i++){
            Process newProcess=new Process();//每次创建一个新的进程信息，并对其初始化
            System.out.println("请输入进程的名字：");
            newProcess.name=scanner.next();
            String name=newProcess.name;
            newProcess.MaxNeed=new int[KINDS];
            for(int j=0;j<KINDS;j++){
                System.out.println("请输入"+name+"进程对"+j+"种资源最大需求数量");
                newProcess.MaxNeed[j]=scanner.nextInt();
            }
            newProcess.allocation=new int[KINDS];
            for(int j=0;j<KINDS;j++){
                System.out.println("请输入"+name+"进程对"+j+"种资源已分配数量");
                newProcess.allocation[j]=scanner.nextInt();
            }
            team.add(newProcess);//插入顺序表
        }
        for(int i=0;i<team.size();i++){
            team.get(i).needs=new int[KINDS];
            for(int j=0;j<KINDS;j++){
                team.get(i).needs[j]=team.get(i).MaxNeed[j]-team.get(i).allocation[j];
            }
        }//根据输入的已分配和最大需求，初始化各个进程的仍需

    }
    public static void setAvaliable(){
        avaliable=new int[KINDS];
        for(int i=0;i<KINDS;i++){
            for(int j=0;j<team.size();j++) {
               avaliable[i] +=team.get(j).allocation[i];
            }
        }
        for (int i=0;i<avaliable.length;i++){
            avaliable[i]=resource[i]-avaliable[i];
        }
    }
    public static boolean SafeCheck(){
        if(team == null){
            System.out.println("请初始化进程资源信息!");
            return false;
        }


        for(int i=0;i<KINDS;i++){//初始化乱输出
            if(avaliable[i]<0){
                System.out.println("当前状态错误！请重新初始化！");
                System.out.println(Arrays.toString(avaliable));
                team=null;
                return false;
            }
        }

        String[] safeteam=new String[ProcessCount];//存放安全序列
        int safecount=0;//记录安全序列的序数


        int work[]=new int[KINDS];
        for(int i=0;i<KINDS;i++){
            work[i]=avaliable[i];  //把当前的avaliable数组的值放置到work进行试分配
        }

        int index=0;//循环找进程顺序表的下标
        boolean choose=false;//选择器,看当前状态是否能分配
        int tmp=0;//计算值是否达到了进程长度，即说明查询一圈。

        while (safecount < team.size() && tmp <team.size()){
            //当安全序列有效数小于进程数 或者 查询小于一圈
             if(index >=team.size()){
                index=0;   //防止越界，循环查找
            }
                if(!team.get(index).finshined){//判断当前状态
                    for(int i=0;i<KINDS;i++){//循环比较可用和进程所需,满足置选择器为true
                        if(team.get(index).needs[i]>work[i]){
                            choose=false;
                            tmp++;
                            index++;
                            break;
                        }else {
                            choose=true;
                        }
                    }
                    if(choose){   //找到能分配的，修改work数组，暂时修改状态值
                        for (int j=0;j<KINDS;j++){
                            work[j]=work[j]+team.get(index).allocation[j];
                        }
                        team.get(index).finshined=true;
                        safeteam[safecount]=team.get(index).name;
                        safecount++;
                        index++;
                        tmp=0;//重新计数
                    }
                }else {
                    tmp++;//当前进程已查找，增加查找次数
                    index++;//增加下标值
                }
        }
        for(int i=0;i<safeteam.length;i++){
            if(safeteam[i] == null){//安全队列有一个为空的话，说明不安全，输出阻塞进程信息
                System.out.println("当前状态不安全");
                Printmatrix(work);
                for(int k=0;k<team.size();k++){
                    team.get(k).finshined=false;//把进程状态全部还原
                }
                return false;
                }
            }

        System.out.println("当前状态安全,安全序列为:");
        PrinSafe(safeteam);
        boolean chice=false;
               for(int i=0;i<KINDS;i++){
               if(team.get(index).needs[i] !=0){
                   chice=false;
                   break;
               }else {
                   chice=true;
               }
           }
               if(chice){
                   for(int l=0;l<KINDS;l++){
                       avaliable[l]=team.get(index).allocation[l];
                       team.get(index).allocation[l]=0;
                   }
               }

        for(int k=0;k<team.size();k++){
            team.get(k).finshined=false;//把进程状态全部还原
        }
		return true;
    }
    private static void PrinSafe(String[] Safe){
        //输出安全序列
        for(int i=0;i<Safe.length;i++){
            if(i==0){
                System.out.print("[");
            }
            if(i!=Safe.length-1) {
                System.out.print(Safe[i] + "、");
            }else {
                System.out.print(Safe[i]+"]");
            }
        }
        System.out.println();
        int[] work=new int[KINDS];
        for(int i=0;i<Safe.length;i++){
            for(int j=0;j<team.size();j++){
                if(Safe[i].equals(team.get(j).name)){
                    if(i==0){//第一个的话就是把avaliable+第一个进程的allocation
                        System.out.println("当前可用资源数:"+Arrays.toString(avaliable));
                        for(int k=0;k<KINDS;k++){
                            work[k]=team.get(j).allocation[k]+avaliable[k];
                        }

                        System.out.println(team.get(j));//初始化work的初值
                        System.out.println("当前可用资源数为"+Arrays.toString(work));
                        break;
                    }else{
                        System.out.println(team.get(j));
                        for(int k=0;k<KINDS;k++){
                            work[k]=team.get(j).allocation[k]+work[k];
                        }
                        System.out.println("当前可用资源数为"+Arrays.toString(work));
                        break;
                    }
                }
            }
        }
        System.out.println();


    }
    public static void Printmatrix(){

        //无参数的时候，就是显示当前的进程信息;
        if(team == null){
            System.out.println("请初始化进程资源信息!");
            return ;
        }
        System.out.println("资源总数"+Arrays.toString(resource));
        System.out.println("当前可用资源数"+Arrays.toString(avaliable));
        for(int i=0;i<team.size();i++){
        System.out.println(team.get(i));
    }
    }
    public static void Printmatrix(int[] work){
        //有参数的时候，就是显示当前可用的资源数，和各个进程运行的情况4
        if(team == null){
            System.out.println("请初始化进程资源信息!");
            return ;
        }
        System.out.println("资源总数"+Arrays.toString(work));
        System.out.println("当前可用资源数"+Arrays.toString(avaliable));
        for(int i=0;i<team.size();i++){
            System.out.println(team.get(i));
        }
    }
    public static  void Blank(){
        if(team == null){
            System.out.println("请初始化进程资源信息!");
            return ;
        }
        Scanner scanner=new Scanner(System.in);
            System.out.println("请输入你要分配进程名字");
            String name=scanner.next();
            for(int i=0;i<team.size();i++){
                if(team.get(i).name.equals(name)){

                    int request[]=new int[KINDS];
                    for(int j=0;j<KINDS;j++){
                        System.out.println("请输入要分配的第"+j+"种资源数");
                        request[j]=scanner.nextInt();//保存request的值
                    }
                    for(int j=0;j<KINDS;j++){
                        //是否大于可利用数
                        if(team.get(i).needs[j]<request[j]){
                            System.out.println("错误！请求数量大于进程所需");
                            return;
                        }
                    }
                    for(int j=0;j<KINDS;j++){
                        //是否大于当前可用的资源数
                        if(avaliable[j]<request[j]){
                            System.out.println("错误！请求数量大于可以分配资源");
                            return;
                        }
                    }//前两种都通过
                    TryAllcotion(i,request);//i为进程的ID，request为请求资源数
					return;
                }
            }
            System.out.println("请核对后进程名进行检查");
        }
	private static void TryAllcotion(int i,int[] request){
         for(int j=0;j<KINDS;j++){
             //把这个暂时分配给i进程，修改其need和allocation，修改当前状态可用资源数
		 team.get(i).allocation[j]+=request[j];
		 team.get(i).needs[j]-=request[j];
		 avaliable[j]-=request[j];
        }
		if(!SafeCheck()){//安全性检查,不安全的话，还原刚才分配所得
			System.out.println("状态不安全，未分配");
			for(int j=0;j<KINDS;j++){
			team.get(i).allocation[j]-=request[j];
			team.get(i).needs[j]+=request[j];
			avaliable[j]+=request[j];
			}
			return;
		}
		System.out.println("状态安全，已分配");
	}

    public static void menu(){
        Scanner sc=new Scanner(System.in);
        int choice=1;
        while(choice != 0){
        System.out.println("********************************");
        System.out.println("*     1.初始化资源总数          *");
        System.out.println("*     2.进程资源数量            *");
        System.out.println("*     3.安全性检测              *");
        System.out.println("*     4.银行家算法              *");
        System.out.println("*     5.显示进程状态            *");
        System.out.println("*     0.退出                    *");
        System.out.println("********************************");
        System.out.println("请输入你的选项");
            choice=sc.nextInt();
            if(choice == 0){
                return;
            }else if(choice == 1){
                InitAllResource();//初始化资源总数量和各个资源最大数量
            }else if(choice == 2){
                InitProcess();//初始化各个进程
                setAvaliable();//初始化可分配资源数
            }else if(choice ==3){
                SafeCheck();
            } else if(choice == 4){
				Blank();
			}else if(choice == 5){
                Printmatrix();
            }else {
                System.out.println("输入错误，请检查后重新输入");
            }


        }


    }
    public static void main(String[] args) {
        menu();
    }
}
