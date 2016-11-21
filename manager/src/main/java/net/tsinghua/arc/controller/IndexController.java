package net.tsinghua.arc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Created by ji on 15-10-15.
 */
@Controller
@RequestMapping("/index/")
public class IndexController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("download")
    public void testDownload(HttpServletResponse response) {
        try {
            response.setContentType("image/png");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            BufferedInputStream in = new BufferedInputStream(new FileInputStream("/home/ji/图片/background/idea.jpg"));
            int i;
            while ((i = in.read()) != -1) {
                toClient.write(i);
            }
            toClient.flush();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }


}
