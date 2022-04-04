package es.ucm.fdi.ici.c1920.practica4.grupo01;

public class OutputMsPacMan 
{
	private double RUN_LIMIT = 50,
					CHASE_LIMIT = 50;
	
	private boolean run,
					chase;
	
	public void processOutput(MsPacManDescription queryDesc)
	{
		run = false;
		chase = false;
		
		setRun(queryDesc);
		
		if(!run)
			setChase(queryDesc);
	}
	
	private void setRun(MsPacManDescription queryDesc)
	{
		if(queryDesc.getBlinkyDistance() < RUN_LIMIT && !queryDesc.getBlinkyEdible())
			run = true;
		
		else if (queryDesc.getPinkyDistance() < RUN_LIMIT && !queryDesc.getPinkyEdible())
			run = true;
		
		else if (queryDesc.getInkyDistance() < RUN_LIMIT && !queryDesc.getInkyEdible())
			run = true;
		
		else if (queryDesc.getSueDistance() < RUN_LIMIT && !queryDesc.getSueEdible())
			run = true;
	}
	
	private void setChase(MsPacManDescription queryDesc)
	{
		if(queryDesc.getBlinkyDistance() < CHASE_LIMIT && queryDesc.getBlinkyEdible())
			chase = true;
		
		else if (queryDesc.getPinkyDistance() < CHASE_LIMIT && queryDesc.getPinkyEdible())
			chase = true;
		
		else if (queryDesc.getInkyDistance() < CHASE_LIMIT && queryDesc.getInkyEdible())
			chase = true;
		
		else if (queryDesc.getSueDistance() < CHASE_LIMIT && queryDesc.getSueEdible())
			chase = true;
	}
	
	public boolean isRunning() { return run; }
	public boolean isChasing() { return chase; }
	
}
