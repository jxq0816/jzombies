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
  

  @Watch(watcheeClassName = "jzombies.Zombie",watcheeFieldNames = "moved",query = "within_moore 2",whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
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
    
    //统计附近距离3僵尸的数量
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator2 = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 3, 3);//因为是二维的，最后两个值是x,y方向观察的范围
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells2 = nghCreator2.getNeighborhood(true);//网格单元列表
    SimUtilities.shuffle(gridCells2, RandomHelper.getUniform());//随机打乱列表
    
    
    GridPoint target = null;//目标点：僵尸 和人最少的点
    GridPoint pointWithHumans = null;//目标点：人最少的点
    double minCount = Double.MAX_VALUE;//最小值初始化
    
    for(GridCell<Zombie> cell : gridCells){//遍历网格单元列表
      //加入网格单元的僵尸数量，比当前最小值小，则更新
      GridPoint pointWithZombies = cell.getPoint();
      int x2=pointWithZombies.getX();
      int y2=pointWithZombies.getY(); 
      
      int numOfZombie=cell.size();//目标点僵尸的数量
      System.out.println("x2="+x2+",y2="+y2+",numOfZombie="+numOfZombie);
      int numOfZombie2=0;
      for(GridCell<Zombie> cell2 : gridCells2){
         	pointWithHumans=cell2.getPoint();
            //获得该点附近人类的数量
         	if(pointWithHumans.getX()==pointWithZombies.getX()&&pointWithHumans.getY()==pointWithZombies.getY()){
         		numOfZombie2=cell2.size();
         	}
      }
      double sum=numOfZombie*0.8+numOfZombie2*0.2; 
      if(sum<=minCount){
        target=cell.getPoint();
        minCount = sum;//最小值更新
      }
    }
    
    if(energy>0 && !pt.equals(target)){
      System.out.println("Human before x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, target.getX(), target.getY());//移动至僵尸最少的地方
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
