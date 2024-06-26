package cc.shunfu.bigdata.utils;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-20
 */
@Log4j2
public class ChuanUtils {

    /**
     * 以post方式调用对方接口方法
     *
     * @param pathUrl 请求地址
     */
    public static void doPost(String pathUrl, String data) {
        OutputStreamWriter out = null;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(pathUrl);

            // 打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设定请求的方法为"POST"，默认是GET
            // post与get的不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setRequestMethod("POST");

            // 设置30秒连接超时
            conn.setConnectTimeout(100000);
            // 设置30秒读取超时
            conn.setReadTimeout(100000);

            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            conn.setDoInput(true);

            // Post请求不能使用缓存
            conn.setUseCaches(false);

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive"); // 维持长链接
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // 必须要有的身份认证参数 在组织 界面 钉钉右上角 我的头像点击 后 系统管理》系统集成中有这两个字段
            conn.setRequestProperty("EngineCode", "qsvmn6vy9mh6dp1ehji15wzj3");
            conn.setRequestProperty("EngineSecret", "N7cIOvE0qjfObeCn/SdkiJvgvl9nxLwUChZqjEbJ+3UXU7sBiYcBwA==");

            // 连接，从上述url.openConnection()至此的配置必须要在connect之前完成，
            conn.connect();

            /**
             * 下面的三句代码，就是调用第三方http接口
             */
            // 获取URLConnection对象对应的输出流
            // 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，所以在开发中不调用上述的connect()也可以)。
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数即数据
            out.write(data);
            // flush输出流的缓冲
            out.flush();

            /**
             * 下面的代码相当于，获取调用第三方http接口后返回的结果
             */
            // 获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            StringBuilder json = new StringBuilder();
            // 构造一个字符流缓存
            br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                result += str + "\r\n";
                ;
                json.append(str + "\r\n");
            }
            while ((str = br.readLine()) != null) {

            }
            // 关闭流
            is.close();
            // 断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();

        } catch (Exception e) {
            log.error("调用第三方接口异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                log.error("关闭流异常：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 以get方式调用对方接口方法
     *
     * @param pathUrl 请求地址
     */
    public static void doGet(String pathUrl) {
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(pathUrl);

            // 打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设定请求的方法为"GET"，默认是GET
            // post与get的不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setRequestMethod("GET");

            // 设置30秒连接超时
            conn.setConnectTimeout(30000);
            // 设置30秒读取超时
            conn.setReadTimeout(30000);

            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            conn.setDoInput(true);

            // Post请求不能使用缓存(get可以不使用)
            conn.setUseCaches(false);

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive"); // 维持长链接
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 必须要有的身份认证参数 在组织 界面 钉钉右上角 我的头像点击 后 系统管理》系统集成中有这两个字段
            conn.setRequestProperty("EngineCode", "xxx");
            conn.setRequestProperty("EngineSecret", "xxx");
            // 连接，从上述url.openConnection()至此的配置必须要在connect之前完成，
            conn.connect();

            /**
             * 下面的代码相当于，获取调用第三方http接口后返回的结果
             */
            // 获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            // 构造一个字符流缓存
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str = "";
            while ((str = br.readLine()) != null) {
                result += str;
            }
            System.out.println(result);
            // 关闭流
            is.close();
            // 断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
