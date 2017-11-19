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
  

  @Watch(watcheeClassName = "jzombies.Zombie",watcheeFieldNames = "moved",query = "within_moore 1",whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
  public void run(){
	//获得人类所在点
    GridPoint pt = grid.getLocation(this);
    int x=pt.getX();
    int y=pt.getY();
    System.out.println("human in danger x="+x+",y="+y);
    
    //统计附近点僵尸的数量
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);//因为是二维的，最后两个值是x,y方向观察的范围
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);//网格单元列表
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());//随机打乱列表
    
    GridPoint safe = null;//安全点：僵尸 最少的点
    int minCount = Integer.MAX_VALUE;//安全指数初始化
    
    GridPoint danger = null;//危险点：周围有僵尸 的点
    int maxCount = 0;//危险指数值初始化
    
    int distance=0;//安全距离
    int maxDis=0;
    
    for(GridCell<Zombie> cell : gridCells){//遍历网格单元列表
       GridPoint pointWithZombies = cell.getPoint();
       int numOfZombie=cell.size();//该点周围僵尸的数量
       if(numOfZombie>=maxCount){//最危险点更新
     	  danger=cell.getPoint();
     	  maxCount=numOfZombie;
       }
    }
    System.out.println("most danger point x:"+danger.getX()+" y:"+danger.getY());
    
    for(GridCell<Zombie> cell : gridCells){//遍历网格单元列表
      //加入网格单元的僵尸数量，比当前最小值小，则更新
      GridPoint pointWithZombies = cell.getPoint();
      int tmpx=pointWithZombies.getX();
      int tmpy=pointWithZombies.getY(); 
      
      int numOfZombie=cell.size();//该点周围僵尸的数量
      System.out.println("x="+tmpx+",y="+tmpy+",numOfZombie="+numOfZombie);
      if(numOfZombie<minCount){
    	 safe=cell.getPoint();//安全点更新
         minCount = numOfZombie;//周围最少僵尸数量更新
      }else if(numOfZombie==minCount){
    	  if(danger!=null){//存在危险点
          	 distance=(tmpx-danger.getX())*(tmpx-danger.getX())+(tmpy-danger.getY())*(tmpy-danger.getY());
          	 if(distance>maxDis){
          		maxDis=distance;//安全距离更新
          		safe=cell.getPoint();//安全点更新
                minCount = numOfZombie;//周围最少僵尸数量更新
                System.out.println("maxDis:"+maxDis);
          	 }
          }
      }
    }
    
    if(energy>0 && !pt.equals(safe)){
      System.out.println("Human before x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, safe.getX(), safe.getY());//移动至僵尸最少的地方
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
