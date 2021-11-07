import java.util.concurrent.locks.ReentrantLock;

public class main {
    static int QSIZE = 10;
    public static class DoubleLockBasedQueue {
        int head = 0, tail = 0;
        int[] items = new int[QSIZE];
        ReentrantLock enqlock = new ReentrantLock();
        ReentrantLock deqlock = new ReentrantLock();
        DoubleLockBasedQueue() {
            for (int i = 0; i < QSIZE; ++i){
                items[i] = -1;
            }
        }
        public void enq(int x) {
            while (tail - head == QSIZE) {
            }
            enqlock.lock();
            try {
                items[tail % QSIZE] = x;
                tail++;
            } finally {
                enqlock.unlock();
            }
        }

        public int deq(int threadId) {

            while (tail == head) {
            }
            deqlock.lock();
            if(tail == head){
                System.out.println("Thread [" + threadId + "] has locked the q for dequeue despite it being empty. head=" + head + " tail="+tail);
            }
            try {
                int item = items[head % QSIZE];
                items[head % QSIZE] = -1; //debug
                head++;
                if(item == -1){
                    System.out.println("\titem == -1; head=" + head + " tail="+tail);
                }
                return item;
            } finally {
                deqlock.unlock();
            }
        }
    }

    public static class ProducingTread implements Runnable{
        DoubleLockBasedQueue qRef;
        static int threadCount = 0;
        int threadId;
        ProducingTread(DoubleLockBasedQueue qRef){
            this.qRef = qRef;
            threadId = ProducingTread.threadCount++;
        }
        @Override
        public void run() {
            for(int i = 0; i < 1000; ++i){
                qRef.enq(10);
            }
        }
    }

    public static class ConsumingThread implements Runnable{
        DoubleLockBasedQueue qRef;
        static int threadCount = 0;
        int threadId;
        ConsumingThread(DoubleLockBasedQueue qRef){
            this.qRef = qRef;
            ConsumingThread.threadCount++;
            threadId = ProducingTread.threadCount++;
        }
        @Override
        public void run() {
            for(int i = 0; i < 1000; ++i) {
                var res = qRef.deq(threadId);
                if(res != 10)
                    System.out.println("thread [" + ConsumingThread.threadCount + "] got " + res + " from queue");
            }
        }
    }

    public static void main(String[] args) {
        var q = new DoubleLockBasedQueue();
        var consumer1 = new Thread(new ConsumingThread(q));
        var consumer2 = new Thread(new ConsumingThread(q));
        var consumer3 = new Thread(new ConsumingThread(q));


        var producer1 = new Thread(new ProducingTread(q));
        var producer2 = new Thread(new ProducingTread(q));
        var producer3 = new Thread(new ProducingTread(q));

        consumer1.start();
        consumer2.start();
        consumer3.start();


        producer1.start();
        producer2.start();
        producer3.start();

    }
}