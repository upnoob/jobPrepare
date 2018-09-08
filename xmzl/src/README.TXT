Eduxm项目（Struts MVC架构）
1.	安全管理 shiro
1.1	选择shiro的原因以及shiro与Spring Security的区别
    Shiro相对于Spring security来说，配置简单和灵活。Shiro是一个强大而灵活的开源安全框架，能够非常清晰的处理认证，授权，管理会话以及密码加密等。具有如下特点：
    1.	易于理解的java security API
    2.	简单的身份认证（登录），支持多种数据源（LDAP，jdbc， kerberos， activeDirectory等）
    3.	对角色简单的签权（访问控制），支持细粒度的签权。
    4.	支持一级缓存，以提升应用程序的性能；
    5.	内置的基于POJO企业会话管理，适用于Web以及非Web环境；
    6.	异构客户端会话访问；
    7.	非常简单的加密API；
    8.	不与其他框架或者容器捆绑，可以独立运行。
1.2	Spring Security
    除了不能脱离Spring，Shiro的功能都具备，而且Spring security对Oauth（OAUTH协议为用户资源的授权提供了一个安全的、开放而又简易的标准。）、OpenID（OpenID 是一个以用户为中心的数字身份识别框架，它具有开放、分散性。）也有支持，而shiro则需要手动的实现。Spring security的权限细粒度更高。
注：
	Oauth在客户端与服务提供商之间设置了一个授权层，客户端不能直接登录服务提供商，只能登录授权层，以此将用户与客户端区分开来，客户端登录授权层所用的令牌token，与用户的密码不同，用户可以在登录的时候，指定授权层令牌的权限范围和有效期。
	OpenID框架的第一部分是身份验证，即如何通过URI来认证用户身份，目前的网站都是依靠用户名和密码来进行登录认证，这就意味着大家在每个网站都需要注册用户名和密码，即便你使用的是同样的密码。如果使用OpenID，你的网址URI就是你的用户名，而你的密码安全的存储在一个OpenID服务网站上。
1.3	Shiro的基本模块
    Subject：主体，可以是任何与应用（C++、java、php、web MVC等）交互的用户。
    SecurityManager：相当于SpringMVC中的DispathcherServlet或者Struts2中的FilterDispatcher，是Shiro的心脏。所有具体的交互都通过SecurityManager进行控制，它管理所有Subject，且负责进行认证、授权、会话管理和缓存管理。
    Authenticator认证器，负责对主体的认证，这是一个扩展点，可以重写。
    Authrizer:授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作。
    Realm：可以有一个或者多个Realm，可以认为是安全实体数据源，即用于获取安全实体(用户，角色和权限)的，可以使JDBC实现，也可是LDAP实现或者内存实现等等。注意：Shiro不知道你的用户/权限存储在哪里，以何种方式存储，所以一般我们都需要重写Realm。我们的项目中就是根据需求重新了JdbcRealm类 中的doGetAuthenticationInfo()方法和getRoleNamesForUser()方法。
    SessionManager：Session用于管理生命周期，Shiro不仅可以用在Web环境，也可以用在普通的JavaSE环境、EJB等环境。所以Shiro就抽象了一个自己的Session来管理主体与应用之间的交互数据。比如：一开始在一台Web服务器上，接着又上了台EJB服务器，这时想把两台服务器的会话数据放到一个地方，就可以通过实现自己的分布式会话（如把数据放到Memcached服务器）。
    SessionDAO，实现自己的S二十四欧尼DAO，通过JDBC写入数据库，也可以将Session放入Memcached中。
    CacheManager：缓存控制器，管理用户、角色、权限等缓存，提高系统。
    Cryptography：密码模块，Shiro提供了一些常见的加密组件用于加密和解密。
1.4	Shiro四大核心功能
    Authentication、Authorization、Cryptography、SessionManager
1.5	Shiro三个核心组件
    Subject、SecurityManager和Realms.

2.	tomcat Datasource的配置  连接池的配置
配置context.xml文件(而不是在server.xml中配置)，因为server.xml中配置是不可以动态重加载资源的，必须重新启动服务器。

在Tomcat5.5之前，Context体现在/conf/server.xml中的Host元素里的<Context>元素中，它由Context接口定义。每个<Context>元素代表了运行在虚拟主机上的单个Web应用。
<Context path="/kaka" docBase="kaka" debug="0" reloadbale="true">
1、path: 即要建立的虚拟目录，注释/kaka，它指定访问Web应用的上下文根，如http://localhost:8080/kaka/***
2、docBase: 为应用程序的路径或者WAR文件存放的路径，可以使绝对路径，也可为相对路径，相对于<Host>
3、reloadable：如果ture,tomcat服务器在运行状态下会监视WEB-INF/classes和WEB-INF/lib目录，如果监视到有class文件被更新，服务器自动重新加载Web应用程序，无需重启Tomcat

一个Host元素中嵌套任意多的Context元素，每个Context的路径必须是唯一的，有path属性定义。另外，必须定义一个path=""的context，这个context成为该虚拟主机的缺省web应用，用来处理那些不能匹配任何context路径的请求。

在Tomcat5.5字后，不推荐在server.xml中进行配置，而是在/conf/context.xml中独立配置，因为server.xml本身是不可动态加载的资源，服务器一旦启动了以后，要修改这个文件，就得重新启动服务器才能重新加载。而
context.xml文件则不然，tomcat服务器会定时去扫描这个文件，一旦发现文件被修改，就会重新加载这个文件，而不需要重起服务器。

<?xml version="1.0" encoding="UTF-8"?>
<Context>
	<Resource name="jdbc/eduxm" auth="Container" type="javax.sql.DataSource"
		maxActive="5" maxIdle="3" maxWait="10000" username="xxxx"
		password="xxx" driverClassName="oracle.jdbc.driver.OracleDriver"
		url="jdbc:oracle:thin:@210.xxx.xx.xxx:1521:orcl" />
</Context>

name: 表示指定的jndi（java Naming and Directory Interface）名称。
auth: 表示认证方式，一般为Container
type: 类型
maxActive: 连接池支持的最大连接数
maxIdle: 连接池中最多可空闲的连接数
maxWait: 连接池中的连接用完时，新的请求的等待时间，毫秒
username: 数据库用户名
password：数据库密码
driverClass="Oracle.jdbc.driver.OracleDriver"或者driverClass="com.mysql.jdbc.Driver" 表示JDBC Driver
url="jdbc:orcle:thin:@ip:post:orcl"或者url="jdbc:mysql://ip:post/dbname" 表示数据库url地址

分页原理实现
oracle    select * from (select ROW_.*, ROWNUM ROWNUM_ from(sql) ROW_ where ROWNUM_ <= ?) where ROWNUM_ > ?
mysql     select * from tablename limit ?, ?;

导入导出，HSSFWorkBook

事务管理
return work(new TransactionCallback<Project>() {
			@Override
			public Project doInTransaction(TransactionStatus arg0) {
                xxxx
			}
	}

Struts2前后台传参的三种方式
    1.属性驱动：在action中定义属性以及getter/setter方法，前台直接属性获取值。
    2.模型驱动：action实现ModelDriven，定义实体，然后重写getModel方法（懒汉加载，需判断是否为空），返回具体所需对象。前台直接属性获取值
    3.action中定义实体，然后给出实体属性的get/set方法，前台通过实体.属性获取值

解决中文乱码：java.net.URLEncoder.encode(filename.toString(), "UTF-8"); 编码  使用decode进行解码。
前端URL乱发解决   URLEncoder.encode(value, "utf-8");  URLDecoder.decode(value, "utf-8");
获取项目文件路径 String path = ServletActionContext.getServletContext().getInitParameter("param")
                 String realPath = ServletContext sc = ServletActionContext.getServletContext().getRealPath("param");

web.xml中配置元素
<web-app>
    <display-name>xxx</display-name>

    <context-param>
        <param-name>xxxx</param-name>
        <param-value>xxxx</param-value>
    <context-param>

    <listener>
        <listener-class>xxxxx</listener-class>
    <listener>

    <filter>
        <filter-name>ShiroFilter</filter-name>
        <filter-class>org.apache.shiro.web.servlet.ShiroFilter</filter-class>
    <fiter>

    <filter-mapping>
        <filter-name>ShiroFilter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispathcer>REQUEST/FORWARD/INCLUDE/ERROR</dispathcer>
    </filter-mapping>

    <filter>
        <filter-name>struts-prepare</filter-name>
        <filter-class>org.apach.struts2.dispathcer.ng.filter.StrutsPrepareFilter</filter>
    </filter>

    <filter-mapping>
        <filter-name>struts-prepare</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <filter>
        <filter-name>struts-execute</filter-name>
        <filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsExecuteFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>struts-execute</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <resource-ref>
        <description>DB Connection</description>
        <res-ref-name>jdbc/custjob</res-ref-name>
        <res-type>javax.sql.Datasource</res-type>
        <res-auth>Container</res-auth>
     </resource-ref>

     <session-config>
        <session-timeout>30</session-timeout>
     </session-config>

     <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
     </welcome-file-list>

</web-app>

web.xml 加载过程
1. 启动web项目时，容器首先会去它的web.xml中读取两个节点，<listener></listener>  和 <context-param></context-param>
2. 容器（JBoss，Tomcat等）创建一个ServletContext(Application)，这个web项目的所有文件都将共享这个上下文。
3. 容器以<context-param> 的name作为键，value作为值，存入ServletContext。
4. 容器创建<listener>中的实例，并根据配置的<listener-class>来创建监听，在监听过程中会有contextInitialized(ServletContextEvent args)初始化方法，启动Web应用时，系统调用Listener的该方法，在这个方法中获得：
    ServletContext application = ServletContextEvent.getServletContext;
    在我们的Struts中是ServletActionContext.getServletContext().getInitParamter("param-name");
5. 接着，容器会读取<filter></filter>中的内容，并根据指定的类路径来实例化过滤器。
6. 以上步骤都是在web项目还没有完全启动的时候就已经完成的工作。如果系统中还有Servlet，则Servlet是在第一次发起请求时被实例化的，而且一般不会被容器销毁。他可以服务于多个用户请求，所以，Servlet的初始化要比上述步骤晚。
7. 总的来说，web.xml加载顺序为<context-param> <listener>  <filter>  <servlet>。如果出现了相同顺序，则按照出现的先后顺序加载。
8. 对于<filter> 和 <filter-mapping>而言，<filter-mapping>必须出现在对应的<filter>之后，否则会报<filter-name>还未定义。

web.xml标签详解
