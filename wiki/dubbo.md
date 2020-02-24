### 3. dubbo使用

dubbo接口调用需要传的参数有：接口名称（接口全路径、方法名称、参数类型名称全路径与参数值），如果dubbo接口是没有参数的，参数名称全路径和参数值不用传，否则会报错或者找不到服务提供者。dubbo调用有三种方式：同步调用、异步调用、直连调用。样例如下：

```java
public class DubboTest extends BasicTestNGSpringContextTests {

    @Autowired
    DubboService dubboService;
  	//同步调用，dubbo接口是基础数据参数
    @Test
    public void testSyncParam() {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(String.class.getName(),"16323232223");
        List<Map<String, Object>> result = dubboService.invoke("com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService", "queryUserInfoByPhoneNo", paramMap);
        System.out.println(result);
    }

  	//同步调用，dubbo接口是基础数据参数
    @Test
    public void testSyncParam2() {
        DubboRequest dubboRequest = DubboRequest.builder()
                .interfaceName("com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService")
                .methodName("queryUserInfoByPhoneNo")
                .add(String.class.getName(), "16323232223")
                .build();
        DubboResponse dubboResponse = dubboService.invoke(dubboRequest);
        System.out.println(dubboResponse.toString());
    }

  	//同步调用，dubbo接口是一个包装的对象，转成JSONObject
    @Test
    public void testSyncObject() {
        String json = "{\"phone\":\"16323232223\",\"email\":\"jordan@huawei.com\"}";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("com.learn.springboot.springbootssmp.ro.UserRo", JSONObject.parseObject(json));
        List<Map<String, Object>> result = dubboService.invoke("com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService", "getInfo", paramMap);
        System.out.println(result);
    }

  	//同步调用，dubbo接口是一个包装的对象，转成JSONObject
    @Test
    public void testSyncObject2() {
        Map<String, Object> request = Maps.newHashMap();
        request.put("phone","16323232223");
        request.put("email","jordan@huawei.com");
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("com.learn.springboot.springbootssmp.ro.UserRo", request);
        List<Map<String, Object>> result = dubboService.invoke("com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService", "getInfo", paramMap);
        System.out.println(result);
    }

   //异步调用，dubbo接口参数是一个基础数据类型
    @Test
    public void testAsyncParam() {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(String.class.getName(),"15101287330");
        List<Map<String, Object>> result = dubboService.asyncInvoke(
                "com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService",
                "queryUserInfoByPhoneNo",
                paramMap, new ResponseCallback() {
                    @Override
                    public void done(Object o) {
                        System.out.println("==============done=============");
                        System.out.println("==========result========="+o);
                    }

                    @Override
                    public void caught(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        System.out.println(result);
    }

  	//异步调用，dubbo接口参数是一个基础数据类型
    @Test
    public void testAsyncParam1() {
        DubboRequest dubboRequest = DubboRequest.builder()
                .interfaceName("com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService")
                .methodName("queryUserInfoByPhoneNo")
                .add(String.class.getName(), "15101287330")
                .build();
        DubboResponse dubboResponse = dubboService.asyncInvoke(dubboRequest,
            new ResponseCallback() {
            @Override
            public void done(Object o) {
                System.out.println("=======================done=====================");
                System.out.println(o);
            }

            @Override
            public void caught(Throwable throwable) {

            }
        });
        System.out.println(dubboResponse.toString());
    }

  //异步调用，dubbo接口参数是一个包装对象，参数直接传Map
    @Test
    public void testAsyncObject1() {
        Map<String, Object> request = Maps.newHashMap();
        request.put("phone","16323232223");
        request.put("email","jordan@huawei.com");

        DubboRequest dubboRequest = DubboRequest.builder()
                .interfaceName("com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService")
                .methodName("getInfo")
                .add("com.learn.springboot.springbootssmp.ro.UserRo",request)
                .build();
        DubboResponse dubboResponse = dubboService.asyncInvoke(dubboRequest);
        System.out.println(dubboResponse.toString());
    }

  //关于dubbo直连调用需要两步，1-更改dubbo配置文件的配置，2-调用直接方法
  //注册中心协议更改为直连
  dubbo.consumer.register.protocol=direct
  //注册的地址改为：服务提供者的地址
  dubbo.consumer.register.address=127.0.0.1:20889

   @Test
   public void testDirectLinkParam1() {
        DubboRequest dubboRequest = DubboRequest.builder()
                .interfaceName("com.learn.springboot.springbootssmp.dubbo.UserInfoDubboService")
                .methodName("queryUserInfoByPhoneNo")
                .add(String.class.getName(), "16323232223")
                .build();
    	//调用直连服务
        DubboResponse dubboResponse = dubboService.directedInvoke(dubboRequest
            new ResponseCallback() {
            @Override
            public void done(Object o) {
                System.out.println("=======================done=====================");
                System.out.println(o);
            }

            @Override
            public void caught(Throwable throwable) {

            }
        });
        System.out.println(dubboResponse.toString());
    }
}
```
