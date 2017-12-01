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
	//����������ڵ�
    GridPoint pt = grid.getLocation(this);
    int x=pt.getX();
    int y=pt.getY();
    System.out.println("human in danger x="+x+",y="+y);
    
    //ͳ�Ƹ����㽩ʬ������
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 3, 3);//��Ϊ�Ƕ�ά�ģ��������ֵ��x,y����۲�ķ�Χ
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);//����Ԫ�б�
    SimUtilities.shuffle(gridCells, RandomHelper.getUniform());//��������б�
    
    //�����ҵ���Σ�յĵ㣬Σ��ָ������Χ��ʬ������
    GridPoint dangerPoint = null;//��Σ�յ㣺��Χ��ʬ���ĵ�
    int maxDangerIndex = 0;//Σ�վ����ʼ��������Ѱ����Σ�յĵ�
    
    for(GridCell<Zombie> cell : gridCells){//��������Ԫ�б�
       int numOfZombie=cell.size();//�õ���Χ��ʬ������
       if(numOfZombie>=maxDangerIndex){//��ʬ���ĵ�
    	  dangerPoint=cell.getPoint();//��Σ�յ����
     	  maxDangerIndex=numOfZombie;
       }
    }
    System.out.println("most danger point x:"+dangerPoint.getX()+" y:"+dangerPoint.getY());
    
  //ͳ�Ƹ����㽩ʬ������
    //Creates a GridCellNgh using the specified grid, point, type and extent. 
    GridCellNgh<Zombie> nghCreator2 = new GridCellNgh<Zombie>(grid, pt, Zombie.class, 1, 1);//��Ϊ�Ƕ�ά�ģ��������ֵ��x,y����۲�ķ�Χ
    //Gets the neighborhood of GridCells.
    List<GridCell<Zombie>> gridCells2 = nghCreator2.getNeighborhood(true);//����Ԫ�б�
    SimUtilities.shuffle(gridCells2, RandomHelper.getUniform());//��������б�
    
    GridPoint safePoint = null;//��ȫ�㣨Ŀ��㣩��Σ��ָ����ͣ���Χ��ʬ ���٣��ĵ㣬����dangerPoint��Զ�ĵ�
    int minZombieNum = Integer.MAX_VALUE;//��СΣ��ָ��������Χ��ʬ��������ʼ��������Ѱ����԰�ȫ��ļ���
    int maxSafeDis=0;//���ȫ���룬����ָ���Ǿ�����Σ�յ�ľ��룬�����ڰ�ȫ�㼯����ѡ���ȫ�ĵ�
    
    //���������㣬Ѱ���ȫ�ĵ�
    for(GridCell<Zombie> cell : gridCells2){
      
      GridPoint pointWithZombies = cell.getPoint();
      int tmpx=pointWithZombies.getX();
      int tmpy=pointWithZombies.getY(); 
      
      int numOfZombie=cell.size();//�õ���Χ��ʬ������
      System.out.println("x="+tmpx+",y="+tmpy+",numOfZombie="+numOfZombie);
      
      if(numOfZombie<minZombieNum){//����ǰ��Σ��ָ�����ȡ���СΣ��ָ����Сʱ
    	 safePoint=cell.getPoint();//��ȫ�����
    	 minZombieNum = numOfZombie;//����СΣ��ָ��������
      }else if(numOfZombie==minZombieNum){//Σ��ָ����ͬʱ���Ƚ�Σ�վ��룬Ѱ��Σ�վ������ĵ�
    	  if(dangerPoint!=null){//������Σ�յĵ�
    		 int distance=(tmpx-dangerPoint.getX())*(tmpx-dangerPoint.getX())+(tmpy-dangerPoint.getY())*(tmpy-dangerPoint.getY());
          	 if(distance>maxSafeDis){//����ǰ��ȫ���롿�ȡ����ȫ���롿��ʱ
          		maxSafeDis=distance;//��ȫ�������
          		safePoint=cell.getPoint();//��ȫ�����
          		minZombieNum = numOfZombie;//Σ��ָ������
                System.out.println("max safe distance:"+maxSafeDis);
          	 }
          }
      }
    }
    
    if(energy>0 && !pt.equals(safePoint)){
      System.out.println("Human before x:"+pt.getX()+" y:"+pt.getY());
      grid.moveTo(this, safePoint.getX(), safePoint.getY());//�ƶ�����ʬ���ٵĵط�
      pt = grid.getLocation(this);
      System.out.println("Human moverd x:"+pt.getX()+" y:"+pt.getY());
      energy--;
    }else{
      energy = startingEnergy;
    }
  }

}
