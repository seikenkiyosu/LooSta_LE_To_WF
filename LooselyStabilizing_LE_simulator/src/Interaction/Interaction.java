package Interaction;
import Agent.Agent;

public class Interaction {
	public static void interaction(Agent P, Agent Q){
		//Rule1
		if(P.IsLeader() && Q.IsLeader()){ 
			P.TimerReset(); Q.TimerReset(); 
			Q.ChangeState(false);
			return;
		}
		//Rule2
		if(P.IsLeader() && !Q.IsLeader()){ 
			P.TimerReset(); Q.TimerReset();
			return;
		}
		//Rule3
		if(!P.IsLeader() && Q.IsLeader()){ 
			P.TimerReset(); Q.TimerReset();
			return;
		}
		//Rule4
		if(!P.IsLeader() && !Q.IsLeader() && P.IsTimeout() && Q.IsTimeout()){ 
			P.TimerReset(); Q.TimerReset(); 
			P.ChangeState(true);
			return;
		}
		//Rule5
		if(!P.IsLeader() && !Q.IsLeader()){
			if(P.gettimer() > Q.gettimer()){
				P.Countdown();
				Q.TimerForRule5(P.gettimer());
			}
			else{
				Q.Countdown();
				P.TimerForRule5(Q.gettimer());
			}
			return;
		}
	}
}
