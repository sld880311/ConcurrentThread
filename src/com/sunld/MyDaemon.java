package com.sunld;
/**
 * @Title: MyDaemon.java
 * @Package com.sunld
 * <p>Description:</p>
 * @author sunld
 * @version V1.0.0 
 * <p>CreateDate:2017年10月9日 下午4:09:31</p>
*/

public class MyDaemon implements Runnable{

	public static void main(String[] args) {
		Thread daemonThread = new Thread(new MyDaemon());
		// 设置为守护进程
        daemonThread.setDaemon(true);
        daemonThread.start();
        System.out.println("isDaemon = " + daemonThread.isDaemon());
        //sleep完成之后,main线程结束，JVM退出!
        try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        //AddShutdownHook方法增加JVM停止时要做处理事件：
        //当JVM退出时，打印JVM Exit语句.
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
        	public void run() {
        		System.out.println("JVM Exit!");
        	}

        });
	}

	@Override
	public void run() {
		for(int i = 0; i < 10;i++) {
			System.out.println(i+"=====MyDaemon=======");
		}
	}
}
