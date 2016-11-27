package net.tsinghua.arc.controller;

import com.alibaba.fastjson.JSONObject;
import net.tsinghua.arc.domain.User;
import net.tsinghua.arc.service.UserService;
import net.tsinghua.arc.util.FSURLHandler;
import net.tsinghua.arc.util.ImageUtil;
import net.tsinghua.arc.util.PageResult;
import net.tsinghua.arc.util.RequestUtil;
import net.tsinghua.arc.web.ResponseCodeConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ji on 16-11-15.
 */
@Controller
@RequestMapping("/arc/user/")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("register")
    public JSONObject addUser(String message, MultipartFile file) {
        PageResult result = new PageResult();
        try {
            User user = (User) RequestUtil.toClassBean(message, User.class);
            userService.addUser(user);

            String fileName = user.getId() + ".jpeg";
            String url = "/media/ji/document/school/arc/photo/" + fileName;
            String hdfsUrl = FSURLHandler.PHOTO_HDFS_PATH + fileName;
            ImageUtil.makeSmallImage(file.getInputStream(), url, hdfsUrl);
            user.setAvatar(hdfsUrl);
            userService.updateImageUrl(user);
            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("addUser Exception", e);
        }
        return result.toJson();
    }

    /**
     * 上传头像
     *
     * @param userId
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("uploadPhoto")
    public JSONObject uploadPhoto(Integer userId, MultipartFile file) {
        PageResult result = new PageResult();
        try {
            if (userId == null) {
                throw new RuntimeException("userId不能为空");
            }
            User user = new User();
            user.setId(userId);
            String fileName = user.getId() + ".jpeg";
            String tmpUrl = "/media/ji/document/school/arc/photo/" + fileName;
            String hdfsUrl = FSURLHandler.PHOTO_HDFS_PATH + fileName;
            ImageUtil.makeSmallImage(file.getInputStream(), tmpUrl, hdfsUrl);
            user.setAvatar(hdfsUrl);
            userService.updateImageUrl(user);
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("uploadPhoto Exception", e);
        }
        return result.toJson();
    }

    /**
     * 登录
     *
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping("login")
    public JSONObject login(String message) {
        PageResult result = new PageResult();
        try {
            User user = (User) RequestUtil.toClassBean(message, User.class);
            if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
                result.setCode(ResponseCodeConstants.PARAM_ERROR_CODE);
                result.setMessage("用户名和密码不能为空");
            } else {
                User useDb = userService.queryByNameAndPwd(user);
                if (useDb == null) {
                    result.setCode(ResponseCodeConstants.PARAM_ERROR_CODE);
                    result.setMessage("登录失败");
                } else {
                    result.setCode(ResponseCodeConstants.SUCCESS_CODE);
                    result.setObj(useDb);
                }
            }
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("login Exception", e);
        }
        return result.toJson();
    }

    /**
     * 获取用户头像
     *
     * @param url
     * @param response
     */
    @RequestMapping("getImage")
    public void getImage(String url, HttpServletResponse response) {
        try {
            response.setContentType("image/png");

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            //本地存储
//            BufferedInputStream in = new BufferedInputStream(new FileInputStream(url));
//            int i;
//            while ((i = in.read()) != -1) {
//                toClient.write(i);
//            }

            FileSystem fs = FileSystem.get(FSURLHandler.getConfiguration());
            Path path = new Path(url);
            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = null;
            try {
                inputStream = fs.open(path);
                BufferedInputStream in = new BufferedInputStream(inputStream);
                int i;
                while ((i = in.read()) != -1) {
                    toClient.write(i);
                }
                toClient.flush();
            } finally {
                IOUtils.closeStream(inputStream);
                IOUtils.closeStream(outputStream);
                fs.close();
            }
        } catch (Exception e) {
            LOGGER.error("geImage error", e);
        }
    }

    @ResponseBody
    @RequestMapping("geBalance")
    public JSONObject getBalance(Integer userId) {
        PageResult result = new PageResult();
        try {
            User user = userService.queryById(userId);
            if (user != null) {
                result.setObj(user);
            }
            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (Exception e) {
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            LOGGER.error("getBalance error", e);
        }
        return result.toJson();
    }
}
