package ex_02;

import java.util.concurrent.locks.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class SushiMonitor_02 {
	

	/* COMPLETE */
	private final static int MAX_CAPACITY = 5;
	private boolean fullGroup = false;
	private int numCustomers = 0;
	private int nfs;
	private boolean first = true;

	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();
	Queue<Customer> waiting = new ArrayDeque<Customer>();
	
	public void enter (int i) {
		/* COMPLETE */
		lock.lock();
		if(nfs == 0) {
			fullGroup = true;
		}
		while(numCustomers == MAX_CAPACITY || fullGroup) {
			System.out.println("----> Entering "+"C("+i+")");
			if (first) {
				System.out.println(" *** Possible group detected. I wait "+"C("+i+")");
				first = false;
			} else {
				System.out.println(" *** I'm told to wait for all free "+"C("+i+")");
			}
			// customer has to WAIT. 
			waiting.add(new Customer(i, this));
		}
		System.out.println(" +++ [free: " + nfs + "] I sit down C("+i+")");
		numCustomers++;
		nfs--;
		lock.unlock();
	}
	
	public void exit (int i) {
		/* COMPLETE */
		lock.lock();
		System.out.println("---> Now leaving [free: "+ nfs + "] "+"C("+i+")");
		if(!waiting.isEmpty()) {
			waiting.remove();
		}
		else {
			numCustomers--;
		}
		lock.unlock();
	}
}
