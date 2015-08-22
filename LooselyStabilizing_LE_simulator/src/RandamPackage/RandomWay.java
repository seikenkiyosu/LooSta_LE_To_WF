package RandamPackage;
import Agent.Agent;

public class RandomWay {
	public static int RandamPickNearAgent(int p, int n, Agent agent[]){
		for(int i=0; i<n; i++){
			if(p!=i){
				if((agent[p].getx() - agent[i].getx())*(agent[p].getx() - agent[i].getx())
						+(agent[p].gety() - agent[i].gety())*(agent[p].gety() - agent[i].gety())  <= 1)
				return i;
			}
		}
		return -1;
	}
}
