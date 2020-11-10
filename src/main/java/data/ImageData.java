package data;

import com.esotericsoftware.kryo.io.Input;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by user on 2020-11-02.
 */
public class ImageData {
  private Node headNode;
  public static final int SYNC = 0x1ACFFC1D;
  private final ImageDataHeader imageDataHeader;
  private final int[] imageData;
  private int size;

  public ImageData(ImageDataHeader imageDataHeader, int[] imageData) {
    this.imageDataHeader = imageDataHeader;
    this.imageData = imageData;
  }

  public int[] getImageData() { return imageData; }
  public ImageDataHeader getImageDataHeader() { return imageDataHeader; }

    public ImageData() {
      imageDataHeader = null;
      imageData       = null;
      headNode        = new Node();
      size            = 0;
    }

    public void addFirst(ImageData data) {
      Node newNode = new Node(data);
      newNode.nextNode = headNode.nextNode;
      headNode.nextNode = newNode;

      size++;
    }

    public ImageData get(int index) { return getNode(index).data; }
    private Node getNode(int index) {
      if (index < 0 || index >= size) {
        throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
      } else {
        Node node = headNode.nextNode;
        for(int i=0; i < index; i++) {
          node = node.nextNode;
        }

        return node;
      }
    }

    public int size() { return size; }
    private class Node {
      private Node nextNode;
      private ImageData data;

      Node() { }

      Node(ImageData data) {
        this.nextNode = null;
        this.data = data;
      }
    }
}
