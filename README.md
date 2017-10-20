# -股票助手

使用retrofit配合rxjava进行网络请求,
根据用户输入的股票代号,在周一至周五的9:15-11:30,13:00-15:00每隔一秒获取一次当前价格,并通过Notification显示出来
并且考虑到流量问题,只有在WIFI网络下才会启动请求
            
本Demo适合初学者学习，主要有以下知识点：
* Service以及Notification的基本使用
* 判断某时间是否处于某一时间段内
* 网络的连接状态的获取
* SharedPreferences数据持久化
* 开源框架retrofit配合rxjava进行简单的网络请求
* 定时任务
