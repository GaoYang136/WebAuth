# WebAuth
基于tomcat，结合编程式安全和申明式安全进行web用户验证和授权的示例

    这个程序没有使用Spring之类的开源框架和面向切面编程技术，完全基于Tomcat容器和Servlet规范，让被
    绝大多数程序员抛弃的Servlet规范安全机制变得更加实用。除此之外，因为使用了申明式安全，Tomcat容
    器的很多自带组件都可以配置使用，如单点登录SSO、JDBC连接池、登陆限制等。如果要求实现方式更加优雅，
    则可以考虑使用Tomcat类替代的机制。此示例还实现了一个简单的防CSRF攻击的Filter。

    接下来简单介绍一下实现细节，其主要思想是将验证和权限检查分开。验证由我们自己在响应login POST请
    求的Servlet中调用req.login()函数发起，而权限检查则完全交由Tomcat容器根据web应用描述符的约束规
    则进行处理。首先需要对本应用对应的context容器针对resource和real元素进行配置，如果要使用单点登
    录、登录限制等功能需要参考Tomcat文档对本程序的context.xml配置文件进行适当更改。然后是对部署描
    述符web.xml进行配置，但是只声明安全角色（security-role）和安全约束规则（security-constraint），
    完全不用配置验证方式元素（login-config）。因为没有login-config元素，所以Tomcat会在加载web应用
    时，使用NonLoginAuthenticator。在用户验证阶段NonLoginAuthenticator总是通过并返回true，无论
    发起该请求的用户是否真正通过用户名和密码验证。然后在权限检查时，未验证的用户请求和已验证但权限不
    对的请求都会被拦截，然后调用sendError()设置HTTP响应状态码403（Forbidden）结束处理过程，开始返
    回。在返回过程中StandardHost容器管道中的ErrorReportValve会对未处理的错误响应状态进行处理，处理
    流程就是forward请求到响应到403的error page。在此error page中我们需要区分未验证和已验证但权限不
    对的两种情况，并且只返回后一种情况的内容。而把未验证的请求更改响应状态码并转发到401的error page，
    此时一定不能再简单地使用sendError(401)来处理响应,因为Tomcat已经不会再处理sendError(),所以只能
    自己立即返回401的内容或使用我这里采用的转发方式。

    最后顺便介绍一下防CSRF的Filter。这个Filter的基本思想是使用URL重写为URL添加token来防止CSRF，具
    体实现参照了Tomcat自带的org.apache.catalina.filters.CsrfPreventionFilter。不过与CsrfPreve-
    ntionFilter不同的是不必需配置特定的被保护的页面地址，而是简单地包括所有页面，并且认为需要用户登
    录的页面需要CSRF保护，不需要登录的页面不需要防CSRF保护，而且只要登录以后访问任意一个属于此应用的
    URL都需要携带token，否则视为CSRF攻击，返回403。其次这个Filter并没有像CsrfPreventionFilter使用
    的每5次请求（可配置）就更改token，它是一个会话一直使用同一个token。（我不知道这样会不会带来一些
    风险，按道理来说CsrfPreventionFilter也是有风险的，只不过token变换的更频繁，风险更小而已，CSRF
    是最难防范的web攻击之一）这个Filter也同样具有一个非常明显的缺陷，就是必须使用encodeRedirectURL()
    和encodeURL()方法分别对重定向URL和出现在JSP页面的URL进行重写，而且静态页面里不能包含指向本应用的
    URL，否则就应该改写成JSP并对URL进行重写。当然你也可以对需要保护的页面进行配置，这样可以排除一些静
    态资源。
