import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParallelTest {

    @Test
    void maxParallel() throws InterruptedException {
        String result = Parallel.maxParallel(5, Arrays.asList("s", "ssssss", "sssss", "ssssssssssss", "ss"));
        assertEquals(result, "ssssssssssss");
    }

    @Test
    void maxParallelStream() throws ExecutionException, InterruptedException {
        String result = Parallel.maxParallelStream(5, Arrays.asList("s", "ssssss", "sssss", "ssssssssssss", "ss"));
        assertEquals(result, "ssssssssssss");
    }

    @Test
    void bigDataParallel1Thread() throws ExecutionException, InterruptedException {
        List<String> list = getList(100000);

        bigDataParallel(1, list);
    }

    @Test
    void bigDataParallel5Threads() throws ExecutionException, InterruptedException {
        List<String> list = getList(100000);

        bigDataParallel(5, list);
    }

    @Test
    void bigDataParallel10Threads() throws ExecutionException, InterruptedException {
        List<String> list = getList(100000);

        bigDataParallel(10, list);
    }

    public static List<String> getList(int size){
        Random r = new Random();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int length = r.nextInt(10000);
            StringBuffer sb = new StringBuffer();

            while(sb.length() < length){
                sb.append(Integer.toHexString(r.nextInt(10000)));
            }

            list.add(sb.toString().substring(0, length));
        }

        return list;
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