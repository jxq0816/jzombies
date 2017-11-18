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
    GridPoint pt = grid.getLocation(this);//����������ڵ�
    //��������������ڵ���Χ������Ԫ������õ�Ԫ��ΧZombie������
    GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);//��Ϊ�Ƕ�ά�ģ��������ֵ��x,y����۲�ķ�Χ
    List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);//����Ԫ�б�
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());//��������б�
    
    GridPoint pointWithLeastZombies = null;//Ŀ��㣺��ʬ���ٵĵ�
    int minCount = Integer.MAX_VALUE;//��Сֵ��ʼ��
    for(GridCell<Zombie> cell : gridCells){//��������Ԫ�б�
    	//��������Ԫ�Ľ�ʬ�������ȵ�ǰ��СֵС�������
      if(cell.size()<minCount){
        pointWithLeastZombies = cell.getPoint();//����Ŀ���
        minCount = cell.size();//��ʬ��Сֵ����
      }
    }
    
    if(energy>0 && !pt.equals(pointWithLeastZombies)){
      System.out.println("Human before moverd x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, pointWithLeastZombies.getX(), pointWithLeastZombies.getY());//�ƶ�����ʬ���ٵĵط�
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
