package Agent;
public class Agent {
	
	private boolean leader;
	private int timer;
	private double x;
	private double y; 
	private double vx;
	private double vy;
	
	
	public Agent(boolean initleader, int timerset, double inix, double iniy) {
		this.leader = initleader;	
		this.timer = timerset;
		this.x = inix; this.y = iniy;
		this.vx = 0; this.vy = 0;
	}
	
	public boolean IsLeader(){ return this.leader; }
	public int gettimer(){ return this.timer; }
	public double getx(){ return this.x; }
	public double gety(){ return this.y; }
	
	public void ShiftPointForRWP(){		//ランダムウェイポイントでの一秒間のshift
		this.x += this.vx;
		this.y += this.vy;
	}
	
	public void ShiftPointForTorus(int Gridsize){	//トーラス内での一秒間のshift
		this.x += this.vx;
		if(this.x > Gridsize) this.x %= Gridsize;
		if(this.x <  0) this.x += Gridsize;
		this.y += this.vy;
		if(this.y > Gridsize) this.y %= Gridsize;
		if(this.y <  0) this.y += Gridsize;
	}
	
	public void Vchange(double ax, double ay){	//速度変化
		this.vx = ax;
		this.vy = ay;
	}
	
	public void Countdown(){
		if(this.timer > 0) this.timer--;
	}
	
	public boolean IsTimeout(){
		if(this.timer==0) return true;
		else return false;
	}
	
	public void TimerReset(int timerset){
		this.timer = timerset;
	}
	
	public void TimerForRule5(int f){
		this.timer = f;
	}
	
	public void ChangeState(boolean isleader){
		this.leader = isleader;
	}
	
	public void ShowPoint() {		//現在の座標を表示
		System.out.print("x = " + this.x);
		System.out.println("\t\ty = " + this.y);
	}
}
