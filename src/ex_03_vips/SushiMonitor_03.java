package ex_03_vips;

import java.util.concurrent.locks.*;

public class SushiMonitor_03 {
	
	/* COMPLETE */
	private final static int MAX_CAPACITY = 5;
	private volatile boolean fullGroup = false;
	private volatile int numCustomers;
	private volatile int vipsWaiting = 0;
	private volatile int nfs = 5;
	private volatile boolean first = true;
	
	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();
	Condition vipWait = lock.newCondition();
	
	public void enterVIP (int i) {
		/* COMPLETE */
		lock.lock();
		System.out.println("----> ENTERING "+"VIPC("+i+")");
		while (numCustomers == MAX_CAPACITY) {
			vipsWaiting++;
			vipWait.awaitUninterruptibly(); 	
		}
		System.out.println(" +++ [free: " + nfs + "] I sit down VIPC("+i+")");
		vipsWaiting--;
		numCustomers++;
		nfs--;
		lock.unlock();
	}
	
	public void exitVIP (int i) {
		/* COMPLETE */
		lock.lock();
		numCustomers--;
		nfs++;
		System.out.println("---> Now leaving [free: "+ nfs + "] "+"C("+i+")");
		
		vipWait.signal();
		if(vipsWaiting==0) {
			if(nfs==5) fullGroup = false;
			noGroup.signal();
		}
		lock.unlock();		
	}
	
	public void enter (int i) {
		/* COMPLETE */
		lock.lock();
		if(nfs == 0) {
			fullGroup = true;
		}
		System.out.println("----> ENTERING "+"C("+i+")");
		while (numCustomers == MAX_CAPACITY || fullGroup) {
			if (first) {
				System.out.println(" *** Possible group detected. I wait "+"C("+i+")");
				first = false;
			} else {
				System.out.println(" *** I'm told to wait for all free "+"C("+i+")");
			}
			noGroup.awaitUninterruptibly();
		}
		System.out.println(" +++ [free: " + nfs + "] I sit down C("+i+")");
		numCustomers++;
		nfs--;
		lock.unlock();
	}
	
	public void exit (int i) {
		/* COMPLETE */
		lock.lock();
		numCustomers--;
		nfs++;
		System.out.println("---> Now leaving [free: "+ nfs + "] "+"C("+i+")");

		vipWait.signal();
		if(vipsWaiting==0) {
			if(nfs==5) {
				first = true;
				fullGroup = false;
			}
			noGroup.signal();
		}
		lock.unlock();
	}
}
