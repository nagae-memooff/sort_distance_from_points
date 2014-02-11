import java.util.ArrayList;
import java.io.*;

public class SortDistance {
  private static String CONFIG_FILE_PATH = "./points";

  public static void main(String[] args){
    SortDistance sd = new SortDistance();
    PointList points = PointList.generatePointsListByFile(CONFIG_FILE_PATH);
    DistanceTable distanceTable = DistanceTable.generateDistanceList(points);
    DistanceTable distanceTable2 = (DistanceTable) distanceTable.clone();

    long quickSortExecTime = quickSortDistanceTable(distanceTable, 0, distanceTable.size() - 1);
    long bubbleSortExecTime = bubbleSortDistanceTable(distanceTable2);

    System.err.println("快速排序执行时间：" + quickSortExecTime + "\n");
    System.err.println("冒泡排序执行时间：" + bubbleSortExecTime + "\n");

    distanceTable.print();
    distanceTable2.print();
  }

  //  根据题目推测排序量会大，因此使用快速排序。
  public static long quickSortDistanceTable(DistanceTable a, int left, int right) {
    long startAt = System.currentTimeMillis();
    int i, j;
    DistanceRecord temp;

    i = left;
    j = right;
    if (left < right) {
      temp = a.get(left);
      while (i < j)
      {
        while (a.get(j).distance >= temp.distance && j > i) {
          j--;
        }
        if (j > i) {
          a.set(i, a.get(j));
          i++;
        }
        while (a.get(i).distance <= temp.distance && j > i) {
          i++;
        }
        if (j > i){
          a.set(j, a.get(i));
          j--;
        }
      }
      a.set(i, temp);
      quickSortDistanceTable(a, left, i - 1);
      quickSortDistanceTable(a, i + 1, right);
    }
    long endAt = System.currentTimeMillis();
    long execTime = (endAt - startAt)/1000;
    return execTime;
  }

  //用作对比的冒泡排序
  public static long bubbleSortDistanceTable(DistanceTable a) {
    long startAt = System.currentTimeMillis();
    DistanceRecord temp;

    for (int i=0;i<a.size();i++) {
      for (int j=i+1;j<a.size();j++) {
        if (a.get(i).distance > a.get(j).distance) {
          temp = a.get(i);
          a.set(i, a.get(j));
          a.set(j, temp);
        }
      }
    }
    long endAt= System.currentTimeMillis();
    long execTime = (endAt - startAt)/1000;
    return execTime;
  }
}

class Point {
  double x;
  double y;

  Point() {
    this.x = 0;
    this.y = 0;
  }

  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  //    返回当前点与指定点的距离
  public double distanceTo(Point anotherPoint) {
    double distance = Math.sqrt(Math.pow(this.x - anotherPoint.x, 2) + Math.pow(this.y - anotherPoint.y, 2)  );
    return distance;
  }

  //  使用字符串包装Point对象
  public static Point parse(String str) {
    String[] coordinateStringArray = str.split(",");
    double x = Double.parseDouble(coordinateStringArray[0]);
    double y = Double.parseDouble(coordinateStringArray[1]);
    Point point = new Point(x, y);
    return point;
  }

  public String toString() {
    String pointStr = "(" + this.x + ", " + this.y + ")";
    return pointStr;
  }
}

//距离表类，存储两个点对象及其距离。
class DistanceRecord {
  Point p1;
  Point p2;
  double distance;

  DistanceRecord(Point p1, Point p2, double distance) {
    this.p1 = p1;
    this.p2 = p2;
    this.distance = distance;
  }

  public String toString(){
    String str = "distance: " + this.distance + " between " + this.p1 + " and " + this.p2;
    return str;
  }
}

class PointList extends ArrayList<Point> {
  private static final long serialVersionUID = 1L;

  // 从文件中读取信息，每一行调用Point.parse方法生成点对象
  public static PointList generatePointsListByFile(String file) {
    String s;
    PointList points = new PointList();
    File f = new File(file);
    if (f.exists()) {
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        while ((s = br.readLine()) != null) {
          points.add(Point.parse(s));
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    else {
      System.out.println("文件不存在");
    }
    return points;
  }
}

class DistanceTable extends ArrayList<DistanceRecord> {
  private static final long serialVersionUID = 1L;

  //  根据传入的点集依次生成每个点到其他所有点的距离列表
  public static DistanceTable generateDistanceList(PointList points) {
    DistanceTable distanceRecords = new DistanceTable();
    for(int i=0; i<points.size();i++) {
      for(int j=i+1;j<points.size();j++) {
        Point point1 = points.get(i);
        Point point2 = points.get(j);
        double distance = point1.distanceTo(point2);
        DistanceRecord distanceRecord = new DistanceRecord(point1, point2, distance);

        distanceRecords.add(distanceRecord);
      }
    }
    return distanceRecords;
  }

  public void print(){
    for (DistanceRecord distanceRecord : this) {
      System.out.print(distanceRecord + "\n");
    }
  }
}
