import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by ji on 16-11-16.
 */
public class HttpUtil {
    public static void main(String[] args) throws IOException {
        String URL_STR = "http://127.0.0.1:8080/arc/user/register";
        URL_STR = "http://127.0.0.1:8080/arc/plan/addEvidence";
        String filePath = "/home/ji/图片/background/idea.jpg";

        CloseableHttpClient httpClient = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setStaleConnectionCheckEnabled(true)
                .build();


        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        HttpPost httpPost = new HttpPost(URL_STR);

        FileBody bin = new FileBody(new File(filePath));

//
//        JSONObject message = new JSONObject();
//        message.put("userName", "ji");
//        message.put("password", "123");
//        StringBody data = new StringBody(message.toJSONString(), ContentType.create("text/plain", Consts.UTF_8));
//        HttpEntity reqEntity = MultipartEntityBuilder.create()
//                .addPart("message", data)
//                .addPart("file", bin).build();




        StringBody data = new StringBody("1", ContentType.create("text/plain", Consts.UTF_8));
        StringBody comment = new StringBody("测试评论", ContentType.create("text/plain", Consts.UTF_8));
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("planItemId", data)
                .addPart("comment",comment)
                .addPart("file", bin).build();

        httpPost.setEntity(reqEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            String content = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
            System.out.println(content);
            JSONObject resultObj = JSON.parseObject(content);
            if (resultObj.getInteger("code") == 0) {

            }
        }
    }
}
