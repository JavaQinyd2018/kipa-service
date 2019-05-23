> # Kipa测试框架的总体介绍
>
> 背景：实际的分布式项目测试会涉及http接口、dubbo接口的调用，同时消息、缓存也是我们测试的校验项，本测试框架提供了http接口调用、dubbo接口调用、数据库增删改查、http请求的mock、sftp文件的上传和下载、RocketMQ消息的发送和消费以及redis缓存的基本操作，能够满足分布式系统测试的基本要求，对于测试人员来说，可以极大的减少对于测试代码本身的时间消耗，更多关注业务本身；进而提高测试效率，提升测试质量。
>
> > 代码的目录结构
> >
> > ```
> > ├─java
> > │  └─com
> > │      └─kipa
> > │          ├─base
> > │          ├─check
> > │          ├─config
> > │          ├─data
> > │          ├─dubbo
> > │          │  ├─annotation
> > │          │  ├─entity
> > │          │  ├─enums
> > │          │  ├─exception
> > │          │  └─service
> > │          │      ├─base
> > │          │      ├─execute
> > │          │      └─impl
> > │          ├─env
> > │          ├─http
> > │          │  ├─annotation
> > │          │  ├─core
> > │          │  ├─emuns
> > │          │  ├─exception
> > │          │  ├─service
> > │          │  │  ├─base
> > │          │  │  ├─convert
> > │          │  │  ├─execute
> > │          │  │  └─impl
> > │          │  └─ssl
> > │          ├─log
> > │          ├─mock
> > │          │  ├─annotation
> > │          │  ├─entity
> > │          │  └─service
> > │          │      ├─base
> > │          │      ├─bo
> > │          │      ├─execute
> > │          │      └─impl
> > │          ├─mq
> > │          │  ├─consumer
> > │          │  └─producer
> > │          ├─mybatis
> > │          │  ├─mapper
> > │          │  ├─provider
> > │          │  ├─service
> > │          │  │  └─impl
> > │          │  └─type
> > │          ├─sftp
> > │          └─utils
> > └─resources --框架配置数据文件
> >     ├─db
> >     ├─dubbo
> >     ├─http
> >     ├─mock
> >     ├─mq
> >     └─redis
> > ```
> >
> > * base：整个项目的配置类，包括和testng整合的测试的基类，用于测试用例的继承和加载testng的测试框架
> > * check：实体类的校验工具类以及用到的常量
> > * config：spring整合http、dubbo、mockserver、mybatis、redis、rocketMq的配置类，包括可控的配置开启注解
> > * data：testng框架数据驱动和数据csv文件解析的工具类
> > * dubbo：整合dubbo框架，远程调用提供dubbo consumer的调用客户端
> > * env：环境切换的信息以及注解
> > * http ：整合http框架，进行get、post、put、delete等操作
> > * log：系统日志注解
> > * mock：mock http请求的框架
> > * mq：rocketMq的整合，提供消息的生产者和消费者操作入口
> > * redis：整合spring data redis，用于redis缓存基本操作
> > * mybatis ：整合mybatis框架，用于数据库的增删改查操作
> > * sftp ：整合jsch框架，提供sftp的文件的远程操作
> > * utils：包括各种工具类

> > 代码封装的底层的第三方工具
> >
> > * http 封装的 okhttp
> > * dubbo 利用apache dubbo的泛化调用技术实现的封装，不依赖任何的api代码
> > * mybatis 利用mybatis 的provider实现动态sql
> > * mock 利用mockserver，绑定特定接口进行http请求的mock
> > * sftp 利用jsch和common-pool2实现基于对象池的jsch连接
> > * redis 直接spring 整合redis，有单机和集群两种配置
> > * mq 直接整合rocketMq

# 详细使用

## 一. 依赖配置

### 1. 依赖

```xml
		<dependency>
            <groupId>com.kipa</groupId>
            <artifactId>kipa-service</artifactId>
            <version>1.0.0</version>
        </dependency>
```

### 2. 配置

(1) 基本配置

```
1.直接继承BaseTestConfiguration基类，同时需要配置在resources下面的config目录下配置，项目启动会查找这些文件，如果不存在框架会启动失败。
A: 数据库配置文件----db.properties
B：dubbo配置文件----dubbo.properties
C: http配置文件-----http.properties
D: mock配置文件-----mockserver.properties
E：sftp的配置文件---sftp.properties
2. 需要在项目的resources下面新建配置文件：application.properties，方便我们将自定义配置的常用信息方法该文件中，并通过spring提供的@Value注解，获取属性值，如：
=================================================================
shopping.web.base.url=http://127.0.0.1:1234/hello/console
=================================================================
    @Value("${shopping.web.base.url}")
    private String baseUrl;
如果不新建项目就会报错：
java.io.FileNotFoundException: class path resource [application.properties] cannot be opened because it does not exist
3. 需要在resources下面新建data目录，用于放数据驱动的数据文件，当然这个不是必选项，不会影响框架的整体启动，但是推荐这样做。
```

(2) 自定义配置（高级配置，支持Redis和RocketMQ）

```tex
A:整合BaseConfiguration框架总的配置类
B:通过继承AbstractTestNGSpringContextTests（spring整合testng入口类）
C:选择配置redis，通过添加@EnableRedis注解开启redis，RedisModel默认是集群模式的redis，可以自定义配置STAND_ALONE（单机模式的）redis
D:选择配置rocketMq,通过添加@EnableRocketMQ开启rocketMq的配置，需要配置扫描消息消费监听的路径listenerScanPackage，扫描带有@RocketMQListener注解的类，将其加入消费监听容器中
```

> 整合配置如下：
>
> ```java
> @Configuration
> @Import(BaseConfiguration.class)
> //包扫描：扫描带有@Database、@Dubbo、@Http的注解从而动态的切换spring配置
> @AppConfigScan("com.kipa.service")
> //默认开启集群的redis操作，若要开启集群的请设置model为RedisModel.CLUSTER。
> @EnableRedis(model = RedisModel.STAND_ALONE)
> //开启MQ
> @EnableRocketMQ(listenerScanPackage = "com.kipa.service")
> public class ApplicationConfiguration {
> }
> ```
>
> ```java
> @ContextConfiguration(classes = ApplicationConfiguration.class)
> public class BaseTestContextApplication extends AbstractTestNGSpringContextTests {
> }
>
> ```

整个的配置情况如下图：

![kipa](C:\workspace\WorkInfo\other\wechat images\微信截图_20190521163151.png)

### 3. 配置文件详解

（1）数据库配置：db.properties

```properties
#数据源配置
# 数据库驱动
mybatis.datasource.driver=com.mysql.cj.jdbc.Driver
# 数据库url
mybatis.datasource.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
# 用户名
mybatis.datasource.username=root
# 密码
mybatis.datasource.password=123456
```

（2）http配置：http.properties

```properties
#http client的配置
# 安全证书校验开关
okhttp.client.verifySSLCertificate=false
# 安全证书本地路径 绝对路径
okhttp.client.certificatePath=D:\\WebProject\\test-service\\src\\main\\java\\com
# 安全证书秘钥路径
okhttp.client.keyStorePath=D:\\WebProject\\test-service\\src\\main\\java\\com
# 安全秘钥
okhttp.client.keyStorePass=781974194
# http连接池最大IDLE连接数
okhttp.client.maxIdleConnections=30
# http 连接时长
okhttp.client.keepAliveDuration=10
```

（3）dubbo的配置：dubbo.properties

```properties
#注册中心的配置
#地址包括：host和port两部分
#zk
dubbo.consumer.register.protocol=zookeeper
#集群模式：10.20.153.10:2181,10.20.153.11:2181,10.20.153.12:2181
dubbo.consumer.register.address=127.0.0.1:2181
#dubbo.consumer.register.group=test
dubbo.consumer.register.timeout=60
#redis 配置
#dubbo.consumer.register.protocol=redis
# redis集群地址
#dubbo.consumer.register.address=10.20.153.10:6379,10.20.153.11:6379,10.20.153.12:6379
#dubbo.consumer.register.group=test
#dubbo.consumer.register.timeout=60
```

（4）mock的配置：mockserver.properties

```properties
# mock远程host
mock.server.remote.host=192.168.31.22
#mock远程端口
mock.server.remote.port=3456
```

（5）redis的配置：redis.properties

```properties
#redis
#redis cluster（集群）地址
spring.redis.cluster.address=192.168.3.21:36379,192.168.3.22:36379,192.168.3.23:36379
# redis 集群密码
spring.redis.cluster.password=123456

# redis单机地址
spring.redis.standalone.address=192.168.3.22:98613
#redis 单机密码
spring.redis.standalone.password=123456
```

（6）sftp的配置：sftp.properties

```properties
#sftp
# sftp服务器host
sftp.connection.host=192.168.31.22
# sftp服务器端口号
sftp.connection.port=3456
# sftp服务器用户名
sftp.connection.username=root
# sftp服务器密码
sftp.connection.password=root
```

（7）mq的配置

```properties
###producer
#该应用是否启用生产者
rocketmq.producer.isOnOff=on
#发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
rocketmq.producer.groupName=kipa-consumer
#mq的nameserver地址
rocketmq.producer.nameServerAddress=127.0.0.1:9876
#消息最大长度 默认1024*4(4M)
rocketmq.producer.maxMessageSize=4096
#发送消息超时时间,默认3000
rocketmq.producer.sendMsgTimeout=3000
#发送消息失败重试次数，默认2
rocketmq.producer.retryTimesWhenSendFailed=3
# 扫描该路径下面所有类型的@produce注解，并获取对应的信息

###consumer
##该应用是否启用消费者
#rocketmq.consumer.isOnOff=on
#rocketmq.consumer.groupName=kipa-producer
##mq的nameserver地址
#rocketmq.consumer.namesrvAddr=127.0.0.1:9876
#该消费者订阅的主题和tags("*"号表示订阅该主题下所有的tags),格式：topic~tag1||tag2||tag3;topic2~*;
rocketmq.consumer.consumeThreadMin=20
rocketmq.consumer.consumeThreadMax=64
#设置一次消费消息的条数，默认为1条
rocketmq.consumer.consumeMessageBatchMaxSize=1
```

## 二、 使用示例

### 1. 基本使用

```java
/**
* 直接继承框架提供的测试基类
*/
public class HelloTest extends BaseTestConfiguration {
    
    @Test
    public void hello() {
        System.out.println("hello kipa test");
    }
}
```

```java
/**
* 继承框架自定义测试的基类
*/
public class HelloTest extends BaseTestContextApplication {
    
    @Test
    public void hello() {
        System.out.println("hello kipa test");
    }
}
```

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

### 3. dubbo使用

dubbo接口调用需要传的参数有：接口名称（接口全路径、方法名称、参数类型名称全路径与参数值），如果dubbo接口是没有参数的，参数名称全路径和参数值不用传，否则会报错或者找不到服务提供者。dubbo调用有三种方式：同步调用、异步调用、直连调用。样例如下：

```java
public class DubboTest extends BaseTestConfiguration {

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

### 4. mock使用

框架提供的mock功能只支持mock http请求，需要传入http请求的相关信息和http响应的相关信息，会在本地分配出来一个端口mock服务，默认是6231。当然，端口可以自己在mockserver.properties进行配置。

```java
public class MockTest extends BaseTestConfiguration {

    @Autowired
    private MockService mockService;
  
  	/**
  	* 通过文件构造mock请求和响应对象，框架提供了CSVutils工具类，可以构造对象
  	* @BeforeMethod 这个注解是为了在@Test方法之前运行mock操作，方便在@Test方法里面http调用的时候
  	* 返回mock的结果
  	*/
    @BeforeMethod
    public void before() {
    MockParamRequest request =CSVUtils.convert2Bean(MockParamRequest.class,"mock/data/mockParamRequest.csv",0,CSVType.VERTICAL);
    MockResponse response = CSVUtils.convert2Bean(MockResponse.class,"mock/data/mockResponse.csv",0,CSVType.VERTICAL);
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
    public class DataHelloTest extends BaseTestConfiguration {

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

### 5. sftp使用

项目中常常存在生成话单或者账单文件等业务进行数据传递或者业务交互，框架提供了SftpHelper工具用于文件的上传、下载到sftp服务器。 常用的操作有：文件的长传、下载、删除、是否存在等操作。

```java
public class SftpTest  extends BaseTestConfiguration {

    @Test
    public void testUpload() {
        String localFilePath = "C:\\workspace\\Project\\testTools\\execute\\src\\test\\resources\\service\\data\\test\\Test.csv";
        SftpHelper.upload(localFilePath, "/opt/data/sftp/test/","Test.csv");
    }
  
    @Test
    public void testDownload() {
        String localFilePath = "C:\\workspace\\Project\\testTools\\execute\\src\\test\\resources\\service\\data\\test\\Test.csv";
      String romotePath = "/opt/data/sftp/test/Test.csv";
        SftpHelper.download(romotePath, localFilePath);
    }
}

```

###6. DB(mybatis)使用

框架对于数据库的操作，封装了mybatis的一些简单的CRUD操作，传入参数或者sql语句就可以实现和数据库的交互。

##### （1）基本的增删改查操作

```java
public class DatabaseTest  extends BaseTestConfiguration {
  
  		//直接注入DatabaseService服务进行数据库增删改查的操作
        @Autowired
        DatabaseService databaseService;

  		/**
  		* 查询一条数据，将多个查询条件添加到list中拼成sql语句进行交互
  		* 将结果封装成 Map<String, Object>
  		*/
        @Test
        public void test() {
            Map<String, Object> map = databaseService.selectOne("tb_user", Arrays.asList("username='kobe'", "id = 1"));
            System.out.println(map);
        }

  		/**
  		* 查询一条数据，将多个查询条件添加到map中拼成sql语句进行交互
  		* 将结果封装成 Map<String, Object>
  		*/
        @Test
        public void test1() {
            Map<String,Object> map = Maps.newHashMap();
            map.put("id",1);
            map.put("username","kobe");
            Map<String, Object> resultMap = databaseService.selectOne("tb_user", map);
            System.out.println(resultMap);
        }
		/**
  		* 查询一条数据，直接传入sql语句进行查询
  		* 将结果封装成 Map<String, Object>
  		*/
        @Test
        public void test2() {
            Map<String, Object> map = databaseService.selectOne("select * from tb_user where username = 'kobe' and id = 1");
            System.out.println(map);
        }

  		/**
  		* 查询多条数据，将多个查询条件添加到list中拼成sql语句进行交互
  		* 将结果封装成List<Map<String, Object>>
  		*/
        @Test
        public void test3() {
            List<Map<String, Object>> list = databaseService.selectList("tb_user", Arrays.asList("password = '123456'", "phone = '141414141'"));
            System.out.println(list);
        }

        @Test
        public void test4() {
            Map<String,Object> map = Maps.newHashMap();
            map.put("password",123456);
            map.put("phone","141414141");
            List<Map<String, Object>> list = databaseService.selectList("tb_user", map);
            System.out.println(list);
        }

        @Test
        public void test5() {
            List<Map<String, Object>> list = databaseService.selectList("select * from tb_user where password = '123456' and phone = '141414141'");
            System.out.println(list);
        }

    	/**
  		* 查询指定的列对应的结果集，将多个查询条件添加到list中拼成sql语句进行交互
  		* 将结果封装成List<Map<String, Object>>
  		*/
        @Test
        public void test6() {
            List<Map<String, Object>> list = databaseService.selectColumn("tb_user",
                    Arrays.asList("username","password","created", "updated"),
                    Arrays.asList("phone = '16323232223'"));
            System.out.println(list);
        }

        @Test
        public void test7() {
            Map<String,Object> map = Maps.newHashMap();
            map.put("phone","16323232223");
            List<Map<String, Object>> list = databaseService.selectColumn("tb_user",
                    Arrays.asList("username", "password", "created", "updated"),
                    map);
            System.out.println(list);
        }

    	/**
  		* 统计结果，将多个查询条件拼成sql语句进行交互
  		* 返回的结果就是条件对应的统计结果
  		*/
        @Test
        public void test8() {
            Long count = databaseService.count("tb_user", Arrays.asList("phone = '16323232223'"));
            System.out.println(count);
        }

        @Test
        public void test9() {
            Map<String,Object> map = Maps.newHashMap();
            map.put("password",123456);
            map.put("phone","141414141");
            Long count = databaseService.count("tb_user", map);
            System.out.println(count);
        }

    	/**
  		* 分页查询多条数据，将多个查询条件拼成sql语句进行交互
  		* 将结果封装成List<Map<String, Object>>
  		*/
        @Test
        public void test10() {
            Map<String,Object> map = Maps.newHashMap();
            map.put("password",123456);
            map.put("phone","141414141");
            List<Map<String, Object>> list = databaseService.selectPage("tb_user", map, 1, 2);
            System.out.println(list);
        }

        @Test
        public void test11() {
            List<Map<String, Object>> list = databaseService.selectPage("tb_user", Arrays.asList("password='123456'", "phone='141414141'"), 2, 1);
            System.out.println(list);
        }

  		/**
  		* 插入数据，将数据添加到map中进行数据插入
  		* 返回结果 插入成功返回1，插入失败返回0
  		*/
        @Test
        public void test12() {
            Map<String, Object> map = Maps.newLinkedHashMap();
            map.put("username","mybatis");
            map.put("password","123456");
            map.put("phone","1234567890");
            map.put("email","mybatis@123.com");
            map.put("created",new Date());
            map.put("updated",new Date());
            int result = databaseService.insert("tb_user", map);
            System.out.println(result);
        }

  		/**
  		* 插入数据，直接sql语句插入
  		* 返回结果 插入成功返回1，插入失败返回0
  		*/
        @Test
        public void test13() {
            String sql = "insert into tb_user(username,password,phone,email,created,updated) values('mysql','456789','797328323232','123@123.com',now(),now())";
            int result = databaseService.insert(sql);
            System.out.println(result);
        }

  		/**
  		* 当数据很多的时候，就可以用csv文件批量插入了， csc文件是行模式的csv文件
  		* 返回结果 插入成功返回1，插入失败返回0
  		*/
        @Test
        public void test14() {
            String csvFilePath = "service/data/test/Test2.csv";
            int result = databaseService.batchInsert("tb_user", csvFilePath);
            System.out.println(result);
        }

    	/**
  		* 更新，将数据添加到map中进行数据更新
  		* 返回结果 插入成功返回1，插入失败返回0
  		*/
        @Test
        public void test15() {
            Map<String, Object> map = Maps.newLinkedHashMap();
            map.put("username","book");
            map.put("password","123456");
            map.put("phone","67236726323");
            map.put("email","book@123.com");
            Map<String, Object> map1 = Maps.newLinkedHashMap();
            map1.put("phone","1234567890");
            map1.put("email","mybatis@123.com");
            int result = databaseService.update("tb_user", map, map1);
            System.out.println(result);
        }

        @Test
        public void test16() {
            int result = databaseService.update("tb_user", Arrays.asList("phone='68136813131'", "email='hello@123.com'"),
                    Arrays.asList("username='book'", "password='123456'"));
            System.out.println(result);
        }

        @Test
        public void test17() {
            int update = databaseService.update("update tb_user set password='123456' where username = 'oracle4'");
            System.out.println(update);
        }

    	/**
  		* 删除数据，将数据添加到map中进行数据插入
  		* 返回结果 插入成功返回1，插入失败返回0
  		*/
        @Test
        public void test18() {
            Map<String, Object> map = Maps.newLinkedHashMap();
            map.put("username","oracle5");
            int result = databaseService.delete("tb_user", map);
            System.out.println(result);
        }

        @Test
        public void test19() {
            int result = databaseService.delete("tb_user", Arrays.asList("username = 'oracle7'"));
            System.out.println(result);
        }

        @Test
        public void test20() {
            int result = databaseService.delete ("delete from tb_user where username = 'oracle6'");
            System.out.println(result);
        }
}
```
#####  （2） 直接执行sql脚本

如果是批量数据，需要通过sql脚本写到数据库中，可以使用sql脚本服务批量插入数据

```java
           //直接注入脚本执行的服务类
		   @Autowired
            private SqlScriptService sqlScriptService;

            @Test
            public void test21() {
                String sql = "service/data/test/user.sql";
                sqlScriptService.executeSqlScript(sql);
            }
```
```sql
-------------------------sql脚本------------------------------
insert into tb_user(username,password,phone,email,created,updated) values('oracle1','456789','797328323232','oracle1@123.com',now(),now());
insert into tb_user(username,password,phone,email,created,updated) values('oracle2','456789','797328323232','oracle2@123.com',now(),now());
insert into tb_user(username,password,phone,email,created,updated) values('oracle3','456789','797328323232','oracle3@123.com',now(),now());
insert into tb_user(username,password,phone,email,created,updated) values('oracle4','456789','797328323232','oracle4@123.com',now(),now());
insert into tb_user(username,password,phone,email,created,updated) values('oracle5','456789','797328323232','oracle5@123.com',now(),now());
insert into tb_user(username,password,phone,email,created,updated) values('oracle6','456789','797328323232','oracle6@123.com',now(),now());
insert into tb_user(username,password,phone,email,created,updated) values('oracle7','456789','797328323232','oracle7@123.com',now(),now());
```

### 7. 结果校验

框架提供了CheckHelper这个工具类帮助我们进行结果校验，提供了实体类、集合、数组、map，还有大对象的校验，checkBeanEquals对普通Javabean以及基础数据类型校验；checkEntityEquals是服实体类进行校验，实体类具备的特征就是没有大字段，同时提供了私有属性的get和set方法。具体示例如下：

#### （1）校验普通基础数据类型，包括list、map、数组

```java
public class CheckTest extends BaseTestConfiguration {

    	List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        map.put("username","kobe");
        map.put("phone","123456789");
        Map<String, Object> map3 = Maps.newHashMap();
        map3.put("username","kobe2");
        map3.put("phone","123456789");
        list.add(map);
        list.add(map3);
        List<Map<String, Object>> list1 = Lists.newArrayList();
        Map<String, Object> map1 = Maps.newHashMap();
        map1.put("username","kobe1");
        map1.put("phone","123456789");
        Map<String, Object> map4 = Maps.newHashMap();
        map4.put("username","kobe");
        map4.put("phone","123456789");
        list1.add(map1);
        list1.add(map4);
  		//校验 两个map对象是否相等
        CheckHelper.checkBeanEquals(map, map1, "map不相等");
  		//校验 两个list集合是否相等
        CheckHelper.checkBeanEquals(list, list1, "list不相等");
=======================================================================
Exception in thread "main" java.lang.RuntimeException: list不相等: 
当前集合第1个元素字段对应的
实际值为：[username <==> kobe]，期望值为：[username <==> kobe1]
当前集合第2个元素字段对应的
实际值为：[username <==> kobe2]、期望值为：[username <==> kobe]
```

#### （2）校验普通Javabean

csv数据文件

![微信截图_20190521161148](C:\workspace\WorkInfo\other\wechat images\微信截图_20190521160709.png)

```java
@Test
public void testCheck() {
  String csv = "mock/data/MockParamRequest_1.csv";
  MockParamRequest request = CSVUtils.convert2Bean(MockParamRequest.class, csv, 0, CSVType.VERTICAL);
  MockParamRequest request1 = CSVUtils.convert2Bean(MockParamRequest.class, csv, 1, CSVType.VERTICAL);
        CheckHelper.checkBeanEquals(request, request1, "对象不相等");
}
=====================================================================================
Exception in thread "main" java.lang.RuntimeException: 对象不相等: 
method字段对应的实际值为：[GET]，期望值为：[POST]，二者不相等
path字段对应的实际值为：[/userInfo/showUserInfo1]，期望值为：[/userInfo/showUserInfo]，二者不相等
headers字段对应的
实际值为：[book1 <==> chinese]，期望值为：[book <==> chinese]
实际值为：[food <==> hello]，期望值为：[food <==> goods]
secure字段对应的实际值为：[false]，期望值为：[true]，二者不相等
cookies字段对应的
实际值为：[password <==> 1234561]，期望值为：[password <==> 123456]
实际值为：[address <==> nanjiang]，期望值为：[address <==> hangzhou]
实际值为：[username <==> kobe]，期望值为：[username <==> kobe2]
requestParams字段对应的
实际值为：[phone <==> 781931981313]，期望值为：[phone <==> 7819319813131]
	at com.kipa.check.CheckHelper.process(CheckHelper.java:82)
	at com.kipa.check.CheckHelper.checkBeanEquals(CheckHelper.java:77)
	at service.csv.CsvUtilsTest.main(CsvUtilsTest.java:70)
```



## 三、高级使用

### 1. 数据处理

#### （一）csv文件进行数据处理

#####  (1) 将csv转化为对象

csv文件的格式被设计为三种，分别对应三种不同的枚举

```
/**
 * 水平行模式的csv文件，也就是行模式csv
 */
TRANSVERSE("T"),
/**
 * 竖直列模式的csv文件， 也就是列模式csv
 */
VERTICAL("V"),
/**
 * 竖直列模式的数据库校验csv文件，也就是字段带有下划线， 列模式的csv
 */
CHECK_DB("C");
```

csv文件信息, 其中class代表该类的类名，property代表该类的属性字段，flag代表转化的标识、exp代表对应的属性值。

其中flag字段又分为4种

Y--会去拼成字段；

N--不会拼成字段；

M--json格式为对象json（Map式）， 格式如：{"username":"kobe","password":"123456"}；

L--json的格式为列表json（List式），格式如：[{"username":"kobe","password":"123456"},{"username":"kobe","password":"123456"}]

```csv
//对象json样例，文件名：mockParamRequest.csv
"class","property","flag","exp"
"MockParamRequest","path","Y","/userInfo/showUserInfo"
"","method","Y",GET
"","cookies",N,""
"","headers",N,""
"","secure","Y",""
"","requestParams",M,{"phone":"16323232223"}
```

对应的实体类信息：

```java
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MockParamRequest {
    private String path;
    private String method;
    private Map<String, String> cookies ;
    private Map<String, String> headers ;
    private boolean secure;
    private Map<String, String> requestParams;
}
```
```java
//1.创建一个csv文件
//创建列模式csv
CSVUtils.createCsvFile(MockParamRequest.class, "mock/data/MockParamRequest_1.csv", CSVType.VERTICAL); 
//创建行模式csv
CSVUtils.createCsvFile(MockResponse.class,"mock/data/MockResponse_1.csv",CSVType.TRANSVERSE); 
//创建带下划线的csv
CSVUtils.createCsvFile(MockResponse.class,"mock/data/MockResponse_2.csv",CSVType.CHECK_DB);
============================================================================================
  //2. csv转化
/**
* 转化为一个对象
*/
MockResponse response1 = CSVUtils.convert2Bean(MockResponse.class, "mock/data/mockResponse_3.csv", 0, CSVType.VERTICAL);
/**
* 转化为对象list
*/
List<MockParamRequest> mockParamRequests = CSVUtils.convert2BeanList( MockParamRequest.class, "mock/data/mockParamRequest.csv", CSVType.VERTICAL);
```

```csv
//列表json样例 文件名：mockResponse.csv
"class","property","flag","exp"
"MockResponse","cookies",N,""
"","headers",N,""
"","body",L,"[{""id"":1, ""username"":""kobe"",""phone"":""16323232223"",""created"":""1234-09-14""}]"
"","reasonPhrase",N,""
"","statusCode","Y",200
"","delay",N,""
```

解析csv文件将数据封装成Map<String, Object>、List<Map<String, Object>> 进行其他操作。

```java
//解析csv生成list
List<Map<String, Object>> mapList = CSVUtils.parseCsvFile("data/head.csv", CSVType.VERTICAL);
//解析csv生成map
Map<String, Object> map = CSVUtils.parseCsvFile("data/head.csv", 1, CSVType.VERTICAL);
```

#### (二) 基于testng的数据驱动

##### （1）testng提供的dataprovider进行数据驱动

默认csv数据文件的路径是当前测试类的类名+方法.csv, csv文件如下：

```j
url,path,name,value
http://127.0.0.1:6231,/userInfo/showUserInfo,phone,16323232223
```

```java
public class CsvDataTest extends BaseTestConfiguration{
  
    @Autowired
    private HttpService httpService;
	//数据驱动的名称为csv， csv文件的读取路径为：CsvDataTest.testMock.csv
    @Test(dataProvider = "csv")
    public void testMock(String url,String path,String name, String value) {
        String s = httpService.get(String.format("%s%s?%s=%s",url, path, name, value), true);
        System.out.println(s);
    }
}
	
```

（2）自定义数据参数的文件路径

框架提供了注解@DataMeta和@DataParam, 当标识有@MockHttp注解的方法需要进行数据驱动的时候，单一的参数可以直接通过@DataParam注入数据，多参数的方法需要通过@DataMeta进行数据驱动，具体用法如下：

```java
public class DataHelloTest extends BaseTestConfiguration {

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
        MockParamRequest request = CSVUtils.convert2Bean( MockParamRequest.class,requestCsvFile, 1, CSVType.VERTICAL);
        MockResponse response = CSVUtils.convert2Bean(MockResponse.class, responseCsvFile,1,CSVType.VERTICAL);
        mockService.mockResponse(request, response);
    }

    @Test(dataProvider = "csv")
    public void test(String url,String path,String name, String value) {
        System.out.println("=================测试=================");
        String s = httpService.get(String.format("%s%s?%s=%s",url, path, name, value), true);
        System.out.println(s);
    }
}
```

### 2. 环境的切换

在实际的测试过程中，我们可能会有多套环境，比如开发环境（dev）、测试环境（qa），预生产环境（preqa）,生产环境，框架提供了@AppConfigScan用于扫描配置注解@Database、@Dubbo、@Http。

其中：

@Database注解是数据源配置注解，用于数据源的切换

@Dubbo注解是用于dubbo接口调用的消费端的配置

@Http注解是用于http和https等安全证书的环境切换配置

配置在总的框架的总配置类上面， 具体配置情况如下：

```java
@Configuration
//导入基本的http、dubbo、mock、mybatis的spring配置文件
@Import(BaseConfiguration.class)
//包扫描：扫描带有@Database、@Dubbo、@Http的注解从而动态的切换spring配置
@AppConfigScan("com.kipa.service")
public class DemoApplicationConfiguration {

}

这三个注解推荐配置到测试的入口类上面，需要修改的时候直接修改入口配置类，这样会方便很多
 * 框架高级用法整合样例，切勿直接继承该类
 */
@Database(datasourceFlag = "dev")
@Http(httpFlag = "dev")
@Dubbo(configFlag = "dev",version = "1.0.0",timeout = 120000)
@Listeners({DataMetaAnnotationListener.class})
@ContextConfiguration(classes = DemoApplicationConfiguration.class)
public class DemoTestContextConfiguration extends AbstractTestNGSpringContextTests {

    @DataProvider(name = "csv")
    public Iterator<Object[]> providerData(Method method) {
        CSVDataProvider csvDataProvider = new CSVDataProvider();
        return csvDataProvider.providerData(method);
    }
}

```

### 3. RocketMQ的使用

分布式系统的测试过程中，，往往需要发消息或者消费消息来实现业务的异步调用或者服务的解耦，需要测试人员进行接收消息的操作，框架通过整合RocketMQ提供了发送消息的服务以及消费消息的入口，帮助更简单的测试。

### （一）配置

#### （1）配置主配置类

```java
@Configuration
@Import(BaseConfiguration.class)
//添加@EnableRocketMQ注解，开启RocketMQ，listenerScanPackage必须要填写，标识扫描对应路径下面
//所有的带@RocketMQListener注解的类，并开启消费监听
@EnableRocketMQ(listenerScanPackage = "service.mq")
public class ApplicationConfiguration {

}
```

#### （2）配置数据文件

```properties
#rocketMq的生产者的配置
rocketmq.producer.groupName=kipa
#mq的nameserver地址
rocketmq.producer.nameServerAddress=127.0.0.1:9876

#rocketMq的消费者的配置
rocketmq.consumer.groupName=kipa
#mq的nameserver地址
rocketmq.consumer.nameServerAddress=127.0.0.1:9876
```

### （二）使用方式

#### （1）消息生产者

框架提供了MQProducerService消息生产服务发送消息，会根据topic、tag等配置进行消息发送，可以满足基本的发送要求

```java
public class ProducerTest extends BaseTestContextApplication {
    @Autowired
    private MQProducerService mqProducerService;

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            mqProducerService.send("hello rocketMq, this is hello world===>"+i,"TestTopic","TagA");
        }
    }

}
```

#### （2）消息消费者

框架提供了@RocketMQListener注解来标识消息的消费监听器，当容器启动后会扫描所有的@RocketMQListener注解标识的类，启动对应的监听，同时放到容器中，进行消息的消费。

框架会解析@Subscribe注解标识的方法，将消费的消息转化为messageType类型参数值，注入到对应的方法结果中，我们可以直接在方法中进行我们的业务测试工作，不用额外的关心消息消费的其他细节

```java
@RocketMQListener(topic = "TestTopic",consumePosition = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET)
public class ConsumerService {

    @Subscribe(tag = "TagA",messageType = String.class)
    public void test1(String message) {
        System.out.println("=============="+message+"===============");
    }
    
    @Subscribe(tag = "TagA",messageType =MessageExt.class)
    public void test2(MessageExt message) {
        System.out.println("====="+message.getMsgId()+"========="+new String(message.getBody(), StandardCharsets.UTF_8)+"===============");
    }

}
```

### 4. Redis的使用

项目在请求频繁或者常用数据的一些业务中会做数据缓存，往往需要测试去验证缓存中的数据正确性或者有效性，框架整合了spring data redis，提供了RedisTemplate和StringRedisTemplate进行缓存的处理。

### （一）配置

#### （1）配置主配置类

```java
@Configuration
@Import(BaseConfiguration.class)
//添加开启@EnableRedis注解，开启redis的配置,RedisModel如果是STAND_ALONE代表单机版的redis
//RedisModel如果是CLUSTER代表集群版的redis,默认是集群版的redis
@EnableRedis(model = RedisModel.STAND_ALONE)
public class ApplicationConfiguration {

}
```

#### （2）配置数据文件

```properties
#redis
#redis cluster（集群）地址
spring.redis.cluster.address=192.168.3.21:36379,192.168.3.22:36379,192.168.3.23:36379
# redis 集群密码
spring.redis.cluster.password=123456

# redis单机地址
spring.redis.standalone.address=192.168.3.22:98613
#redis 单机密码
spring.redis.standalone.password=123456
```

####  （3）使用

直接注入RedisTemplate或者StringRedisTemplate进行redis的数据操作

```java
public class RedisTest extends BaseTestContextApplication {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        System.out.println(redisTemplate.opsForHash().get("hello","123456"));
        System.out.println(stringRedisTemplate.opsForValue().get("hello"));
    }
}
```

