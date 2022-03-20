package ex_01;

import java.util.concurrent.locks.*;

public class SushiMonitor_01 {

	/* COMPLETE */
	private volatile boolean fullGroup = false;
	private volatile int nfs = 5;
	private volatile boolean first = true;
	
	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();

	public void enter(int i) {
		/* COMPLETE */
		lock.lock();
		if(nfs == 0) {
			fullGroup = true;
		}
		System.out.println("----> Entering "+"C("+i+")");
		while (nfs==0 || fullGroup) {
			if (first) {
				System.out.println(" *** Possible group detected. I wait "+"C("+i+")");
				first = false;
			} else {
				System.out.println(" *** I'm told to wait for all free "+"C("+i+")");
			}
			try {noGroup.await();} catch (InterruptedException e) {}
		}
		
		System.out.println(" +++ [free: " + nfs + "] I sit down C("+i+")");
		nfs--;
		lock.unlock();
	}

	public void exit(int i) {
		/* COMPLETE */
		lock.lock();
		nfs++;
		System.out.println("---> Now leaving [free: "+ nfs + "] "+"C("+i+")");

		if(nfs==5) {
			fullGroup = false;
			first = true;
			noGroup.signal();
		}
		lock.unlock();
	}
}
