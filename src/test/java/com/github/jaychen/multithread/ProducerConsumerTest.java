package com.github.jaychen.multithread;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.opentest4j.TestAbortedException;

class ProducerConsumerTest {
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private final PrintStream ps = new PrintStream(os);
    private final PrintStream systemOut = System.out;


    @BeforeEach
    void setUp() {
        System.setOut(ps);
    }

    @AfterEach
    void cleanUp() {
        System.setOut(systemOut);
    }

    @TestFactory
    public Iterable<DynamicTest> test() throws Exception {
        return IntStream.range(1, 10)
                .mapToObj(this::loadClass)
                .filter(Objects::nonNull)
                .map(this::createOneTest)
                .collect(Collectors.toList());
    }

    private Class<?> loadClass(int i) {
        try {
            return Class.forName("com.github.jaychen.multithread.ProducerConsumer" + i);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private DynamicTest createOneTest(Class<?> testClass) {
        return DynamicTest.dynamicTest(testClass.getSimpleName(), () -> testOne(testClass));
    }

    private void testOne(Class<?> testClass) {
        try {
            // 获取被测试类中的 main 方法
            Method main = testClass.getMethod("main", String[].class);

            // 调用 main 方法，模拟运行被测试类
            main.invoke(null, new Object[]{null});

            // 打印被测试类的输出
            systemOut.println("Output of " + testClass.getName() + ": " + os);

            // 使用正则表达式匹配输出，检查是否符合预期
            /* 解释：
                (?s)：单行模式，使 . 可以匹配换行符。
                .*：匹配任意数量的字符。
                (Producing (-?\\d+)\\s+Consuming \\2\\s+)：捕获组，匹配 Producing 后面的数字并捕获为第一个捕获组，然后匹配空白字符，再匹配 Consuming 后面的数字，并引用第一个捕获组来保证两个数字相同。
                {10}：重复匹配 10 次，即匹配 10 次生产者和消费者输出。
                .*：匹配任意数量的字符。
            * */
            String reg = "(?s).*(Producing (-?\\d+)\\s+Consuming \\2\\s+){10}.*";
            Assertions.assertTrue(os.toString().matches(reg));
        } catch (NoSuchMethodException e) {
            throw new TestAbortedException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
