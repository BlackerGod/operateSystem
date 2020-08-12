package Experiment;

import java.util.LinkedList;
import java.util.Scanner;
public class Test2{
    private int size;                 //内存大小
    private LinkedList<Zone> zones;   //内存分区
    private int pointer;             //上次分配的空闲区位置

    class Zone{                      //分区结点信息类
        private int size;           //分区大小
        private int head;           //分区起始位置
        private boolean isFree;    //空闲状态

        public Zone(int head, int size) {
            this.head = head;
            this.size = size;
            this.isFree = true;
        }
    }
    public Test2(){
        this.size = 400;
        this.pointer = 0;
        this.zones = new LinkedList<>();
        zones.add(new Zone(0, size));
    }
    public Test2(int size) {
        this.size = size;
        this.pointer = 0;
        this.zones = new LinkedList<>();
        zones.add(new Zone(0, size));
    }

    private void doAlloc(int size, int location, Zone tmp) {
        //size 作业大小 location 内存位置 tmp可用空闲区
        Zone newHome = new Zone(tmp.head + size, tmp.size - size);
        //产生一个新空闲区;
        zones.add(location + 1, newHome);
        tmp.size = size;
        tmp.isFree = false;
        System.out.println("成功分配 " + size + "KB 内存!");
    }
    public void FirstFit(int size){
        //将point赋值为零 从头开始找
        for (pointer = 0; pointer < zones.size(); pointer++){
            Zone tmp = zones.get(pointer);
            //找到可用分区（空闲且大小足够）
            if (tmp.isFree && (tmp.size > size)){
                doAlloc(size, pointer, tmp);
                return;
            }
        }
        //遍历结束后未找到可用分区, 则内存分配失败
        System.out.println("无可用内存空间!");
    }
    public void NextFit(int size){
        //直接获得上次point位置，开始遍历分区链表
        Zone tmp = zones.get(pointer);
        if (tmp.isFree && (tmp.size > size)){
            doAlloc(size, pointer, tmp);
            return;//查看当前块是否符合要求
        }
        int len = zones.size();
        int i = (pointer + 1) % len; //循环的过程，
        for (; i != pointer; i = (i+1) % len){
            tmp = zones.get(i);
            //找到可用分区（空闲且大小足够）
            if (tmp.isFree && (tmp.size > size)){
                doAlloc(size, i, tmp);
                return;
            }
        }
        //遍历结束后未找到可用分区, 则内存分配失败
        System.out.println("无可用内存空间!");
    }
    public void BestFit(int size){
        //最佳适应算法
        int flag = -1;//记录位置
        int min = this.size;//初始化为内存长度，用来保存最小碎片大小;
        for (pointer = 0; pointer < zones.size(); pointer++){//从头开始遍历;
            Zone tmp = zones.get(pointer);
            if (tmp.isFree && (tmp.size > size)){//找到合适条件的内存区
                if (min > tmp.size - size){//如果产生的碎片小于上一个最小值，就重新标记最小值位置
                    min = tmp.size - size;
                    flag = pointer;//更新位置
                }
            }
        }
        if (flag == -1){//初始值，没找到
            System.out.println("无可用内存空间!");
        }else {
            doAlloc(size, flag, zones.get(flag));
        }
    }
    public void WorstFit(int size){//最坏适应算法
        int flag = -1;
        int max = 0;//类似于最佳算法
        for (pointer = 0; pointer < zones.size(); pointer++){
            Zone tmp = zones.get(pointer);
            if (tmp.isFree && (tmp.size > size)){
                if (max < tmp.size - size){
                    max = tmp.size - size;
                    flag = pointer;
                }
            }
        }
        if (flag == -1){
            System.out.println("无可用内存空间!");
        }else {
            doAlloc(size, flag, zones.get(flag));
        }
    }
    public void collection(int id){//id 指定要回收的分区号
        if (id >= zones.size()){
            System.out.println("无此分区编号!");
            return;
        }
        Zone tmp = zones.get(id);//找到分区块
        int size = tmp.size;//取出大小
        if (tmp.isFree) {//判断状态
            System.out.println("指定分区未被分配, 无需回收");
            return;
        }

        //检查前面是否空闲，是则合并
        if (id > 0 && zones.get(id - 1).isFree){
            Zone fronKuer = zones.get(id - 1);
            fronKuer.size += tmp.size;
            zones.remove(id);
            id--;
        }

        //检查后面是否空闲，是则合并
        if (id < zones.size() - 1 && zones.get(id + 1).isFree){
            Zone nextKuer = zones.get(id + 1);//取到下一个区块
            tmp.size += nextKuer.size;
            zones.remove(nextKuer);
        }
        zones.get(id).isFree = true;
        System.out.println("内存回收成功!, 本次回收了 " + size + "KB 空间!");
    }
    public void showZones(){//展示内存分区状况
        System.out.println("***************************************");
        System.out.println("*分区编号 分区始址 分区大小 空闲状态  *");
        for (int i = 0; i < zones.size(); i++){
            Zone tmp = zones.get(i);
            System.out.println("*  "+i + "          "
                                   + tmp.head + "      "
                                   + tmp.size + "      "
                                   + tmp.isFree+"    *");
        }
        System.out.println("****************************************");
    }

}