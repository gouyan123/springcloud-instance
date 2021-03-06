package com.dongnaoedu.springcloud.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.dongnaoedu.springcloud.uaa.jwt.JwtTokenProvider;
import com.dongnaoedu.springcloud.zuul.TonyConfigurationBean;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jsonwebtoken.Claims;

/**
 * 验证token。
 */
@Component
public class TokenValidataFilter extends ZuulFilter {
	protected static final Logger logger = LoggerFactory.getLogger(TokenValidataFilter.class);

	// jwt：json web token，就是生成token的一种方式；
	JwtTokenProvider jwtTokenProvider;
	// 自定义的配置
	TonyConfigurationBean tonyConfigurationBean;

	public TokenValidataFilter(JwtTokenProvider jwtTokenProvider, TonyConfigurationBean tonyConfigurationBean) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.tonyConfigurationBean = tonyConfigurationBean;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		// ctx.get("proxy")表示 routeId即路由url对应的路由id；tonyConfigurationBean是对zuul-server.yml中 tony.zuul.tokenFilter下配置内容的加载封装；
		return !tonyConfigurationBean.getNoAuthenticationRoutes().contains(ctx.get("proxy"));
	}

	/*shouldFilter()判断该filter执行后，由run()方法执行这个filter*/
	@Override
	public Object run() {
		// zuul中，将当前请求的上下文信息存在线程变量中。取出来
		RequestContext ctx = RequestContext.getCurrentContext();
		// 从上下文中获取httprequest对象
		HttpServletRequest request = ctx.getRequest();
		// 从头部信息中获取Authentication的值，也就是我们的token
		String token = request.getHeader("Authorization");
		if(token == null) {
			forbidden();
			return null;
		}
		// 检验token是否正确，这里只是通过使用key对token进行解码是否成功，并没有对有效期、以及token里面的内容进行校验。
		Claims claims = jwtTokenProvider.parseToken(token);
		if (claims == null) {
			forbidden();	
			return null;
		}
		// 可以将token内容输出出来看看
		logger.debug("当前请求的token内容是：{}", JSONObject.toJSONString(claims));
		// 塞到请求头里面，OrderController类的add(@RequestHeader(name="phone") String phone,@RequestHeader(name="email") String email)会取这 2个变量；
		// jwt相关配置在 application.yml中：token.jwt.key = 123456，这个key自己定义；
		ctx.getZuulRequestHeaders().put("phone",claims.get("phone").toString());
		ctx.getZuulRequestHeaders().put("email",claims.get("email").toString());
		return null;
	}

	// 设置response的状态码为403
	void forbidden() {
		// zuul中，将请求附带的信息存在线程变量中。
		RequestContext.getCurrentContext().setResponseStatusCode(HttpStatus.FORBIDDEN.value());
		ReflectionUtils.rethrowRuntimeException(new ZuulException("无访问权限", HttpStatus.FORBIDDEN.value(), "token校验不通过"));
	}

	@Override
	public String filterType() {
		// pre 在发起请求之前会执行这个filter
		return "pre";
	}

	@Override
	public int filterOrder() {
		// 这个是执行顺序，因为同一个类型的filter可能有多个。 值越小越靠前
		return 6;
	}

}
