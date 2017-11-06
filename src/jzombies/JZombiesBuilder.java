/**
 * 
 */
package jzombies;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * @author jiangxingqi
 *
 */
public class JZombiesBuilder implements ContextBuilder<Object> {

	public Context build(Context<Object> context) {
		// TODO Auto-generated method stub
		context.setId("jzombies");
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
	    Grid<Object> grid = gridFactory.createGrid("grid", context,
	        new GridBuilderParameters<Object>(new WrapAroundBorders(),
	            new SimpleGridAdder<Object>(),
	            true, 50, 50));
	    
	    int zombieCount = 5;
	    for (int i=0; i<zombieCount; i++){
	      context.add(new Zombie(grid));
	    }
	    int humanCount = 100;
	    for (int i=0; i<humanCount; i++){
	      int energy = RandomHelper.nextIntFromTo(4, 10);
	      context.add(new Human(grid, energy));
	    }
	    
	    for (Object obj : context){
	      int x, y;
	      x = RandomHelper.nextIntFromTo(0, 50);
	      y = RandomHelper.nextIntFromTo(0, 50);
	      grid.moveTo(obj, x, y);
	    }
	    return context;
	}
}
