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
    
    GridPoint safe = null;//��ȫ�㣺��ʬ ���ٵĵ�
    int minCount = Integer.MAX_VALUE;//��ȫָ����ʼ��
    
    GridPoint danger = null;//Σ�յ㣺��Χ�н�ʬ �ĵ�
    int maxCount = 0;//Σ��ָ��ֵ��ʼ��
    
    int distance=0;//��ȫ����
    int maxDis=0;
    
    for(GridCell<Zombie> cell : gridCells){//��������Ԫ�б�
       GridPoint pointWithZombies = cell.getPoint();
       int numOfZombie=cell.size();//�õ���Χ��ʬ������
       if(numOfZombie>=maxCount){//��Σ�յ����
     	  danger=cell.getPoint();
     	  maxCount=numOfZombie;
       }
    }
    System.out.println("most danger point x:"+danger.getX()+" y:"+danger.getY());
    
    for(GridCell<Zombie> cell : gridCells){//��������Ԫ�б�
      //��������Ԫ�Ľ�ʬ�������ȵ�ǰ��СֵС�������
      GridPoint pointWithZombies = cell.getPoint();
      int tmpx=pointWithZombies.getX();
      int tmpy=pointWithZombies.getY(); 
      
      int numOfZombie=cell.size();//�õ���Χ��ʬ������
      System.out.println("x="+tmpx+",y="+tmpy+",numOfZombie="+numOfZombie);
      if(numOfZombie<minCount){
    	 safe=cell.getPoint();//��ȫ�����
         minCount = numOfZombie;//��Χ���ٽ�ʬ��������
      }else if(numOfZombie==minCount){
    	  if(danger!=null){//����Σ�յ�
          	 distance=(tmpx-danger.getX())*(tmpx-danger.getX())+(tmpy-danger.getY())*(tmpy-danger.getY());
          	 if(distance>maxDis){
          		maxDis=distance;//��ȫ�������
          		safe=cell.getPoint();//��ȫ�����
                minCount = numOfZombie;//��Χ���ٽ�ʬ��������
                System.out.println("maxDis:"+maxDis);
          	 }
          }
      }
    }
    
    if(energy>0 && !pt.equals(safe)){
      System.out.println("Human before x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, safe.getX(), safe.getY());//�ƶ�����ʬ���ٵĵط�
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
