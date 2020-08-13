package Experiment;

import java.util.*;

class process implements Comparable<process>{
    public String Name;//标识符
    public int InTime;//进入内存时间
    public int ServerTime;//服务时间
    public int priority;//优先级
    public int StartTime;//开始时间
    public int FinishTime;//完成时间
    public int TurnaroundTime=0;//周转时间
    public Double AddTurnaroundTime=0.0;//带权周转时间

    @Override
    public int compareTo(process o) {
        return this.InTime - o.InTime;
    }
}
public class Test {
    public static List<process> result=new ArrayList<>();//进程

    public  static void Input(){
        //初始化进程属性
        System.out.println("请输入你要插入进程数");
        Scanner sc=new Scanner(System.in);
        int count=sc.nextInt();
        for(int i=0;i<count;i++){
           process pro=new process();
            System.out.println("请输入标识");
            pro.Name=sc.next();
            System.out.println("请输入进入时间");
            pro.InTime=sc.nextInt();
            System.out.println("请输入服务时间");
            pro.ServerTime=sc.nextInt();
            System.out.println("请输入优先级");
            pro.priority=sc.nextInt();
            result.add(pro);
        }
    }
    public  static void Menu(){
        //菜单
        System.out.println("*************************");
        System.out.println("请输入你想进行的操作:    *");
        System.out.println("1.录入进程属性           *");
        System.out.println("2.按照先来先服务的算法   *");
        System.out.println("3.按照短作业优先的算法   *");
        System.out.println("4.按照高优先级优先算法   *");
        System.out.println("5.按照时间片轮转的算法   *");
        System.out.println("0.退出                  *");
        System.out.println("*************************");
    }
    private static void PrintForm(){
        System.out.println("进程标识符  进入时间  服务时间  开始时间  完成时间  周转时间  带权值周时间");
    }
    public  static void Print(process pro){

        System.out.println(pro.Name+"              "
                +pro.InTime+"         "
                +pro.ServerTime+"         "
                +pro.StartTime+"         "
                +pro.FinishTime+"         "
                +pro.TurnaroundTime+"           "
                +pro.AddTurnaroundTime);
    }
    public  static void FCFS(){
        //先来先服务
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
         Queue<process> queue=new LinkedList<>();//内存队列
        Collections.sort(result);
        for(process newProcess:result){
            queue.offer(newProcess);
        }
        process tmp=queue.peek();//第一个输出的值
        tmp.StartTime=tmp.InTime;
        tmp.FinishTime=tmp.ServerTime+tmp.StartTime;
        tmp.TurnaroundTime=tmp.FinishTime-tmp.InTime;
        tmp.AddTurnaroundTime=(double)tmp.TurnaroundTime/(double) tmp.ServerTime;
        PrintForm();
        Print(tmp);
        while (!queue.isEmpty()){
            process tmpe=queue.poll();
            if(queue.isEmpty()){
                break;
            }
            process top=queue.peek();
            if(tmpe.FinishTime>top.InTime){
            top.StartTime=tmpe.FinishTime;
            } else{
                top.StartTime=top.InTime;
            }
            top.FinishTime=top.StartTime+top.ServerTime;
            top.TurnaroundTime=top.FinishTime-top.InTime;
            top.AddTurnaroundTime=(double)top.TurnaroundTime/(double) top.ServerTime;
            Print(top);
        }
        AverNum();
    }
    public  static void SpfSort(int start, int end){ //根据服务时间排序
        for(int i = start; i < end; i++){
            for(int j = i + 1; j < end; j++){
                if(result.get(i).ServerTime> result.get(j).ServerTime){
                    process tem = result.get(i);
                    result.set(i,result.get(j));
                    result.set(j, tem);
                }
            }
        }
    }
    public  static void HpfSort(int start, int end, int temover){//根据优先级排序
        for(int i = start; i < end; i++){
            for(int j = i + 1; j < end; j++){
                int now =  (temover - result.get(i).InTime + result.get(i).ServerTime) / result.get(i).ServerTime;
                int next = (temover - result.get(j).InTime + result.get(j).ServerTime) / result.get(j).ServerTime;
                if(next > now){//值越大优先
                    process tem = result.get(i);
                    result.set(i,result.get(j));
                    result.set(j, tem);
                }
            }
        }
    }
    public  static int effective(int temover, int start){ //查看短进程有效值
        int end;  //去返回有效值的下标
        for(end = start; end < result.size(); end++){
            if(result.get(end).InTime > temover){
                break;
            }
        }
        return end;
    }
    public  static void AverNum(){
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
        int Turnsum=0;
        double AddTurnsum=0.0;
        for(int i=0;i<result.size();i++){
            Turnsum+=result.get(i).TurnaroundTime;
            AddTurnsum+=result.get(i).AddTurnaroundTime;
        }
        double AverTurn=(double) Turnsum/result.size();
        double AverAddTurn=AddTurnsum/result.size();
        System.out.println("平均周转时间为:"+AverTurn);
        System.out.println("加权平均周转时间为:"+AverAddTurn);
    }
    public  static void SPF(){
        //短进程优先
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
        Collections.sort(result);
        int temover = result.get(0).InTime;
        PrintForm();
        for (int i = 0; i < result.size(); i++) {

                int effect = effective(temover,i);  //查看进来了几个进程
                SpfSort(i,effect); //对进来的进程对短进程排序
                temover=InitOther(i,temover);

        }
        AverNum();//计算周转时间和带权周转时间
    }
    public  static void HPF() {
        //高优先级优先
        if (result == null) {
            throw new RuntimeException("请输入进程");
        }
        Collections.sort(result);
        PrintForm();
        int temover = result.get(0).InTime;
        for (int i = 0; i < result.size(); i++) {
                int effect = effective(temover, i);  //查看进来了几个进程
                HpfSort(i, effect, temover); //对进来的进程进行优先级排序
                temover=InitOther(i,temover);

        }
        AverNum();  //计算周转时间和带权周转时间
    }
    public  static void RR(){
        //时间片轮转
        if(result == null){
            throw new RuntimeException("请输入进程");
        }
        Collections.sort(result);

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输出系统时间片:");
        int RR = scanner.nextInt();
            PrintForm();
        Queue<Integer> queue1=new LinkedList<>();
        int timeover = result.get(0).InTime;  //记录上个进程结束时间
        int[] serviceTem = new int[result.size()]; //存放所有的进程估计运行时间的,开始全为0.
        int i = 1; //看队列进了几个进程了
        queue1.offer(0); //排完序,肯定先执行第一个.

        while(!queue1.isEmpty() || i < result.size()){
            int cur = RR;
            if(queue1.isEmpty()) {
                for (int tep = 0; tep < result.size(); tep++) {
                    if (serviceTem[tep] == 0) {
                        queue1.offer(tep);
                        i = i + 1;
                        timeover=result.get(tep).InTime;
                        break;
                    }
                }
            }
            //出队,进行执行
            int tem = queue1.poll();
            if(serviceTem[tem] == 0){  //当数组里估计运行时间为0的话,那就是第一次初始化,可以赋一下初始值.
                result.get(tem).StartTime = timeover;
            }
            while(cur != 0){   //模拟实现加时间片轮转,执行RR次,直到相等或用完。
                if(serviceTem[tem] != result.get(tem).ServerTime){
                    ++serviceTem[tem];
                    timeover++;
                }
                if(serviceTem[tem] == result.get(tem).ServerTime) {
                    result.get(tem).FinishTime =timeover;
                    result.get(tem).TurnaroundTime=result.get(tem).FinishTime-result.get(tem).InTime;
                    result.get(tem).AddTurnaroundTime=(double)result.get(tem).TurnaroundTime/result.get(tem).ServerTime;
                    Print(result.get(tem));
                    break;
                }
                cur--;
            }
            //i记录进程个数,去遍历所有进程,看还有那个没进入,如果进程到了,就插入队列.
            if(i < result.size()) {
                int j = i;
                for (; j < result.size(); j++) {
                    if (result.get(j).InTime <= timeover) {
                        queue1.offer(j);
                        i = i + 1;
                    }
                }
            }
            //如果当前进程没有执行完,就在进入队列.
            if(serviceTem[tem] != result.get(tem).ServerTime) {
                queue1.offer(tem);
            }
        }
        AverNum();
    }
    public  static int InitOther(int i,int temover){
    if (temover >= result.get(i).InTime) {
        result.get(i).StartTime = temover;
        result.get(i).FinishTime = result.get(i).StartTime + result.get(i).ServerTime;
        temover = result.get(i).FinishTime;
        result.get(i).TurnaroundTime=result.get(i).FinishTime-result.get(i).InTime;
        result.get(i).AddTurnaroundTime=(double)result.get(i).TurnaroundTime/result.get(i).ServerTime;
        Print(result.get(i));
        return temover;
    } else {
        result.get(i).StartTime = result.get(i).InTime;
        result.get(i).FinishTime = result.get(i).StartTime + result.get(i).ServerTime;
        temover = result.get(i).FinishTime; //记住他的结束时间.
        result.get(i).TurnaroundTime=result.get(i).FinishTime-result.get(i).InTime;
        result.get(i).AddTurnaroundTime=(double)result.get(i).TurnaroundTime/result.get(i).ServerTime;
        Print(result.get(i));
        return  temover;
    }

}
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        int choose=1;
        while(choose !=0){
            Menu();
            choose=scan.nextInt();
            if(choose == 0){
                return;
            } else if(choose==1){
               Input();
            } else if(choose == 2){
                FCFS();
            } else if(choose == 3){
               SPF();
            } else if(choose == 4){
                HPF();
            } else if(choose == 5){
                RR();
            } else{
                System.out.println("输入错误");
                continue;
            }

        }
    }
}
