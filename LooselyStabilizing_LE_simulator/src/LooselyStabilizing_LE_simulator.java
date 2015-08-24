import java.util.Random;

import Agent.Agent;
import Interaction.Interaction;
import RandamPackage.*;

//ファイルのためのパッケージ
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

class LooselyStabilizing_LE_simulator{
	public static final int Gridsize = 50;
	public static final int Roundnum = 1000000;
	
	public static final int DataNum = 100;
	
	public static final int s = 192;			//96以上で3n以上
	
	public static final int n_from = 10;	//sに対して、n_fromからn_toまでのnを書き出す
	public static final int n_to = 64;
	
	public static void main(String args[]){
		Random random = new Random();
		int CTcounter;
		int CT, HT;
		int SPperRound[] = new int[Roundnum];
		
		for(int n=n_from; n<=n_to; n++){
			System.out.println("n number = " + n);
			
			Agent agent[] = new Agent[n];
			int CTsum=0, HTsum=0;
			double CTave=0.0 , HTave=0.0;
			
			/*ここからシュミレートとファイル書き込み*/
			for(int Data=0; Data < DataNum; Data++){
				System.out.println("(n:" + n_from + "~" + n_to + "," + "Datanum:" + DataNum + ")"
						+ " n = " + n
						+ ", Data number = " +Data);
				CTcounter = CT = HT = 0;
			
				for(int i=0; i<n; i++)
				agent[i] = new Agent(random.nextBoolean(), s, random.nextInt(Gridsize)+random.nextDouble(), random.nextInt(Gridsize)+random.nextDouble());
				
				for(int i=0; i<Roundnum; i++){
						int leadercount=0;
						int leaderid=0;
						for(int j=0; j<n; j++) if(agent[j].IsLeader()){ leaderid = j; leadercount++; }
						if(leadercount==1){ 
								SPperRound[i] = leaderid;
								if(i>0)//一番最初にリーダ一個やったらばぐるからi>0
								if(SPperRound[i-1]==leaderid){	
									HT++;
								}
								else{
									HT = 0; CT = CTcounter;
//									System.out.println(leaderid); 	//リーダのidをプリント
								}
							}
//						System.out.println("the number of leaders = " + leadercount);
						int p, q;
						while(true){					//交流させる個体を選ぶ
							p = random.nextInt(n-1);		//ランダムにagentをとってくる
							q = RandomWay.RandamPickNearAgent( p, n, agent);		//pと距離1以内にあるノードの中で(一番idの低い)ノードをqに代入
							if(q != -1) { 	//qが見つかったらinteractionをして次のラウンドへ
								Interaction.interaction(agent[p], agent[q], s);	//交流させる
								for(int j=0; j<n; j++) agent[j].Countdown();	//timerをカウントする
								break;						//次のラウンドへ
							}
							for(int j=0; j<n; j++){					//各個体移動
								agent[j].Vchange((random.nextDouble()-0.5)*2 , (random.nextDouble()-0.5)*2 );
								agent[j].ShiftPointForTorus(Gridsize);
							}
						}
						CTcounter++;
						if(CT!=0 && HT!=0 && HT+CT+1 < CTcounter) break;	//HTが終わってら即終了したかった
					}
//					System.out.println("CT = " + CT);
//					System.out.println("HT = " + HT);
					
					/*CT、HTのデータをファイルに書き込むための処理*/
				    try{
				    	String svalue = new Integer(s).toString();
				    	String nvalue = new Integer(n).toString();
				    	
				        File fileCT = new File("src\\Data\\CT_s=" + svalue + "\\n=" + nvalue + ".txt");
				        File fileHT = new File("src\\Data\\HT_s=" + svalue + "\\n=" + nvalue + ".txt");
				        
				        if(!fileCT.exists()){ fileCT.createNewFile(); }
				        if(!fileHT.exists()){ fileHT.createNewFile(); }
			
				        PrintWriter pwCT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileCT, true)));
				        PrintWriter pwHT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileHT, true)));
				        
				        String stringCT = Integer.toString(CT);
				        String stringHT = Integer.toString(HT);
				        
				        pwCT.write(stringCT + "\r\n");
				        pwHT.write(stringHT + "\r\n");
				          
				        pwCT.close();
				        pwHT.close();
			
				      }catch(IOException e){
				        System.out.println(e);
				      }
				   CTsum += CT;
				   HTsum += HT;
			}	//for(int Data=0; Data < DataNum; Data++) 終わり
			CTave = (double)CTsum / DataNum;
			HTave = (double)HTsum / DataNum;
			/*平均をファイルに書き込む*/
			try{	
				String svalue = new Integer(s).toString();
				String nfromvalue = new Integer(n_from).toString();
				String ntovalue = new Integer(n_to).toString();
		    	
		        File fileCT = new File("src\\Data\\CT_s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue + ".txt");
		        File fileHT = new File("src\\Data\\HT_s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue + ".txt");
		        
		        if(!fileCT.exists()){ fileCT.createNewFile(); }
		        if(!fileHT.exists()){ fileHT.createNewFile(); }
	
		        PrintWriter pwCT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileCT, true)));
		        PrintWriter pwHT = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileHT, true)));
		        
		        String stringCTave = String.valueOf(CTave);
		        String stringHTave = String.valueOf(HTave);
		        
		        pwCT.write(stringCTave + "\r\n");
		        pwHT.write(stringHTave + "\r\n");
		          
		        pwCT.close();
		        pwHT.close();
	
		      }catch(IOException e){
		        System.out.println(e);
		      }
		}//for(int n=n_from; n<=n_to; n++) 終わり
	}	//メイン終わり
}	//クラス終わり
