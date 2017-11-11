package com.sunld;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Title: TestCreateThread.java
 * @Package com.sunld
 * <p>Description:</p>
 * @author sunld
 * @version V1.0.0 
 * <p>CreateDate:2017年9月29日 下午5:03:25</p>
*/

public class TestCreateThread {

	public static void main(String[] args) {
		Thread myThread = new MyThread();
		myThread.setName("myThread");
		myThread.start();
		
		Runnable myRunnable = new MyRunnable();
		Thread myRunnableThread = new Thread(myRunnable);
		myRunnableThread.setName("myRunnableThread");
		myRunnableThread.start();
		
		Thread myRunnableThread2 = new MyThread(myRunnable);
		myRunnableThread2.setName("myRunnableThread2");
		myRunnableThread2.start();
		
		//执行结果参考如下：
		//myThread @@@@ MyThread。run（）我是通过继承Thread实现的多线程
		//myRunnableThread2 @@@@ MyThread。run（）我是通过继承Thread实现的多线程
		//myRunnableThread @@@@ MyRunnable。run（）我是通过实现Runnable接口实现的多线程
		
		//测试callable方法
		// 创建MyCallable对象
		Callable<Integer> myCallable = new MyCallable();    
		//使用FutureTask来包装MyCallable对象
		FutureTask<Integer> ft = new FutureTask<Integer>(myCallable); 
		//FutureTask对象作为Thread对象的target创建新的线程
		Thread thread = new Thread(ft);
		thread.start();//启用
		
		//获取信息
		try {
			//取得新创建的新线程中的call()方法返回的结果
			//当子线程此方法还未执行完毕，ft.get()方法会一直阻塞，
			//直到call()方法执行完毕才能取到返回值。
			int sum = ft.get();
			System.out.println("sum = " + sum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//使用ExecutorService处理多线程
		ExecutorService pool = Executors.newFixedThreadPool(10);  
		Future<Integer> f = pool.submit(myCallable);  
		// 关闭线程池  
		pool.shutdown(); 
		try {
			int sum1 = f.get();
			System.out.println("sum1 = " + sum1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
//方法1通过继承Thread实现
class MyThread extends Thread{

	public MyThread(Runnable myRunnable) {
		super(myRunnable);
	}

	public MyThread() {
		super();
	}

	//需要实现的方法，该方法执行具体的业务逻辑
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()
				+" @@@@ MyThread。run（）我是通过继承Thread实现的多线程");
	}
}
//方法2通过实现runnable接口
//实现Runnable接口，并重写该接口的run()方法，该run()方法同样是线程执行体，创建Runnable实现类的实例，
//并以此实例作为Thread类的target来创建Thread对象，该Thread对象才是真正的线程对象。
class MyRunnable implements Runnable{

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+
				" @@@@ MyRunnable。run（）我是通过实现Runnable接口实现的多线程");
	}
}

//方法3通过Executor框架实现
class MyCallable implements Callable<Integer>{
	//需要实现call方法而不是run方法
	@Override
	public Integer call() throws Exception {
		return 100;
	}
}