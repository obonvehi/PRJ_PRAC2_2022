package ex_02;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SushiMonitor_02 {

	/* COMPLETE */
	private volatile boolean fullGroup = false;
	private volatile int nfs = 5;

	private volatile int nextCustomer = 1;
	private volatile int nowServing = 1;

	private ReentrantLock lock = new ReentrantLock();
	Condition noGroup = lock.newCondition();

	public void enter(int i) {
		/* COMPLETE */
		lock.lock();
		int currentCustomer = nextCustomer;
		nextCustomer++;
		System.out.println("----> Entering " + "C(" + i + ")");
		while (nfs == 0 || fullGroup || currentCustomer != nowServing) {

			if (!fullGroup && nfs == 0) {
				System.out.println(" *** Possible group detected. I wait " + "C(" + i + ")");
				fullGroup = true;

			} else if (fullGroup/* &&currentCustomer==nowServing */) {
				System.out.println(" *** I'm told to wait for all free " + "C(" + i + ")");
			}

			if (!fullGroup)
				noGroup.signal();

			noGroup.awaitUninterruptibly();
		}
		System.out.println(" +++ [free: " + nfs + "] I sit down C(" + i + ")");
		nfs--;
		nowServing++;
		lock.unlock();

	}

	public void exit(int i) {
		/* COMPLETE */
		lock.lock();
		nfs++;
		System.out.println("---> Now leaving [free: " + nfs + "] " + "C(" + i + ")");

		if (nfs == 5) {
			fullGroup = false;
			noGroup.signal();
		}
		lock.unlock();
	}
}
