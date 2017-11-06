/**
 * 
 */
package jzombies;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;


/**
 * @author jiangxingqi
 *
 */
public class Zombie {
  private Grid grid;
  private boolean moved;

  public Zombie(Grid grid) {
    this.grid = grid;
  }

  @ScheduledMethod(start=1, interval=1)
  public void step() {
    GridPoint pt = grid.getLocation(this);
    GridCellNgh<Human> nghCreator = new GridCellNgh<Human>(grid, pt, Human.class, 1, 1);
    List<GridCell<Human>> gridCells = nghCreator.getNeighborhood(true);
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

    GridPoint pointWithMostHumans = null;
    int maxCount = -1;
    for (GridCell<Human> cell : gridCells) {
      if (cell.size() > maxCount) {
        pointWithMostHumans = cell.getPoint();
        maxCount = cell.size();
      }
    }
    System.out.println("before moved\n");
    if (!pointWithMostHumans.equals(grid.getLocation(this))) {
      grid.moveTo(this, pointWithMostHumans.getX(), pointWithMostHumans.getY());
      moved = true;
      System.out.println("moved\n");
      infect();
    }
  }
  
  public void infect(){
    GridPoint pt = grid.getLocation(this);
    List<Object> humans = new ArrayList<Object>();
    for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY())){
      if (obj instanceof Human){
        humans.add(obj);
      }
    }
    if(humans.size()>0){
      int index = RandomHelper.nextIntFromTo(0, humans.size()-1);
      Object human = humans.get(index);
      Context<Object> context = ContextUtils.getContext(human);
      context.remove(human);
      Zombie zombie = new Zombie(grid);
      context.add(zombie);
      grid.moveTo(zombie, pt.getX(), pt.getY());
    }
  }
}
