package com.example.spring_food.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.spring_food.aop.Limit.LimitType;
import com.example.spring_food.aop.Limit.annotation.RedisLimit;
import com.example.spring_food.utils.StatusCode;
import com.example.spring_food.aop.Limit.LimitType;
import com.example.spring_food.aop.Limit.annotation.RedisLimit;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * 学习链接：https://blog.csdn.net/qq_34217386/article/details/122100904
 * */
@Slf4j
@Aspect
@Configuration
public class RedisLimitAspect {

    @Resource
    private  RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(com.example.spring_food.aop.Limit.annotation.RedisLimit)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        RedisLimit annotation = method.getAnnotation(RedisLimit.class);
        LimitType limitType = annotation.limitType();

        if( annotation != null ){
            String name = annotation.name();
            String key;

            int period = annotation.period();
            int count = annotation.count();

            switch (limitType){
                case IP:
                    key = getIpAddress();
                    break;
                case CUSTOMER:
                    key = annotation.key();
                    break;
                default:
                    key = StringUtils.upperCase(method.getName());
            }
            ImmutableList<String> keys = ImmutableList.of(StringUtils.join(annotation.prefix(), key));
            try {
                String luaScript = buildLuaScript();
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>(luaScript, Long.class);
                redisTemplate.setEnableTransactionSupport( true );
                Long number = (Long) redisTemplate.execute(redisScript, keys, count, period);
                log.info("Access try count is {} for name = {} and key = {}", number, name, key);
                if(number != null && number.intValue() == 1) {
                    return pjp.proceed();
                }
                responseFail(annotation.msg());
                //throw new Exception(annotation.msg());
            }catch (Exception e){
                log.debug("令牌桶={}，获取令牌失败",key);
                responseFail(e.getLocalizedMessage());
                //throw new Exception(e.getLocalizedMessage());
                e.printStackTrace();
                //throw new RuntimeException("服务器异常");
            }
        }
        return false;
    }

    public String buildLuaScript(){
        return "redis.replicate_commands(); local listLen,time" +
                "\nlistLen = redis.call('LLEN', KEYS[1])" +
                // 不超过最大值，则直接写入时间
                "\nif listLen and tonumber(listLen) < tonumber(ARGV[1]) then" +
                "\nlocal a = redis.call('TIME');" +
                "\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])" +
                "\nelse" +
                // 取出现存的最早的那个时间，和当前时间比较，看是小于时间间隔
                "\ntime = redis.call('LINDEX', KEYS[1], -1)" +
                "\nlocal a = redis.call('TIME');" +
                "\nif a[1]*1000000+a[2] - time < tonumber(ARGV[2])*1000000 then" +
                // 访问频率超过了限制，返回0表示失败
                "\nreturn 0;" +
                "\nelse" +
                "\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])" +
                "\nredis.call('LTRIM', KEYS[1], 0, tonumber(ARGV[1])-1)" +
                "\nend" +
                "\nend" +
                "\nreturn 1;";
    }

    public String getIpAddress(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 直接向前端抛出异常
     * @param msg 提示信息
     */
    private void responseFail(String msg) throws IOException {
        HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Map<String, Object> map = StatusCode.error(5001,msg);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        //logger.error("IP为"+ IPUtils.getIpAddress(request)+"：访问频率过高");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject(map);
        out.append(json.toString());
        out.close();

    }
}
