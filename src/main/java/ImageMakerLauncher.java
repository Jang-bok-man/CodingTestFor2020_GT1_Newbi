/**
 * Created by user on 2020-11-02.
 */
import data.*;
import application.*;
import sun.font.AttributeMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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
    ImageData         id = null, ImgD = null;
    int               size = 0, i;
    ImageDataHeader   ImgDHdr = null;
    int               ImgWidth = 0, ImgHeight = 0;

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

    Map<Integer, ImageData> testMap = new HashMap<Integer, ImageData>();
    for(i = 0; i < size; i++) {
      ImgD = id.get(i);
      ImgDHdr = ImgD.getImageDataHeader();
      testMap.put((ImgDHdr.getSequenceNumber()), id.get(i));
    }

    Object[] mapKey = testMap.keySet().toArray();
    Arrays.sort(mapKey);

    for(Integer nKey : testMap.keySet()) {
      ImageData   imgData = testMap.get(nKey);
      ImgDHdr = imgData.getImageDataHeader();
      if(ImgWidth == 0) {
        ImgWidth = ImgDHdr.getDataLength();
      }
      ImgHeight++;
      //System.out.println(iIdh.getSequenceNumber());
   }

    System.out.format("Width %d / Height %d\n", ImgWidth, ImgHeight);

    //write result (bmp, jpg, png or etc.)
    i = 0;
    BufferedImage   img = new BufferedImage((ImgWidth / 4), ImgHeight, BufferedImage.TYPE_INT_RGB);
    File            ImgFile = new File("result.bmp");
    for(Integer nKey : testMap.keySet()) {
      ImgD = testMap.get(nKey);
      ImgDHdr = ImgD.getImageDataHeader();

      int[] iData = ImgD.getImageData();
      //System.out.format("IData.Length %d Seq %d Len %d\n", iData.length, ImgDHdr.getSequenceNumber(), ImgDHdr.getDataLength());
      for(int j = 0; j < iData.length; j++) {
        img.setRGB(j, i, iData[j]);
          //System.out.format("%08x", iData[j]);
      }
        //System.out.format("\n");
      i++;
    }
    ImageIO.write(img, "bmp", ImgFile);
    //System.out.println("Write Complete!!!!");
  }

  private static byte[] intToByteArray(final int integer) {
    ByteBuffer  buff = ByteBuffer.allocate(Integer.SIZE / 8);
    buff.putInt(integer);
    buff.order(ByteOrder.LITTLE_ENDIAN);

    return buff.array();
  }
}
