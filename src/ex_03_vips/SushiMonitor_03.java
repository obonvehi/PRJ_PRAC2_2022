package ex_03_vips;

import java.util.concurrent.locks.*;

public class SushiMonitor_03 {
	
	/* COMPLETE */
	private volatile boolean fullGroup = false;
	private volatile int nfs = 5;
	private volatile int nextCustomer = 1;
	private volatile int nowServing = 1;
	private volatile int vipsWaiting = 0;
	
	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();
	Condition vipWait = lock.newCondition();
	
	public void enterVIP (int i) {
		/* COMPLETE */
		lock.lock();
		System.out.println("----> Entering " + "VIPC(" + i + ")");
		vipsWaiting++;
		while (nfs == 0) {noGroup.awaitUninterruptibly(); }
		vipsWaiting--;
		System.out.println(" +++ [free: " + nfs + "] I sit down VIPC(" + i + ")");
		nfs--;
		lock.unlock();
		/*
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
		*/
	}
	
	public void exitVIP (int i) {
		/* COMPLETE */
		lock.lock();
		nfs++;
		fullGroup = false;
		System.out.println("---> Now leaving [free: "+nfs+"] " + "VIPC("+ i +")");

		vipWait.signal();
		
		if(vipsWaiting==0) {
			if (nfs == 5) {
				fullGroup = false;
				noGroup.signal();
			}
		}
		lock.unlock();		
		/*
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
		*/	
	}
	
	public void enter (int i) {
		/* COMPLETE */
		lock.lock();
		int currentCustomer = nextCustomer;
		nextCustomer++;
		System.out.println("----> Entering " + "C(" + i + ")");
		while (nfs == 0 || fullGroup || currentCustomer != nowServing) {

			if (!fullGroup && nfs == 0) {
				System.out.println(" *** Possible group detected. I wait " + "C(" + i + ")");
				fullGroup = true;

			} else if (fullGroup) {
				System.out.println(" *** I'm told to wait for all free " + "C(" + i + ")");
			}

			if (!fullGroup&&vipsWaiting==0)
				noGroup.signal();

			noGroup.awaitUninterruptibly();
		}
		System.out.println(" +++ [free: " + nfs + "] I sit down C(" + i + ")");
		nfs--;
		nowServing++;
		lock.unlock();
	}
	
	public void exit (int i) {
		/* COMPLETE */
		
		lock.lock();
		nfs++;
		System.out.println("---> Now leaving [free: " + nfs + "] " + "C(" + i + ")");

		vipWait.signal();
		
		if(vipsWaiting==0) {
			if (nfs == 5) {
				fullGroup = false;
				noGroup.signal();
			}
		}
		lock.unlock();
		/*
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
		*/
	}
}
