import java.util.Random;
import Agent.Agent;
import Interaction.Interaction;
import RandamPackage.*;

class LooselyStabilizing_LE_simulator{
	public static final int Gridsize = 100;
	public static final int Roundnum = 1000000;
	public static final int n = 100;
	
	public static void main(String args[]){
		Random random = new Random();
		Agent agent[] = new Agent[n];
		int CTcounter = 0;
		int CT = 0, HT = 0;
		int SPperRound[] = new int[Roundnum];
		
		for(int i=0; i<n; i++)
		agent[i] = new Agent(random.nextBoolean(), random.nextInt(Gridsize)+random.nextDouble(), random.nextInt(Gridsize)+random.nextDouble());
		
		for(int i=0; i<Roundnum; i++){
			int leadercount=0;
			int leaderid=0;
			for(int j=0; j<n; j++) if(agent[j].IsLeader()){ leaderid = j; leadercount++; }
			if(leadercount==1){ 
					SPperRound[i] = leaderid;
					if(SPperRound[i-1]==leaderid){
						HT++;
					}
					else{
						HT = 0; CT = CTcounter;
						System.out.println(leaderid); 
					}
				}
			System.out.println("the number of leaders = " + leadercount);
			int p, q;
			while(true){					//交流させる個体を選ぶ
				p = random.nextInt(n-1);		//ランダムにagentをとってくる
				q = RandomWay.RandamPickNearAgent( p, n, agent);		//pと距離1以内にあるノードの中で(一番idの低い)ノードをqに代入
				if(q != -1) { 	//qが見つかったらinteractionをして次のラウンドへ
					Interaction.interaction(agent[p], agent[q]);	//交流させる
					for(int j=0; j<n; j++) agent[j].Countdown();	//timerをカウントする
					break;						//次のラウンドへ
				}
				for(int j=0; j<n; j++){					//各個体移動
					agent[j].Vchange((random.nextDouble()-0.5)*2 , (random.nextDouble()-0.5)*2 );
					agent[j].ShiftPointForTorus(Gridsize);
//					agent[j].ShowPoint();
				}
			}
			CTcounter++;
			if(CT!=0 && HT!=0 && HT+CT+1 < CTcounter) break;	//HTが終わってら即終了したかった
		}
		System.out.println("CT = " + CT);
		System.out.println("HT = " + HT++);
	}
}
