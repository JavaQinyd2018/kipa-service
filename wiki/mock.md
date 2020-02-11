### 4. mock使用

框架提供的mock功能只支持mock http请求，需要传入http请求的相关信息和http响应的相关信息，会在本地分配出来一个端口mock服务，默认是6231。当然，端口可以自己在mockserver.properties进行配置。

```java
public class MockTest extends BasicTestNGSpringContextTests {

    @Autowired
    private MockService mockService;

  	/**
  	* 通过文件构造mock请求和响应对象，框架提供了CSVutils工具类，可以构造对象
  	* @BeforeMethod 这个注解是为了在@Test方法之前运行mock操作，方便在@Test方法里面http调用的时候
  	* 返回mock的结果
  	*/
    @BeforeMethod
    public void before() {
        MockParamRequest request = CSVUtils.convertVertical2Bean("mock/data/mockParamRequest.csv", MockParamRequest.class, 0);
        MockResponse response = CSVUtils.convertVertical2Bean("mock/data/mockResponse.csv", MockResponse.class, 0);
      //通过mock服务进行mock操作
        mockService.mockResponse(request, response);
    }

    @Autowired
    private HttpService httpService;

    @Test
    public void testMock() {
       	//http 调用，返回结果是mock的结果
        String s = httpService.get("http:127.0.0.1:1234/userInfo/showUserInfo?phone=2355464646, true);
        System.out.println(s);
    }

    =====================================================================================
    //直接通过builder手动构造mock的参数进行mock http请求
   	@BeforeMethod
    public void before() {

        MockParamRequest mockParamRequest = MockParamRequest.builder()
                .path("/hello")
                .method("get")
                .addHeader("accept", "application/json;charset=utf-8")
                .addRequestParam("username", "kobe")
                .build();
        MockResponse mockResponse = MockResponse.builder()
                .statusCode(200)
                .addHeader("accept", "application/json;charset=utf-8")
                .body("{\"phone\":\"16323232223\",\"created\":\"1234-09-14\",\"id\":1,\"username\":\"kobe\"}")
                .build();
        mockService.mockResponse(mockParamRequest, mockResponse);
    }

    @Autowired
    private HttpService httpService;

    @Test
    public void test() {
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("accept","application/json;charset=utf-8");
        String s = httpService.get("http://127.0.0.1:6231/hello?username=kobe",headerMap, true);
        System.out.println(s);
    }

     ==================================================================================
     //框架定义了@MockHttp注解帮助我们进行mock时候的数据驱动，具体用法如下：
    public class DataHelloTest extends BasicTestNGSpringContextTests {

    @Autowired
    private MockService mockService;

    @Autowired
    private HttpService httpService;

  //用MockHttp标识的方法会在@Test注解标识的方法之前运行，达到mock的作用
    @MockHttp
    @DataMeta({
            @DataParam(paramName = "requestCsvFile",paramValue = "mock/data/mockParamRequest.csv"),
            @DataParam(paramName = "responseCsvFile",paramValue = "mock/data/mockResponse.csv")
    })
    public void mock(String requestCsvFile, String responseCsvFile) {
      //构建参数MockParamRequest
        MockParamRequest request = CSVUtils.convertVertical2Bean(requestCsvFile,  MockParamRequest.class, 0);
      //构建参数：MockResponse
        MockResponse response = CSVUtils.convertVertical2Bean(responseCsvFile, MockResponse.class, 0);
      //进行mock
        mockService.mockResponse(request, response);
    }

    @Test(dataProvider = "csv")
    public void test(String url,String path,String name, String value) {
        System.out.println("=================测试=================");
      //调用http接口，会返回mock的数据
        String s = httpService.get(String.format("%s%s?%s=%s",url, path, name, value), true);
        System.out.println(s);
    }

```
