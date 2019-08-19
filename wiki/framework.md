### 1. 步骤之间的数据传递

testng本身提供了ITestContext这个接口作为测试运行的上下文信息保存地，可以帮助我们将上一个步骤生成的数据保存到测试上下文ITestContext里面，然后下一个步骤可以直接从ITestContext里面直接获取，例如：

```java
public class ContextTest3 {

    @Test
    public void test1(ITestContext context) {
        System.out.println("===test1======");
        context.setAttribute("test1","test1");
    }

    @Test
    public void test2(ITestContext context) {
        System.out.println(context.getAttribute("test1"));
    }
}
```

### 2. 用例之间的数据传递

框架提供了GlobalCacheContext， 测试类之间的数据可以通过GlobalCacheContext放入缓存中，下一条用例需要的时候在从里面获取。

```java
public class GlobalContext extends BaseTestConfiguration {

    @Autowired
    private GlobalCacheContext cacheContext;

    @Test
    public void test() {
        cacheContext.setAttribute("zhangsan","张三是我的偶像");
    }
}
```

```java
public class HelloContext extends BaseTestConfiguration {

    @Autowired
    private GlobalCacheContext cacheContext;
    @Test
    public void test() {
        Object zhangsan = cacheContext.getAttribute("zhangsan");
        PrintUtils.println(zhangsan);
    }
}
```

### 3. 测试步骤的运行顺序

testng对于每个测试的方法提供了@Test的priority属性确定每个测试的方法运行的优先级，框架为了更清晰的标识提供了@Step注解，通过注解的order属性确定运行的顺序和优先级。

```java
public class StepTest extends BaseTestConfiguration {

    @Step(order = 1,description = "第一步")
    @Test
    public void test1() {
        PrintUtils.println("第一步：准备信息");
    }

    @Step(order = 3,description = "第二步")
    @Test
    public void test2() {
        PrintUtils.println("第二步：调用接口");
    }

    @Step(order = 2,description = "第三步")
    @Test
    public void test3() {
        PrintUtils.println("第三步：数据校验");
    }
}

```

### 4. 测试类的运行顺序

由于在实际测试过程中，我们可能期望测试用例有一定的先后顺序方便数据传递，框架定义了@TestCase注解，同时提供的运行器，可以帮助我们有条件的运行想要运行的测试类

```java
@TestCase(order = 1,description = "这是测试类1")
public class HelloTest1 extends BaseTestConfiguration {

    @Test
    public void hello() {
        System.out.println("=======hello1======");
    }
}
```

```java
@TestCase(order = 2,description = "这是测试类2")
public class HelloTest2 extends BaseTestConfiguration {

    @Test
    public void hello() {
        System.out.println("=======hello2======");
    }
}
```

```java
@TestCase(order = 3,description = "这是测试类3")
public class HelloTest3 extends BaseTestConfiguration {

    @Test
    public void hello() {
        System.out.println("=======hello3======");
        throw new RuntimeException("hello3错误");
    }
}
```

测试用例的执行：框架提供了TestNgDiscovery去构建符合条件的测试类，提供了TestNgLauncher去根据条件运行对应的测试类

```java
public class RunTest {

    @Test
    public void test() {

        TestNgDiscovery discovery = TestNgDiscovery.builder()
                .selectPackage("com.hello.service.test.run")
                .build();
        TestNgLauncher launcher = new TestNgLauncher(BaseTestConfiguration.class);
        launcher.launch(discovery);
    }

    @Test
    public void test3() {
        Launcher launcher = new TestNgLauncher(BaseTestConfiguration.class);
        launcher.launch(GlobalContext.class, HelloContext.class);
    }
}

```