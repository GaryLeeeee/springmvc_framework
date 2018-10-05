# springmvc_framework
一个原生的Spring MVC框架，本来想封装好的，但前面有些代码耦合性比较高，所以将会在后面新做一个项目，其中的Servlet部分将交给HttpServlet处理，当前的server部分是用原生代码写的。


## 已经实现的功能
* 注解实现(@Controller,@ResponseBody,@RequestMapping)
* 路由跳转，当没有@ResponseBody，则默认return的是String，并解析为templates
* 反射实现方法参数注入(invoke方法)
* 判断并返回静态文件，统一放在static包

## 待实现或优化的功能
* 文件上传
* 参数注入类型(目前只支持String)
* 完善404处理
* 其他

