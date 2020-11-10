package application;

import data.ImageData;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import data.ImageDataHeader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by user on 2020-11-02.
 */
public class ImageDataReader {
  public static int byteToint(byte[] arr) {
    return (arr[3] & 0xff) << 24 | (arr[2] & 0xff) << 16 | (arr[1] & 0xff) << 8 | (arr[0] & 0xff);
  }

  public static ImageData readImageDataFile(String filePath) throws IOException {
    //implement code

    ImageData     id = new ImageData();
    ImageData     tempId;
    File          dir = new File(filePath);
    File          []fileList = dir.listFiles();
    int           count = 0, sync = 0, sequenceNumber = 0, dataLength = 0, offset = 0;
    long          filesize = 0;
    String        FullFilePath;

    for(int i = 0; i < fileList.length; i++) {
      File		file = fileList[i];

      if(file.isFile()) {
        filesize = file.length();

        if(filesize < 12) continue;
        FullFilePath          = filePath + "/" + file.getName();
        DataInputStream in    = new DataInputStream(new FileInputStream(FullFilePath));
        byte      []fmtHeader = new byte[(int)filesize];

        while(in.read(fmtHeader) > 0) {
          ;
        }
        sync = byteToint(fmtHeader);
        if(id.SYNC != sync) continue;

        sequenceNumber = ((fmtHeader[7] & 0xff) << 24) | ((fmtHeader[6] & 0xff) << 16) | ((fmtHeader[5] & 0xff) << 8) | ((fmtHeader[4] & 0xff));
        dataLength = ((fmtHeader[11] & 0xff) << 24) | ((fmtHeader[10] & 0xff) << 16) | ((fmtHeader[9] & 0xff) << 8) | ((fmtHeader[8] & 0xff));

        if(dataLength + 12 != filesize) continue;

        int []imageData = new int[dataLength / 4];
        byte []fByte = new byte[4];
        System.out.println("FileName :" + file.getName() + " DataLen :" + dataLength + " ImageDataLen :" + (dataLength/4));

        for(int Offset = 0, Idx = 12; Idx < (dataLength + 12); Offset++, Idx += 4) {
          imageData[Offset] = 0;

          fByte[0] = (byte)0x00;
          fByte[1] = (byte)0x00;
          fByte[2] = (byte)0x00;
          fByte[3] = (byte)0x00;

          fByte[0] = fmtHeader[Idx];
          fByte[1] = fmtHeader[Idx + 1];
          fByte[2] = fmtHeader[Idx + 2];
          fByte[3] = fmtHeader[Idx + 3];

          imageData[Offset] = readDataByLittleEndianToInt(fByte);
          //System.out.format("ImageData[%d] 0x%08x\n", Offset, imageData[Offset]);
        }

        ImageDataHeader idh = new ImageDataHeader(sequenceNumber, dataLength);

        tempId = new ImageData(idh, imageData);
        id.addFirst(tempId);

        //System.out.println("File :" + file.getName() + " File Size:" + filesize);
        //System.out.format(" Sync 0x%08x SeqNum %d DataLen %d\n",  sync , seqNum, +dataLen);
        in.close();
      }
    }

    return id;
    //throw new NotImplementedException();
  }

  private static int readDataByLittleEndianToInt(byte[] readData) {
    final int size = Integer.SIZE / 8;
    ByteBuffer buff = ByteBuffer.allocate(size);
    final byte[]newBytes  = new byte[size];

    for(int i = 0; i < size; i++) {
      if(i + readData.length < size) {
        newBytes[i] = (byte)0x00;
      } else {
        newBytes[i] = readData[i + readData.length - size];
      }
    }

    buff = ByteBuffer.wrap(newBytes);
    buff.order(ByteOrder.LITTLE_ENDIAN);

    return buff.getInt();
  }
}