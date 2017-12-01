/**
 * 
 */
package jzombies;

import java.util.List;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

/**
 * @author jiangxinqgi
 *
 */
public class Human {
  private Grid grid;
  private int energy, startingEnergy;

  public Human(Grid grid, int energy) {
    this.grid = grid;
    this.energy = this.startingEnergy = energy;
  }
  

  @Watch(watcheeClassName = "jzombies.Zombie",watcheeFieldNames = "moved",query = "within_moore 9",whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
  public void run(){
	//获得人类所在点
    GridPoint pt = grid.getLocation(this);
    int x=pt.getX();
    int y=pt.getY();
    System.out.println("human in danger x="+x+",y="+y);
    
    //统计附近点僵尸的数量
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 3, 3);//因为是二维的，最后两个值是x,y方向观察的范围
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);//网格单元列表
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());//随机打乱列表
    
    //首先找到最危险的点，危险指数是周围僵尸的数量
    GridPoint dangerPoint = null;//最危险点：周围僵尸最多的点
    int maxDangerIndex = 0;//危险距离初始化，用于寻找最危险的点
    
    for(GridCell<Zombie> cell : gridCells){//遍历网格单元列表
       int numOfZombie=cell.size();//该点周围僵尸的数量
       if(numOfZombie>=maxDangerIndex){//僵尸最多的点
    	  dangerPoint=cell.getPoint();//最危险点更新
     	  maxDangerIndex=numOfZombie;
       }
    }
    System.out.println("most danger point x:"+dangerPoint.getX()+" y:"+dangerPoint.getY());
    
  //统计附近点僵尸的数量
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator2 = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);//因为是二维的，最后两个值是x,y方向观察的范围
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells2 = nghCreator2.getNeighborhood(true);//网格单元列表
    SimUtilities.shuffle(gridCells2, RandomHelper.getUniform());//随机打乱列表
    
    GridPoint safePoint = null;//安全点（目标点）：危险指数最低（周围僵尸 最少）的点，距离dangerPoint最远的点
    int minZombieNum = Integer.MAX_VALUE;//最小危险指数：即周围僵尸数量，初始化，用于寻找相对安全点的集合
    int maxSafeDis=0;//最大安全距离，衡量指标是距离最危险点的距离，用于在安全点集合中选出最安全的点
    
    //遍历附近点，寻找最安全的点
    for(GridCell<Zombie> cell : gridCells2){
      
      GridPoint pointWithZombies = cell.getPoint();
      int tmpx=pointWithZombies.getX();
      int tmpy=pointWithZombies.getY(); 
      
      int numOfZombie=cell.size();//该点周围僵尸的数量
      System.out.println("x="+tmpx+",y="+tmpy+",numOfZombie="+numOfZombie);
      
      if(numOfZombie<minZombieNum){//【当前点危险指数】比【最小危险指数】小时
    	 safePoint=cell.getPoint();//安全点更新
    	 minZombieNum = numOfZombie;//【最小危险指数】更新
      }else if(numOfZombie==minZombieNum){//危险指数相同时，比较危险距离，寻找危险距离最大的点
    	  if(dangerPoint!=null){//存在最危险的点
    		 int distance=(tmpx-dangerPoint.getX())*(tmpx-dangerPoint.getX())+(tmpy-dangerPoint.getY())*(tmpy-dangerPoint.getY());
          	 if(distance>maxSafeDis){//【当前安全距离】比【最大安全距离】大时
          		maxSafeDis=distance;//安全距离更新
          		safePoint=cell.getPoint();//安全点更新
          		minZombieNum = numOfZombie;//危险指数更新
                System.out.println("max safe distance:"+maxSafeDis);
          	 }
          }
      }
    }
    
    if(energy>0 && !pt.equals(safePoint)){
      System.out.println("Human before x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, safePoint.getX(), safePoint.getY());//移动至僵尸最少的地方
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
