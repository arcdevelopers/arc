import net.tsinghua.arc.util.FSURLHandler;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;

import java.io.IOException;

/**
 * Created by ji on 16-11-26.
 */
public class FSURLHandlerTest {

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, "hdfs://127.0.0.1:9000");
        String newDir = "/data/arc/photo";
        //01.检测路径是否存在 测试
        boolean result = FSURLHandler.exits(conf, newDir);
        System.out.println(result);
        if (!result) {
            FSURLHandler.createDirectory(conf, newDir);
        }
        System.out.println(FSURLHandler.exits(conf, newDir));
    }


}
