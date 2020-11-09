package data;

/**
 * Created by user on 2020-11-02.
 */
public class ImageDataHeader {
  private final int sequenceNumber;
  private final int dataLength;

  public ImageDataHeader(int sequenceNumber, int dataLength) {
    this.sequenceNumber = sequenceNumber;
    this.dataLength = dataLength;
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public int getDataLength() {
    return dataLength;
  }
}
