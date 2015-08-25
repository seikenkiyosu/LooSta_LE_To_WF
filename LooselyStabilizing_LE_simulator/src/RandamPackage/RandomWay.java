package RandamPackage;

import java.util.Random;
import Agent.Agent;

public class RandomWay {
	public static int RandamPickNearAgent(int p, int n, Agent agent[], int DI){
		Random random = new Random();
		int randompoint;
		for(int i=0; i<3*n; i++){
			randompoint = random.nextInt(n);
			if(p!=randompoint){
				if((agent[p].getx() - agent[randompoint].getx())*(agent[p].getx() - agent[randompoint].getx())
						+(agent[p].gety() - agent[randompoint].gety())*(agent[p].gety() - agent[randompoint].gety()) <= DI)
				return randompoint;
			}
		}
		return -1;
	}
}
