# springsecurityjwt
springboot + spring security + JWT 进行登录验证和动态权限管理

springsecurity 分2部分，登录验证和权限管理，在登录验证成功之后会在 SecurityContextHolder 上线文中添加 Authentication 对象，包含用户信息和角色信息，用于权限管理和系统验证用户信息保存。

登录验证

验证相关类
AbstractAuthenticationProcessingFilter 信息验证抽象类

UsernamePasswordAuthenticationFilter  登录信息验证拦截器，默认URL验证 new AntPathRequestMatcher("/login", "POST")，会对 /login 请求进行拦截验证
           AuthenticationManager 验证管理类（接口），验证用户
                   authenticate 验证传入用户信息与

AbstractUserDetailsAuthenticationProvider 验证抽象类
           DaoAuthenticationProvider 实现类，从数据库获取信息
           UserDetailsService 调用该接口的实现类，通过登录帐号获取账户信息

 验证的3种实现方式
          1. 使用默认的认证方式
                  formLogin() 基于表单登录
                  loginPage() 登录页
                  defaultSuccessUrl  登录成功后的默认处理页
                  failuerHandler登录失败之后的处理器
                  successHandler登录成功之后的处理器
                  failuerUrl登录失败之后系统转向的url，默认是this.loginPage + "?error"

        配置：http.requestMatchers().antMatchers("/api/**")
        .and().authorizeRequests().antMatchers("/user","/api/user").authenticated()
        .anyRequest().authenticated()
        .and().formLogin().loginPage("/login");
        
           2. 自定义验证拦截器
             参考：https://www.cnblogs.com/lori/p/10400564.html
   
           3. 自定义登录验证，将登录验证器中的验证方法取出，添加到自己的验证逻辑中，本人使用该种方式实现

    //用户验证
	final Authentication authentication = authenticate(username, password);
	//存储认证信息
	SecurityContextHolder.getContext().setAuthentication(authentication);
	
	private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new RuntimeException("用户名或密码无效");
        }
    }
权限管理

       拦截器 AbstractSecurityInterceptor 实现权限管理，由 FilterInvocationSecurityMetadataSource 的 getAttributes 方法获取所有资源与角色的信息，然后过滤出当前资源的角色，并返回给权限管理器 AccessDecisionManager 使用，权限管理器获取 SecurityContextHolder 上下文中保存的用户和角色信息，判断用户是否有操作该资源的权限。权限足够则直接返回，权限不够则提示对应异常。交易异常处理器处理。

 

遇到的坑及处理方法

使用自定义JWT-TOKEN过滤器JwtAuthenticationTokenFilter之后，该过滤器会忽略  web.ignoring() 和 permitAll 的配置，因为要实现token保存在cookie的方式，导致登录时不走帐号、密码验证而是走了token验证。
   解决方法：添加 AntPathMatcher antPathMatcher = new AntPathMatcher(); 配置解析器，对不需要进行token验证的请求进行过滤
   if(antPathMatcher.match("/auth/*", httpServletRequest.getRequestURI())){
        // 过滤不需要通过自定义过滤器的链接
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        return ;
    }
    

  添加使用了注解 @Component 的自定义权限过滤器 MyFilterSecurityInterceptor 之后，该过滤器会忽略  web.ignoring() 和 permitAll 的配置。
    解决方法：去掉注解 @Component,使用new的方式创建对象添加
    httpSecurity.addFilterBefore(new MyFilterSecurityInterceptor(myInvocationSecurityMetadataSourceService, myAccessDecisionManager), FilterSecurityInterceptor.class);
