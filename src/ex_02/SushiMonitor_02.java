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

	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();
	Queue<Customer> waiting = new ArrayDeque<Customer>();
	
	public void enter (int i) {
		/* COMPLETE */
		lock.lock();
		while(numCustomers == MAX_CAPACITY || fullGroup) {
			if (nfs == 0) fullGroup = true;
			// customer has to WAIT. 
			waiting.add(new Customer(i, this));
		}
		numCustomers++;
		nfs--;
		lock.unlock();
	}
	
	public void exit (int i) {
		/* COMPLETE */
		lock.lock();
		if(!waiting.isEmpty()) {
			waiting.remove();
		}
		else {
			numCustomers--;
		}
		lock.unlock();
	}
}
