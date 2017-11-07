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
  

@Watch(watcheeClassName = "jzombies.Zombie",
      watcheeFieldNames = "moved",
      query = "within_moore 1",
      whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
  public void run(){
    GridPoint pt = grid.getLocation(this);
    GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);
    List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
    
    GridPoint pointWithLeastZombies = null;
    int minCount = Integer.MAX_VALUE;
    for(GridCell<Zombie> cell : gridCells){
      if(cell.size()<minCount){
        pointWithLeastZombies = cell.getPoint();
        minCount = cell.size();
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
