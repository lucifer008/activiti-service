package lc.activiti.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lc.activiti.lcenum.HttpRequestStatus;
import lc.activiti.lcenum.Result;
import lc.activiti.utils.DesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 郝伟
 * Date: 2019/12/10
 * Description:token签名认证
 */
@Slf4j
@Component
public class CheckFilter extends  OncePerRequestFilter{

    final static List<String> nonFilterKeys=new ArrayList<>();
    static {
        nonFilterKeys.add("/lcActivitiService/test/test");
        nonFilterKeys.add("/lcActivitiService/test/testHelloWord");
        nonFilterKeys.add("/lcActivitiService/test/getJson");
        nonFilterKeys.add("/lcActivitiService/test/testAsync");
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${lc.headerString}")
    public    String headerString;
    @Value("${lc.secretKey}")
    public      String  secretKey;
    @Value("${lc.originalWord}")
    public      String  originalWord;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
            chain.doFilter(request,response);
//        try {
//            String uri = request.getRequestURI();
//            //部分路径不校验，直接跳过  比如登录
//            if(nonFilterKeys.contains(uri)){
//                chain.doFilter(request,response);
//                return;
//            }
//            String header = request.getHeader(headerString);
//            String method = request.getMethod();
//            String key = DesUtils.encrypt(secretKey, originalWord);
//            String token="";
//            if("GET".equals(method)){
//                 token = verificationSign(request);
//            }else if("POST".equals(method)){
//                StringBuilder sb = new StringBuilder();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//                reader.close();
//                Map<String,Object> jsonObject =(Map<String,Object>) com.alibaba.fastjson.JSONObject.parse(sb.toString());
//                jsonObject.put("routing",request.getRequestURI());
//                 token = createSign(jsonObject);
//            }
//            if(header.startsWith(key)){
//                String check = header.substring(key.length());
//                if(StringUtils.isBlank(check))  throw  new Exception("token认证无效!");
//                if(check.equals(token)){
//                    chain.doFilter(request,response);
//                }else{
//                   throw  new Exception("token认证无效!");
//
//                }
//            }
//        } catch (Exception ex) {
//            log.error("【无效的Token】",ex.getMessage());
//            response.setContentType("application/json;charset=utf-8");
////            response.setStatus(HttpRequestStatus.Failed.getStatus());
//            Result<Object> result=new Result<>();
//            result.setT(ex.getMessage());
//            result.setMessage("授权失败");
//            result.setStatus(HttpRequestStatus.Failed.getStatus());
//            String resJson= null;
//            try {
//                resJson = objectMapper.writeValueAsString(result);
//                response.getWriter().println(resJson);
//            } catch (Exception e) {
//                e.printStackTrace();
//                log.error("异常--->{}",e);
//            }
//        }

    }

    private String verificationSign(HttpServletRequest request ) {
        Enumeration<?> pNames = request.getParameterNames();
        Map<String, Object> params = new HashMap<String, Object>();
        while (pNames.hasMoreElements()) {
            String pName = (String) pNames.nextElement();
            Object pValue = request.getParameter(pName);
            params.put(pName, pValue);
        }
        params.put("routing",request.getRequestURI());
        String sign = createSign(params);
        return sign;
    }

    private String createSign(Map<String, Object> params ){
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuilder temp = new StringBuilder();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {

                valueString = String.valueOf(value);
            }
            temp.append(valueString);
        }
        return DesUtils.encrypt(secretKey,temp.toString());
    }


}
