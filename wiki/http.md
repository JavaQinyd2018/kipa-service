### 2. http使用

http的一些基本概念

```tex
HTTP请求报文格式:
HTTP请求报文主要由请求行、请求头部、请求正文3部分组成

请求行：由请求方法，URL，协议版本三部分构成，之间用空格隔开
请求方法包括：POST、GET、HEAD、PUT、POST、TRACE、OPTIONS、DELETE等
协议版本：HTTP/主版本号.次版本号，常用的有HTTP/1.0和HTTP/1.1

请求头部:
请求头部为请求报文添加了一些附加信息，由“名/值”对组成，每行一对，名和值之间使用冒号分隔
常见请求头如下：
Host ----接受请求的服务器地址，可以是IP:端口号，也可以是域名
User-Agent ----发送请求的应用程序名称
Connection ---- 指定与连接相关的属性，如Connection:Keep-Alive
Accept-Charset ---- 通知服务端可以发送的编码格式
Accept-Encoding ---- 通知服务端可以发送的数据压缩格式
Accept-Language ---- 通知服务端可以发送的语言

状态行：
由3部分组成，分别为：协议版本，状态码，状态码描述，之间由空格分隔
状态码:为3位数字，200-299的状态码表示成功，300-399的状态码指资源重定向，400-499的状态码指客户端请求出错，500-599的状态码指服务端出错（HTTP/1.1向协议中引入了信息性状态码，范围为100-199)
常见的：
200：响应成功
302：重定向跳转，跳转地址通过响应头中的Location属性指定
400：客户端请求有语法错误，参数错误，不能被服务器识别
403：服务器接收到请求，但是拒绝提供服务（认证失败）
404：请求资源不存在
500：服务器内部错误

响应头部 :
与请求头部类似，为响应报文添加了一些附加信息
Server - 服务器应用程序软件的名称和版本
Content-Type - 响应正文的类型（是图片还是二进制字符串）
Content-Length - 响应正文长度
Content-Charset - 响应正文使用的编码
Content-Encoding - 响应正文使用的数据压缩格式
Content-Language - 响应正文使用的语言

集中常用的MediaType:
MediaType Image = MediaType.parse("image/jpeg; charset=utf-8");
MediaType MEDIA_TYPE_TEXT = MediaType.parse("text; charset=utf-8");
MediaType JSON = MediaType.parse("application/json; charset=utf-8");
常见的媒体格式类型如下：

    text/html ： HTML格式
    text/plain ：纯文本格式      
    text/xml ：  XML格式
    image/gif ：gif图片格式    
    image/jpeg ：jpg图片格式 
    image/png：png图片格式
   以application开头的媒体格式类型：

   application/xhtml+xml ：XHTML格式
   application/xml     ： XML数据格式
   application/atom+xml  ：Atom XML聚合格式    
   application/json    ： JSON数据格式
   application/pdf       ：pdf格式  
   application/msword  ： Word文档格式
   application/octet-stream ： 二进制流数据（如常见的文件下载）
   application/x-www-form-urlencoded ： <form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）
   另外一种常见的媒体格式是上传文件之时使用的：

    multipart/form-data ： 需要在表单中进行文件上传时，就需要使用该格式
    以上就是我们在日常的开发中，经常会用到的若干content-type的内容格式。
```

关于http的操作，框架提供了http和https两种类型的的操作，如果需要使用https需要在http.properties的配置文件中配置相关的安全证书信息

http提供了httpService和HttpsService两个服务类，可以帮助我们发起http请求，目前支持get、post、put、delete、以及文件的上传和下载等功能，能够满足测试需要，支持同步调用和异步调用，如下是同步调用的样例：

```java
public class HttpTest extends BaseTestConfiguration {

 	//注入http服务
    @Autowired
    private HttpService httpService;

  	/**
  	* get请求直接传url进行调用，返回的结果是一个json或者其他报文格式
  	*/
    @Test
    public void testGet() {
        String s = httpService.get("http://localhost:8989/user/findById?id=1");
        System.out.println(s);
    }

  	/**
  	* get请求直接传url进行调用，同时开启获取所有返回的信息，返回的结果是一个json或者其他报文格式
  	* 返回的有 head、cookie，结果状态码、错误信息等等所有的信息
  	*/
    @Test
    public void testGet1() {
        String s = httpService.get("http://localhost:8989/user/findById?id=1",true);
        System.out.println(s);
    }
  	/**
  	* get请求直接传url进行调用，同时开启获取所有返回的信息，将请求的参数放在一个map
  	* 返回的有 head、cookie，结果状态码、错误信息等等所有的信息
  	*/
    @Test
    public void testGet2() {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Accept","application/json");

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("id","1");
        List<Map<String, Object>> list = httpService.get("http://localhost:8989/user/findById", headMap, paramMap, true);
        System.out.println(list);
    }

    @Test
    public void testGet3() {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Accept","application/json");
        String json = httpService.get("http://localhost:8989/user/findById?id=1", headMap, true);
        System.out.println(json);
    }

   /**
  	* post请求直接传url进行调用，接口请求是json格式的，返回的结果是一个json或者其他报文格式
  	*/
    @Test
    public void testPost() {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("username","spring");
        paramMap.put("password","123456");
        paramMap.put("password2","123456");
        paramMap.put("email","spring@123.com");
        paramMap.put("phone","123456789");
        String json = JSON.toJSONString(paramMap);
        String result = httpService.post("http://localhost:8989/user/save", json);
        System.out.println(result);
    }

  	/**
  	* post请求直接传url进行调用，接口请求是json格式的，返回的结果是一个json或者其他报文格式
  	* 返回的有 head、cookie，结果状态码、错误信息等等所有的信息
  	*/
    @Test
    public void testPost1() {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Accept","application/json");
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("username","spring");
        paramMap.put("password","123456");
        paramMap.put("password2","123456");
        paramMap.put("email","spring@123.com");
        paramMap.put("phone","123456789");
        String json = JSON.toJSONString(paramMap);
        List<Map<String, Object>> result = httpService.post("http://localhost:8989/user/save", headMap, json, true);
        System.out.println(result);
    }

    @Test
    public void testPost2() {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("username","spring");
        paramMap.put("password","123456");
        paramMap.put("password2","123456");
        paramMap.put("email","spring@123.com");
        paramMap.put("phone","123456789");
        String json = JSON.toJSONString(paramMap);
        String result = httpService.post("http://localhost:8989/user/save", json);
        System.out.println(result);
    }

    /**
  	* post请求直接传url进行调用，接口请求键值对的参数，返回的结果是一个json或者其他报文格式
  	* 返回的有 head、cookie，结果状态码、错误信息等等所有的信息
  	*/
    @Test
    public void testPost3() {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("username","spring");
        paramMap.put("password","123456");
        paramMap.put("password2","123456");
        paramMap.put("email","spring@123.com");
        paramMap.put("phone","123456789");
        String result = httpService.post("http://localhost:8989/user/save", paramMap);
        System.out.println(result);
    }

    /**
  	* post请求直接传url进行调用，传入head等信息，接口请求键值对的参数，
  	* 返回的结果是一个json或者其他报文格式
  	* 返回的有 head、cookie，结果状态码、错误信息等等所有的信息
  	*/
    @Test
    public void testPost4() {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Accept","application/json");
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("username","spring");
        paramMap.put("password","123456");
        paramMap.put("password2","123456");
        paramMap.put("email","spring@123.com");
        paramMap.put("phone","123456789");
        List<Map<String, Object>> result = httpService.post("http://localhost:8989/user/save", headMap, paramMap, true);
        System.out.println(result);
    }

    /**
  	* post请求直接传url进行调用，传入head等信息，接口请求键值对的参数，
  	* 返回的结果是一个json或者其他报文格式
  	* 将返回的结果转化成一个实体类User
  	*/
    @Test
    public void testPost5() {
        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Accept","application/json");
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("username","spring");
        paramMap.put("password","123456");
        paramMap.put("password2","123456");
        paramMap.put("email","spring@123.com");
        paramMap.put("phone","123456789");
        List<User> result = httpService.post("http://localhost:8989/user/save", headMap, paramMap,User.class, true);
        System.out.println(result);
    }

    	@Test
        public void testPut() {
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("Accept","application/json");
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("id","104");
            paramMap.put("username","root");
            paramMap.put("password","456123");
            paramMap.put("password2","456123");
            paramMap.put("email","root@123.com");
            paramMap.put("phone","4747474747");
            String jsonString = JSON.toJSONString(paramMap);
            List<Map<String, Object>> result = httpService.put("http://localhost:8989/user/update", headMap, jsonString, true);
            System.out.println(result);
        }

        @Test
        public void testPut1() {
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("Accept","application/json");
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("id","104");
            paramMap.put("username","root");
            paramMap.put("password","456123");
            paramMap.put("password2","456123");
            paramMap.put("email","root@123.com");
            paramMap.put("phone","4747474747");
            String jsonString = JSON.toJSONString(paramMap);
            String json = httpService.put("http://localhost:8989/user/update", jsonString);
            System.out.println(json);
        }

        @Test
        public void testPut2() {
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("Accept","application/json");
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("id","104");
            paramMap.put("username","root");
            paramMap.put("password","456123");
            paramMap.put("password2","456123");
            paramMap.put("email","root@123.com");
            paramMap.put("phone","4747474747");
            String jsonString = JSON.toJSONString(paramMap);
            String json = httpService.put("http://localhost:8989/user/update", jsonString,true);
            System.out.println(json);
        }


        @Test
        public void testPut3() {
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("Accept","application/json");
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("id","104");
            paramMap.put("username","root");
            paramMap.put("password","456123");
            paramMap.put("password2","456123");
            paramMap.put("email","root@123.com");
            paramMap.put("phone","4747474747");
            String jsonString = JSON.toJSONString(paramMap);
            String json = httpService.put("http://localhost:8989/user/update", jsonString,true);
            System.out.println(json);
        }

        @Test
        public void testDelete() {
            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("ids","104,91,92,93");
             String result = httpService.delete("http://localhost:8989/user/delete", paramMap, true);
            System.out.println(result);
        }

  	/**
  	* 文件上传接口，需要传参数filePath、fileName、fileType、mediaType，封装成一个map请求上传
  	*/
  	@Test
    public void fileUpload() {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("filePath","C:\\workspace\\WorkInfo\\hello\\notify.xml");
        paramMap.put("fileName","notify.xml");
        paramMap.put("fileType","xml");
        paramMap.put("mediaType","text/xml; charset=utf-8");
        Map<String, String> upload = httpService.upload("http://localhost:8989/user/upload", paramMap);
        System.out.println(upload);
    }

```

http异步调用，框架封装了异步调用的接口ResultCallback, 异步调用回调需要处理的额业务逻辑可以创建一个匿名对象，也可以实现该接口，new 一个实现类传入参数进行异步回调逻辑的处理。具体样例如下：

```java
    //其他的post、put、delete用法类似，这里就不列举了。
	@Test
    public void testAsyncGet() {

        Map<String, String> headMap = Maps.newHashMap();
        headMap.put("Accept","application/json");

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("id","1");
        httpService.asyncGet("http://localhost:8989/user/findById", headMap, paramMap,
            //创建一个匿名类，进行回调处理
            new ResultCallback() {
            @Override
            public void callResponse(Call call, HttpResponse httpResponse) {
                System.out.println(httpResponse);
            }
        });
    }
```