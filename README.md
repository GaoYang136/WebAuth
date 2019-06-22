# WebAuth
基于tomcat，结合编程式安全和申明式安全进行web用户验证和授权的示例

    首先，说明一下编写这个示例的目的。当初在我学习Java的时候，就已经接触了Tomcat，但由于教学目的，
    老师留给我们深入了解Tomcat容器的时间并不多。那时我们会写一些功能十分简单的网站作为课后作业，但
    是在每次做网站用户验证和授权时非常痛苦。用户验证和授权是非常重要也非常基础的功能，但是一个广泛
    通用、灵活可扩展、尽可能复用已有代码的实现方法对当时的我还是要求太高。毫无疑问，我最初的代码完
    全是使用编程式安全，每次doGet、doPost方法一开始就要自己检查用户验证和授权，非常枯燥乏味，而且
    硬编码缺乏灵活性。偶尔间竟然看到request对象竟然有login()方法，当时瞬间就觉得自己很蠢，但是奈何
    不知道这个方法该如何使用。随着对Servlet逐步了解，我知道了申明式安全。虽然申明式安全把程序员从枯
    燥的检查工作中解放出来，部署的时候也足够灵活，但是在某些方面限制很大。比如Form验证方式完全隐藏
    登录页面地址，只在你访问某些需要验证身份的网页时会自动转发，而且没有验证码之类的技术（尽管
    Tomcat可以配置登录限制之类的组件），完全不符合绝大多数网站的实现方式。而Basic和Digest验证非常
    简陋，不实用，除了8年前的无线路由器和Tomcat自带Examples的web网站使用这种方式外，我没有见过其它
    使用这种技术的网站。在此种情况下，我决定使用编程式安全和申明式安全结合的方式实现用户验证和授权。
    因为使用了申明式安全，Tomcat容器的很多自带组件都可以配置使用，如单点登录SSO、JDBC连接池、登陆限
    制等。此示例还实现了一个简单的基于申明式安全的防CSRF攻击的Filter。

    接下来简单介绍一下实现细节，其主要思想是将验证和权限检查分开。验证由我们自己在响应login POST请
    求的Servlet中调用req.login()函数发起，而权限检查则完全交由Tomcat容器,容器根据web应用描述符的
    约束规则进行处理。所以首先与申明式安全一样需要在服务器配置文件中对context容器进行配置，主要针对
    resource和real元素，如果要使用单点登录、登录限制等功能需要参考Tomcat文档进行适当更改。然后是对
    web应用的部署描述符web.xml进行配置，但是这里只是声明安全角色（security-role）和安全约束规则（
    security-constraint），完全不用配置验证方式元素（login-config）。因为没有设置login-config，所
    以Tomcat会在加载web应用时，使用noLoginAuthenticator。在Tomcat处理请求的过程中，总是先判断用户
    是否验证，再根据web应用部署描述符的约束规则判断该用户角色是否拥有相应访问权限。但是在用户验证的时
    候使用的noLoginAuthenticator总是会返回true，无论发起该请求的用户是否真正通过用户名和密码验证。
    然后在权限检查时，未验证的用户请求和已验证但权限不对的请求都会被拦截，然后调用sendError()设置HTTP
    响应状态码403（Forbidden）结束处理过程，开始返回。在返回过程中Tomcat会对未处理的错误响应状态进行
    处理，进入错误转发调度处理流程（req.getDispatcherType() == DispatcherType.ERROR）。接下来
    Tomcat会根据web应用部署描述符的error-page元素配置，转发请求到响应到403的error page。在此error
    page中我们需要区分未验证和已验证但权限不对的两种情况，并且只返回后一种情况的内容。而把未验证的请求
    更改响应状态码并转发到401的error page，此时一定不能再简单地使用sendError(401)来处理响应,因为
    Tomcat已经不会再处理sendError(),所以只能自己立即返回401的内容或使用我这里采用的转发方式。

    最后顺便介绍一下防CSRF的Filter。这个Filter的基本思想是使用URL重写为URL添加token来防止CSRF，具体
    实现参照了Tomcat自带的org.apache.catalina.filters.CsrfPreventionFilter。不过与CsrfPreventi-
    onFilter不同的是不需要配置保护的页面地址，而是简单地认为，所有需要用户登录的页面需要CSRF保护，不
    需要登录的页面不需要防CSRF保护，而且只要登录以后访问任意一个属于此应用的URL都需要携带token，否则
    视为CSRF攻击，返回error code 403。其次我的这个Filter并没有像CsrfPreventionFilter使用的每5次请
    求（可配置）就更改token，我是一个会话一直使用同一个token。（我不知道这样会不会带来一些风险，按道
    理来说CsrfPreventionFilter也是有风险的，只不过token变换的更频繁，风险更小而已，CSRF是最难防范的
    web攻击之一）我的Filter也同样具有一个非常明显的缺陷，就是必须使用resp.encodeRedirectURL()和
    resp.encodeURL()方法分别对重定向URL和出现在JSP页面的URL进行重写，而且静态页面里不能包含指向本应
    用的URL，否则就应该改写成JSP并对URL进行重写。
