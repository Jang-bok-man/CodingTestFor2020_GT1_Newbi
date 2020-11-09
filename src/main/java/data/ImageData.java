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

    public void add(int index, ImageData data) {
      if(index <0 || index >= size) {
        throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
      }

      Node newNode = new Node(data);
      Node preNode = getNode(index-1);
      newNode.nextNode = preNode.nextNode;
      preNode.nextNode = newNode;
      size++;
    }

    public void addLast(ImageData data) {
      add(size-1, data);
    }

    public ImageData removeFirst() {
      Node node           = headNode.nextNode;
      headNode.nextNode   = node.nextNode;
      ImageData result    = node.data;
      node                = null;
      size--;

      return result;
    }

    public ImageData remove(int index) {
      if(index < 0 || index >= size) {
        throw new IndexOutOfBoundsException("index:" + index + " size:" + size);
      }

      if(index == 0) {
        return removeFirst();
      } else {
        Node preNode = getNode(index-1);
        Node removeNode = preNode.nextNode;
        Node postNode = removeNode.nextNode;
        preNode.nextNode = postNode;

        ImageData result = removeNode.data;
        removeNode = null;
        size--;
        return result;
      }
    }

    public int size() { return size; }

    public String toString() {
      StringBuffer result = new StringBuffer("[");

      Node node = headNode.nextNode;
      if(node == null) {
        result.append("No Data");
      } else {
        result.append(node.data);

        node = node.nextNode;

        while(node != null) {
          result.append(", ");
          result.append(node.data);
          node = node.nextNode;
        }
      }

      result.append("]");
      return result.toString();
    }

    public void sort() {
      Node            curr = null, next = null;
      ImageData       currImgData = null, nextImgData = null;
      ImageDataHeader currImgDHdr = null, nextImgDHdr = null;

      if(size > 1) {
        for(int i = 0; i < size; i++) {
          curr = getNode(i);

          for(int j = (i + 1); j < size; j++) {
            next = getNode(j);
            currImgData = curr.data;
            nextImgData = next.data;

            currImgDHdr = currImgData.getImageDataHeader();
            nextImgDHdr = nextImgData.getImageDataHeader();

            if(currImgDHdr.getSequenceNumber() > nextImgDHdr.getSequenceNumber()) {
              //System.out.println("Curr Seq : " + currImageDataHdr.getSequenceNumber() + " Next Seq : " + nextImageDataHdr.getSequenceNumber());
              Node  temp = curr;
              curr = next;
              next = temp;

              //next.nextNode = curr.nextNode;
              //curr.nextNode = temp.nextNode;
            }
          }
        }
      }
    }

    public void reverse() {
      Node    prev = null;
      Node    curr = headNode;
      Node    next = null;

      while(curr != null) {
        next = curr.nextNode;
        curr.nextNode = prev;

        prev = curr;
        curr = next;
      }
      headNode = new Node(null);
      headNode.nextNode = prev;
    }

  public void showList() {
    Node            curr = null;
    ImageData       currImgData = null;
    ImageDataHeader currImgDHdr = null;
    if(size < 1) return ;
    curr = getNode(0);

    for(; curr != null; curr = curr.nextNode) {
      currImgData = curr.data;
      currImgDHdr = currImgData.getImageDataHeader();
      //System.out.println("Sequence Number : " + currImageDataHdr.getSequenceNumber());
    }
  }

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
