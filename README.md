# sun 作者来源：
source for [jakob Henner](https://dribbble.com/pattern)
weather app for ios:[sun](http://pattern.dk/sun/)

一直想做一些很cool的事情，喜欢看一些精美的应用，小巧精美，尤其在交互上更加吸引人，外加看**《第一行代码》**的时候，巧好是一个关于天气 的应用，
所以心血来潮就进行实现了下。

后续效果继续补上。

下面是效果图：

![](https://o1whyeemo.qnssl.com/image/view/app_screenshots/9e876c8aa7c9bd7bfccf60c045a7af53/528)

应用下载链接：
[sun](https://www.pgyer.com/T_sun)

### 相关技术点
- 无线循环`viewpager`,使用`Integer.MAX`的方式,当运动`<3`的情况，就补充到`>3`的情况
- 使用《app研发录》推荐的用线程池封装网络底层(`ThreadPollExcutor+Runnable+Handler`)，来处理网络请求
- 封装自己的类库，有利于以后其他项目的移植
