/**
 * Created by user on 2020-11-02.
 */
import data.*;
import application.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ImageMakerLauncher {
  public static void main(String[] args) throws Exception {
    //implement code
    String cur_dir        = System.getProperty("user.dir");
    cur_dir               += "/src/main/resources/sample";

    //System.out.println("CURR : " + cur_dir);
    ImageDataReader   idr = new ImageDataReader();
    ImageData         id = null, iNode = null, jNode = null;
    int               size = 0, i, TotalDataLen = 0, offset = 0;
    ImageDataHeader   iIdh = null, jIdh = null;
    ImageData         temp = null;
    byte[]            byteArray;

    //read file
    try {
      id = idr.readImageDataFile(cur_dir);
    } catch(IOException e) {
      e.printStackTrace();
    }

    //processing
    //id.sort();
    //id.reverse();
    size = id.size();

    //byteArray = new byte[TotalDataLen];
    Map<Integer, ImageData> testMap = new HashMap<Integer, ImageData>();
    for(i = 0; i < size; i++) {
      iNode = id.get(i);
      iIdh = iNode.getImageDataHeader();
      testMap.put((iIdh.getSequenceNumber() + 1), id.get(i));
    }

    Object[] mapKey = testMap.keySet().toArray();
    Arrays.sort(mapKey);

    for(Integer nKey : testMap.keySet()) {
      ImageData   imgData = testMap.get(nKey);
      iIdh = imgData.getImageDataHeader();
      TotalDataLen += iIdh.getDataLength();
      //System.out.println(iIdh.getSequenceNumber());
   }

    byteArray = new byte[TotalDataLen];
    for(i = 0; i < TotalDataLen; i++) {
      byteArray[i] = (byte)0x00;
    }

    byte[] fByte = null;
    for(Integer nKey : testMap.keySet()) {
      ImageData   imgData = testMap.get(nKey);
      iIdh = imgData.getImageDataHeader();

      int[] iData = imgData.getImageData();

      for(int j = 0; j < iData.length; j++) {
        fByte = new byte[4];

        fByte[0] = (byte)0x00;
        fByte[1] = (byte)0x00;
        fByte[2] = (byte)0x00;
        fByte[3] = (byte)0x00;

        fByte = intToByteArray(iData[j]);

        byteArray[offset++] = fByte[0];
        byteArray[offset++] = fByte[1];
        byteArray[offset++] = fByte[2];
        byteArray[offset++] = fByte[3];

        System.out.format("Byte[0] %02x Byte[1] %02x Byte[2] %02x Byte[3] %02x\n",
                fByte[0], fByte[1], fByte[2], fByte[3]);
      }

      //System.out.format("Byte[0] %02x Byte[1] %02x Byte[2] %02x Byte[3] %02x\n",
      //        fByte[0], fByte[1], fByte[2], fByte[3]);
    }
    //write result (bmp, jpg, png or etc.)

    String  resFile = cur_dir + "/result";
    byteArrayConvertImageFile(byteArray, resFile);
  }

  private static byte[] intToByteArray(final int integer) {
    ByteBuffer  buff = ByteBuffer.allocate(Integer.SIZE / 8);
    buff.putInt(integer);
    buff.order(ByteOrder.LITTLE_ENDIAN);

    return buff.array();
  }

  public static void byteArrayConvertImageFile(byte[] imageByte, String bmpFile) {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByte);

      try {
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        if(bufferedImage == null) {
          System.out.println("Hello~~~~~~~~~~~~~~~~~~~~~~~");
        }
        ImageIO.write(bufferedImage, "bmp", new File("result.bmp"));
      } catch(Exception e) {
        e.printStackTrace();
        //e.getStackTrace();
      }
  }
}
