package lc.activiti.filter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import lc.activiti.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("-------->>>>Request Begin>>>>---------->");
        long startTime = System.currentTimeMillis();
        // 防止流读取一次后就没有了, 所以需要将流继续写出去
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        ServletRequest requestWrapper = new LCHttpServletRequestWrapper(httpServletRequest);
        log.info("RemoteHost={}", httpServletRequest.getRemoteHost());
        String method = httpServletRequest.getMethod();
        log.info("Method={}", method);
        String url = httpServletRequest.getRequestURL().toString();
        log.info("URL={}", url);
            /*//过滤，传图片的直接放过！！！
            for (String s : noNeedToFilt) {
	            if (-1 != url.indexOf(s)) {
	                log.info("属于信任url直接放过");
	                log.info("<--------<<<<Request End<<<<----------");
	                chain.doFilter(request, response);
	                return;
	            }
	        }*/
        String queryString = httpServletRequest.getQueryString();
        log.info("QueryString={}", queryString);
        Map<String, String> headersInfo = HttpUtils.getHeadersInfo(httpServletRequest);
        log.info("Headers={}", headersInfo);
        String json = HttpUtils.getBodyString(requestWrapper);
        log.info("Body={}", json);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String remoteHost = httpServletRequest.getRemoteHost();
        String remoteXRealHost = httpServletRequest.getHeader("x-real-ip");
        log.info("请求IP: remoteHost={},remoteXRealHost={}", remoteHost, remoteXRealHost);


        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
        //处理业务
        chain.doFilter(requestWrapper, responseWrapper);

        long endTime = System.currentTimeMillis();
        // 获取response返回的内容并重新写入response
        String result = responseWrapper.getResponseData(response.getCharacterEncoding());
        response.getOutputStream().write(result.getBytes());

        String uri = httpServletRequest.getRequestURI();
        log.info("URL={}", url);
        log.info("ResponseCode={}", String.valueOf(responseWrapper.getStatus()));
        log.info("ResponseString={}", result);
        log.info("TimeConsuming={}", (int) (endTime - startTime));

        log.info("<--------<<<<Request End<<<<----------");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}

class LCHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public LCHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = HttpUtils.getBodyString(request).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}

class ResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream buffer = null;

    private ServletOutputStream out = null;

    private PrintWriter writer = null;


    public ResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);

        buffer = new ByteArrayOutputStream();
        out = new WapperedOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer, "UTF-8"));
    }

    //重载父类获取outputstream的方法
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (out != null) {
            out.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    public String getResponseData(String charset) throws IOException {
        flushBuffer();//将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据
        byte[] bytes = buffer.toByteArray();
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }

    }


}
//内部类，对ServletOutputStream进行包装，指定输出流的输出端

class WapperedOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream bos = null;

    public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException {
        bos = stream;
    }

    //将指定字节写入输出流bos
    @Override
    public void write(int b) throws IOException {
        bos.write(b);
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setWriteListener(WriteListener listener) {
        // TODO Auto-generated method stub

    }
}

