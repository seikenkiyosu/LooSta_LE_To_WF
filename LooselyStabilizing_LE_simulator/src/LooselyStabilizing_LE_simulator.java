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

class LooselyStabilizing_LE_simulator{
	public static final int Roundnum = 10000000;

	public static final int s = 96;			//96�ȏ��3n�ȏ�

	public static final int n_from = 20;	//s�ɑ΂��āAn_from����n_to�܂ł�n�������o��
	public static final int n_to = 32;

	public static final int Vset = 10;
	public static final int DistanceforInteraction = 1;
	public static final int Gridsize = 50;
	public static final int DataNum = 100;

	public static String RandomMethod = "Torus";
	public static String CoodinateSystem = "Rectanglar";
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

			/*��������V���~���[�g�ƃt�@�C����������*/
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
								if(i>0)//��ԍŏ��Ƀ��[�_��������΂��邩��i>0
								if(SPperRound[i-1]==leaderid){
									HT++;
								}
								else{
									HT = 0; CT = CTcounter;
//									System.out.println(leaderid); 	//���[�_��id���v�����g
								}
							}
//						System.out.println("the number of leaders = " + leadercount);
						int p, q;
						while(true){					//�𗬂�����̂�I��
							p = random.nextInt(n);		//�����_����agent���Ƃ��Ă���
							q = RandomWay.RandamPickNearAgent( p, n, agent, DistanceforInteraction);		//p�Ƌ���1�ȓ��ɂ���m�[�h�̒���(���id�̒Ⴂ)�m�[�h��q�ɑ��
							if(q != -1) { 	//q������������interaction�����Ď��̃��E���h��
								Interaction.interaction(agent[p], agent[q], s);	//�𗬂�����
								for(int j=0; j<n; j++) agent[j].Countdown();	//timer���J�E���g����
								break;						//���̃��E���h��
							}
							for(int j=0; j<n; j++){					//�e�̈ړ�
								agent[j].Vchange((random.nextInt(2*Vset-1)-Vset)+random.nextDouble() , (random.nextInt(2*Vset-1)-Vset)+random.nextDouble() );
								agent[j].ShiftPointForTorus(Gridsize);
							}
						}
						CTcounter++;
						if(CT!=0 && HT!=0 && HT+CT+1 < CTcounter) break;	//HT���I����Ă瑦�I������������
					}
//					System.out.println("CT = " + CT);
//					System.out.println("HT = " + HT);


				   CTsum += CT;
				   HTsum += HT;
					System.out.println("(n:" + n_from + "~" + n_to + "," + "Datanum:" + DataNum + ")"
							+ "\tn = " + n
							+ ",\tData number = " + (Data+1)
							+ ",\tCT = " + CT + ",\tHT = " + HT);
			}	//for(int Data=0; Data < DataNum; Data++) �I���
			CTave = (double)CTsum / DataNum;
			HTave = (double)HTsum / DataNum;
			/*���ς��t�@�C���ɏ�������*/
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
		}//for(int n=n_from; n<=n_to; n++) �I���
	}	//���C���I���
}	//�N���X�I���
