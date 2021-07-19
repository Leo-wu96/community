package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class ThreadPoolTest {
    @Test
    public void test(){
        final ThreadPoolExecutor pool = new ThreadPoolExecutor(2,3,60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(5),
                Executors.defaultThreadFactory());
        for(int i=0;i<9;i++){
            pool.execute(new Thread());
        }
        pool.shutdown();

    }
}
