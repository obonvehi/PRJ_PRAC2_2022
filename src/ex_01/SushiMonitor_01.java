package ex_01;

import java.util.concurrent.locks.*;

public class SushiMonitor_01 {

	/* COMPLETE */
	private final static int MAX_CAPACITY = 5;
	private boolean fullGroup = false;
	private int numCustomers;
	private int nfs;

	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();

	public void enter(int i) {
		/* COMPLETE */
		lock.lock();
		while (numCustomers == MAX_CAPACITY || fullGroup) {
			if (nfs == 0) {
				fullGroup = true;
			}
			try {noGroup.await();} catch (InterruptedException e) {}
		}
		numCustomers++;
		nfs--;
		lock.unlock();
	}

	public void exit(int i) {
		/* COMPLETE */
		lock.lock();
		numCustomers--;
		nfs++;

		if(nfs==5)
			fullGroup = false;
			noGroup.signal();
		lock.unlock();
	}
}
