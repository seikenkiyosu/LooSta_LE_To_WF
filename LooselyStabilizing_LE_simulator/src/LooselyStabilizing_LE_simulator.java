import java.util.Random;
import Agent.Agent;
import Interaction.Interaction;
import RandamPackage.*;

//�t�@�C���̂��߂̃p�b�P�[�W
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

class LooselyStabilizing_LE_simulator{
	public static final int Gridsize = 50;
	public static final int Roundnum = 1000000;
	
	public static final int DataNum = 100;
	public static final int n = 60;
	
	public static void main(String args[]){
		Random random = new Random();
		Agent agent[] = new Agent[n];
		int CTcounter;
		int CT, HT;
		int SPperRound[] = new int[Roundnum];
		
		for(int Data=0; Data < DataNum; Data++){
			CTcounter = CT = HT = 0;
		
		
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
				while(true){					//�𗬂�����̂�I��
					p = random.nextInt(n-1);		//�����_����agent���Ƃ��Ă���
					q = RandomWay.RandamPickNearAgent( p, n, agent);		//p�Ƌ���1�ȓ��ɂ���m�[�h�̒���(���id�̒Ⴂ)�m�[�h��q�ɑ��
					if(q != -1) { 	//q������������interaction�����Ď��̃��E���h��
						Interaction.interaction(agent[p], agent[q]);	//�𗬂�����
						for(int j=0; j<n; j++) agent[j].Countdown();	//timer���J�E���g����
						break;						//���̃��E���h��
					}
					for(int j=0; j<n; j++){					//�e�̈ړ�
						agent[j].Vchange((random.nextDouble()-0.5)*2 , (random.nextDouble()-0.5)*2 );
						agent[j].ShiftPointForTorus(Gridsize);
					}
				}
				CTcounter++;
				if(CT!=0 && HT!=0 && HT+CT+1 < CTcounter) break;	//HT���I����Ă瑦�I������������
			}
			System.out.println("CT = " + CT);
			System.out.println("HT = " + HT);
			
			//CT�AHT�̃f�[�^���t�@�C���ɏ������ނ��߂̏���
		    try{
		        File fileCT = new File("src\\Data\\CT_s=192_n=60.txt");
		        File fileHT = new File("src\\Data\\HT_s=192_n=60.txt");
		        
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
	
		}	//for(int Data=0; Data < DataNum; Data++) �I���
	}	//���C���I���
}	//�N���X�I���
