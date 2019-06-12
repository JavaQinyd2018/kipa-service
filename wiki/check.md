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
