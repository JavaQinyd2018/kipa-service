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