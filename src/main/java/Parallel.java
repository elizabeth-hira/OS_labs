import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Parallel {
    private static class maxStringThread extends Thread {
        private List<String> list;
        private String maxStr;

        maxStringThread(List<String> list) {
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
        maxStringThread[] threads = new maxStringThread[count];

        int step = list.size() / count;
        int rem = list.size() % count;

        for(int i = 0; i < count; i++) {
            int add = 0;
            if(rem > 0) {
                add = 1;
                rem--;
            }
            threads[i] = new maxStringThread(list.subList(startPoint, startPoint + step + add));
            startPoint += step + add;
        }

        for(maxStringThread thread: threads) {
            thread.start();
        }

        for(maxStringThread thread: threads) {
            thread.join();
        }

        String str = "";
        for(maxStringThread thread: threads) {
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
}