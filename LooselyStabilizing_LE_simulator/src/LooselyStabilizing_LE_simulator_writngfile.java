//�t�@�C���̂��߂̃p�b�P�[�W
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

import Agent.Agent;
import Interaction.Interaction;
import RandamPackage.RandomWay;

class LooselyStabilizing_LE_simulator_writngfile{
	public static final int Roundnum = 10000000;	//round数の上界

	public static final int s = 192;		//nの上限(agent.timerの初期値)

	public static final int n_from = 30;	//nはn_from～n_toのデータをとる
	public static final int n_to = 64;		

	public static final int Vset = 1;	//vの速度の上限
	public static final int DistanceforInteraction = 1;	//interactionができる距離
	public static final int Gridsize = 50;	//フィールドのサイズ(Gridsize*Gridsizeの正方形)
	public static final int DataNum = 100;	//データ数

	public static String RandomMethod = "Torus";	//Torus or RWP(Random Way Point)
	public static String CoodinateSystem = "Rectanglar";	//直交座標(Rectanglar) or 極座標(Polar)
	public static String name = "星顕";
	//System.getProperty("user.name");
	
	public static String DataPath = "\\Users\\" + name + "\\Dropbox\\Data\\";
	public static String WritingPath = DataPath + "Data_" + RandomMethod + "\\" + CoodinateSystem + "\\"
			+ RandomMethod + "_" + Vset + "V_" + DistanceforInteraction + "DI_" + Gridsize + "GS_" + DataNum + "DN\\";

	public static void main(String args[]){
		Random random = new Random();
		int CTcounter;
		int CT, HT;
		int SPperRound[] = new int[Roundnum];
		
        File file = new File(WritingPath);
        if (!file.exists()) {
            System.out.println("ディレクトリが存在しません。");
            System.exit(-1);
        }

		for(int n=n_from; n<=n_to; n++){

			Agent agent[] = new Agent[n];
			int CTsum=0, HTsum=0;
			double CTave=0.0 , HTave=0.0;

			/*DataNum個のDataの平均をファイルに保存する*/
			for(int Data=0; Data < DataNum; Data++){

				CTcounter = CT = HT = 0;

				for(int i=0; i<n; i++)
				agent[i] = new Agent(random.nextBoolean(), s, random.nextInt(Gridsize)+random.nextDouble(), random.nextInt(Gridsize)+random.nextDouble());

				for(int i=0; i<Roundnum; i++){
						int leadercount=0;
						int leaderid=0;
						for(int j=0; j<n; j++) if(agent[j].IsLeader()){ leaderid = j; leadercount++; }
						if(leadercount==1){
								SPperRound[i] = leaderid;
								if(i>0)//はじめからリーダが１個だったときのため
								if(SPperRound[i-1]==leaderid){
									HT++;
								}
								else{
									HT = 0; CT = CTcounter;
//									System.out.println(leaderid); 	//リーダのidを返す
								}
							}
//						System.out.println("the number of leaders = " + leadercount);
						int p, q;
						while(true){					//リーダが決定するまで
							p = random.nextInt(n);		//interactionをするagentをランダムで選択
							q = RandomWay.RandamPickNearAgent( p, n, agent, DistanceforInteraction);		//p�Ƌ���1�ȓ��ɂ���m�[�h�̒���(���id�̒Ⴂ)�m�[�h��q�ɑ��
							if(q != -1) { 	//pの周りにinteractionが可能なAgentが見つかったとき
								Interaction.interaction(agent[p], agent[q], s);	
								for(int j=0; j<n; j++) agent[j].Countdown();	//交流したagentのtimerをデクリメント
								break;						//次のラウンドへ
							}
							for(int j=0; j<n; j++){					//各Agentをランダムに移動させる
								agent[j].Vchange((random.nextInt(2*Vset)-Vset)+random.nextDouble() , (random.nextInt(2*Vset)-Vset)+random.nextDouble() );
								agent[j].ShiftPointForTorus(Gridsize);	//移動
							}
						}
						CTcounter++;
						if(CT!=0 && HT!=0 && HT+CT+1 < CTcounter) break;	//リーダが決まった瞬間処理を抜けたかった
					}


				   CTsum += CT;
				   HTsum += HT;
					System.out.println("(n:" + n_from + "~" + n_to + "," + "Datanum:" + DataNum + ")"
							+ "\tn = " + n
							+ ",\tData number = " + (Data+1)
							+ ",\tCT = " + CT + ",\tHT = " + HT);
			}	//for(int Data=0; Data < DataNum; Data++) 終了
			CTave = (double)CTsum / DataNum;
			HTave = (double)HTsum / DataNum;
			/*ファイル書き込みのための処理*/
			try{
				String svalue = new Integer(s).toString();
				String nfromvalue = new Integer(n_from).toString();
				String ntovalue = new Integer(n_to).toString();

		        File fileCT = new File(WritingPath + "CT_" + "_s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue
		        		+ RandomMethod + CoodinateSystem + Vset + "V_" + DistanceforInteraction + "DI_" + Gridsize + "GS_" + DataNum + "DN" + "_" + ".txt");
		        File fileHT = new File(WritingPath + "HT_" + "_s=" + svalue + "_n=from" + nfromvalue + "to" + ntovalue
		        		+ RandomMethod + CoodinateSystem + Vset + "V_" + DistanceforInteraction + "DI_" + Gridsize + "GS_" + DataNum + "DN" + "_" + ".txt");

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
		}//for(int n=n_from; n<=n_to; n++) 終了
	}	//メイン終了
}	//クラス終了
