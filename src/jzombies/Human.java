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
	//����������ڵ�
    GridPoint pt = grid.getLocation(this);
    int x=pt.getX();
    int y=pt.getY();
    System.out.println("human in danger x="+x+",y="+y);
    
    //ͳ�Ƹ����㽩ʬ������
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);//��Ϊ�Ƕ�ά�ģ��������ֵ��x,y����۲�ķ�Χ
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);//����Ԫ�б�
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());//��������б�
    
    //ͳ�Ƹ�������3��ʬ������
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator2 = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 3, 3);//��Ϊ�Ƕ�ά�ģ��������ֵ��x,y����۲�ķ�Χ
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells2 = nghCreator2.getNeighborhood(true);//����Ԫ�б�
    SimUtilities.shuffle(gridCells2, RandomHelper.getUniform());//��������б�
    
    
    GridPoint target = null;//Ŀ��㣺��ʬ �������ٵĵ�
    GridPoint pointWithHumans = null;//Ŀ��㣺�����ٵĵ�
    double minCount = Double.MAX_VALUE;//��Сֵ��ʼ��
    
    for(GridCell<Zombie> cell : gridCells){//��������Ԫ�б�
      //��������Ԫ�Ľ�ʬ�������ȵ�ǰ��СֵС�������
      GridPoint pointWithZombies = cell.getPoint();
      int x2=pointWithZombies.getX();
      int y2=pointWithZombies.getY(); 
      
      int numOfZombie=cell.size();//Ŀ��㽩ʬ������
      System.out.println("x2="+x2+",y2="+y2+",numOfZombie="+numOfZombie);
      int numOfZombie2=0;
      for(GridCell<Zombie> cell2 : gridCells2){
         	pointWithHumans=cell2.getPoint();
            //��øõ㸽�����������
         	if(pointWithHumans.getX()==pointWithZombies.getX()&&pointWithHumans.getY()==pointWithZombies.getY()){
         		numOfZombie2=cell2.size();
         	}
      }
      double sum=numOfZombie*0.8+numOfZombie2*0.2; 
      if(sum<=minCount){
        target=cell.getPoint();
        minCount = sum;//��Сֵ����
      }
    }
    
    if(energy>0 && !pt.equals(target)){
      System.out.println("Human before x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, target.getX(), target.getY());//�ƶ�����ʬ���ٵĵط�
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
