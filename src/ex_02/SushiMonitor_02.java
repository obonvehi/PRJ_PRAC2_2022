package ex_02;

import java.util.concurrent.locks.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class SushiMonitor_02 {
	

	/* COMPLETE */
	private volatile boolean fullGroup = false;
	private volatile int nfs = 5;
	private volatile boolean first = true;
	
	private volatile int nextCustomer = 1;
	private volatile int nowServing = 1;

	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();
	
	public void enter (int i) {
		/* COMPLETE */
		lock.lock();
		System.out.println("----> Entering "+"C("+i+")");
		while (nfs==0 || fullGroup) {
			if (nfs==0&&!fullGroup) {
				System.out.println(" *** Possible group detected. I wait "+"C("+i+")");
				fullGroup = true;
			} else {
				System.out.println(" *** I'm told to wait for all free "+"C("+i+")");
			}
			try {noGroup.await();} catch (InterruptedException e) {}
		}
		
		System.out.println(" +++ [free: " + nfs + "] I sit down C("+i+")");
		nfs--;
		lock.unlock();
	}
	
	public void exit (int i) {
		/* COMPLETE */
		lock.lock();
		nfs++;
		System.out.println("---> Now leaving [free: "+ nfs + "] "+"C("+i+")");

		if(nfs==5) {
			fullGroup = false;
			noGroup.signal();
		}
		lock.unlock();
	}
}
