import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Parallel {
    private static class MyThread extends Thread {
        private List<String> list;
        private String maxStr;

        MyThread(List<String> list) {
            this.list = list;
            maxStr = "";
        }

        @Override
        public void run() {
            for (String str: list) {
                if(maxStr.length() < str.length())
                    maxStr = str;
            }
        }

        public String getMaxStr() {
            return maxStr;
        }
    }

    static String maxParallel(int count, List<String> list) throws InterruptedException {
        int startPoint = 0;
        MyThread[] threads = new MyThread[count];

        int step = list.size() / count;
        int rem = list.size() % count;

        for(int i = 0; i < count; i++) {
            int add = 0;
            if(rem > 0) {
                add = 1;
                rem--;
            }
            threads[i] = new MyThread(list.subList(startPoint, startPoint + step + add));
            startPoint += step + add;
        }

        for(MyThread thread: threads) {
            thread.start();
        }

        for(MyThread thread: threads) {
            thread.join();
        }

        String str = "";
        for(MyThread thread: threads) {
            if(str.length() < thread.getMaxStr().length())
                str = thread.getMaxStr();
        }

        return str;
    }

    static String maxParallelStream(int count, List<String> list) throws InterruptedException, ExecutionException {
        ForkJoinPool customThreadPool = new ForkJoinPool(count);

        return customThreadPool.submit(() -> list.parallelStream()
                .max(Comparator.comparingInt(String::length))).get().get();
    }

    static void bigDataParallel(int count, List<String> list) throws InterruptedException, ExecutionException {
        long start, res;
        int i = count;

        start = System.currentTimeMillis();
        Parallel.maxParallel(i, list);
        res = System.currentTimeMillis() - start;

        System.out.printf("Threads count: %d\nmaxParallel function: %dms\n", i, res);

        start = System.currentTimeMillis();
        Parallel.maxParallelStream(i, list);
        res = System.currentTimeMillis() - start;

        System.out.printf("maxParallelStream function: %dms\n\n", res);
    }
}