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
    GridPoint pt = grid.getLocation(this);//获得人类所在点
    //获得人类现在所在点周围的网格单元，并获得单元周围Zombie的数量
    GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);//因为是二维的，最后两个值是x,y方向观察的范围
    List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);//网格单元列表
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());//随机打乱列表
    
    GridPoint pointWithLeastZombies = null;//目标点：僵尸最少的点
    int minCount = Integer.MAX_VALUE;//最小值初始化
    for(GridCell<Zombie> cell : gridCells){//遍历网格单元列表
    	//加入网格单元的僵尸数量，比当前最小值小，则更新
      if(cell.size()<minCount){
        pointWithLeastZombies = cell.getPoint();//更新目标点
        minCount = cell.size();//僵尸最小值更新
      }
    }
    
    if(energy>0 && !pt.equals(pointWithLeastZombies)){
      System.out.println("Human before moverd x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, pointWithLeastZombies.getX(), pointWithLeastZombies.getY());//移动至僵尸最少的地方
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
