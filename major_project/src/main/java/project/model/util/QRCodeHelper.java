package project.model.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * The util helps to generate QR code
 */
public class QRCodeHelper {
    /**
     * Generate bit matrix for the content
     * @param content The content to embed
     * @param width The width of the matrix
     * @param height The height of the matrix
     * @return The bit matrix
     */
    public static BitMatrix generateQRCodeStream(String content,int width,int height){
        Map<EncodeHintType,Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        hints.put(EncodeHintType.MARGIN,0);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q); //Fault tolerance level-> L、M、Q、H ->High with less information
        BitMatrix bitMatrix;
        try {
            //Parameters: encoding content, encoding type, width and height of the generated picture, setting parameters
            //Both height and width are in pixels
            //Normally, content is a URL. After scanning, it can automatically jump to the specified address
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }


    /**
     * Generate the jpg image and return a base64 encoded format image string
     * @param content The content to embed into the QR code
     * @param size The size of the QR code image
     * @param path File path
     */
    public static String generateQRCodeImage(String content, int size, Path path){
        InputStream in = null;
        byte[] data = null;
        String encodedImage = null;
        try {
            BitMatrix bitMatrix = generateQRCodeStream(content,size,size);
            MatrixToImageWriter.writeToPath(bitMatrix,"jpg", path);

            //Get the 64base encoded image
            File file = path.toFile();
            in = new FileInputStream(file.getPath());
            data = new byte[in.available()];
            in.read(data);
            in.close();
            encodedImage = Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedImage;
    }
}
