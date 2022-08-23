/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.captcha;

import cn.apiclub.captcha.Captcha;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class CaptchaUtils {

    public static String encodeBase64(Captcha captcha){
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(captcha.getImage(),"png",outputStream);
            return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
