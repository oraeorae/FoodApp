# food
【SpringBoot】——美食app后端


项目简介：本APP为消费者推荐周边的美食店铺及其相关信息，同时也让卖家更全面便捷地推销自己的店铺。

技术栈：SpringBoot，Mybaits，Redis，SpringSecruity

项目亮点：

	( 1 ) 使用Redis自带的GEO功能实现附近的人功能；
  
	( 2 ) 使用基于用户的协同过滤算法，进行用户店铺的个性化推荐；
  
	( 3 ) 使用SpringSecruity进行登录的认证和授权，并使用BCryptPasswordEncoder进行密码加密，提高接口和数据的安全性。
  
	( 4 ) 使用基于令牌桶算法的RateLimiter类实现流量限制，并实现注解AOP，来进行接口限流；
  
	( 5 ) 数据库使用Mycat实现分库分表，以及两主两从读写分离；
  
	( 6 ) 使用Canal结合Rabbitmq实现数据库和缓存的同步，保证数据的一致性；
