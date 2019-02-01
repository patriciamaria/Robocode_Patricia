package ia20182;
import robocode.*;
import java.awt.*;
import robocode.AdvancedRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Patricia - a robot by (your name here)
 */
public class Patricia extends AdvancedRobot
{
	double gunTurnAmt; //Valor para virar nossa arma.
	double turnAngle; //Ângulo do canhão.
	double gunTurnAngle; //Quanto virar a arma ao pesquisar.
	
	/**
	 * run: Comportamento Padrão de Patricia's
	 */
	//O jogo chama o método run quando a batalha começa.
	public void run() {
		
		/** Cores do robo */
		setBodyColor(Color.pink); //cor do corpo.
		setGunColor(Color.black); //cor da arma.
		setRadarColor(Color.yellow); //cor do radar.
		setBulletColor(Color.green); //cor das balas.
		setScanColor(Color.blue); //cor do arco .

		/** Loop principal do robô */
		while (true) {
			setTurnGunRight(360); //Virar o canhão 360º para a direita.
			
			/*Executa todas as ações pendentes ou continua executando ações que estão em andamento. 
			Esta chamada retorna depois que as ações foram iniciadas.
			Observe que os robôs avançados devem chamar essa função para executar chamadas pendentes 
			do grupo *, como, por exemplo, setAhead (double), setFire (double), setTurnLeft (double) etc. 
			Caso contrário, essas chamadas nunca serão executadas.*/
			execute();	
		}
	}

	/**
	 * onScannedRobot: O que fazer quando você vê um outro robô
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//Se o nosso alvo estiver muito longe, vire-se e siga em frente.
		//Se a distância do robo inimigo for maior que 150
		if (e.getDistance() > 150) {
			gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
			setTurnGunRight(gunTurnAmt); 
			setTurnRight(e.getBearing());		
			setAhead(e.getDistance() - 140);
			return;
		}
		
		//É criada a variável gunTurnAmt que recebe o valor do calculo
		//de quanto a mira do canhão deve ser ajustada.
		//Para encontrar o valor adequado de ajuste, é chamada a função
		//para normalizar um ângulo.
		//Assegura que nossa arma esteja apontando para o robô antes de disparar, 
		//E também que a arma esteja fria antes de disparar.
		
		//Nosso alvo está próximo.
		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		setTurnGunRight(gunTurnAmt);
		setFire(3); //Força do tiro, e subtraido da energia do meu robô.
		
		//'normalRelativeAngleDegrees'-Função muito utilizada para normalizar o ângulo de um dos componentes do robô em relação à um referencial.
		//'e.getBearing'-Retorna o ângulo do robô inimigo em questão.
		//'getHeading'-Retorna a direção que o chassi do robo está apontando, em graus.
		//'getRadarHeading'-Retorna o ângulo em graus que o radar está virado.
	
		//Nosso alvo está próximo demais! Cópia de segurança.
		if (e.getDistance() < 100) {
			if (e.getBearing() > -90 && e.getBearing() <= 90) {
				setBack(40); //O robô se moverá 40 pixels para trás
			} else {
				setAhead(40); //O robô se moverá 40 pixels para frente
			}
		}
		// Depois de atirar chama o radar novamente,
		// antes de girar o canhão
		scan();	
	}

	/**
	 * onHitByBullet: O que fazer quando você for atingido por uma munição
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		if(e.getBearing() % 180 == 0){
			turnRight(e.getBearing() + 90);
			setAhead(50); //O robô se moverá 50 pixels para frente
		}
		else if(e.getBearing()>0){
			if(e.getBearing() <= 30 || e.getBearing() >= 150){
				turnAngle = 60;
			}else {
				turnAngle = 0;
			}
			turnRight(turnAngle);
			setAhead(50); //O robô se moverá 50 pixels para frente
			turnGunLeft(5);	
		}
		else {
			if(e.getBearing() >= -30 || e.getBearing() <= -150){
				turnAngle = 60;
			}else {
				turnAngle = 0;
			}
			turnLeft(turnAngle);
			setAhead(50); //O robô se moverá 50 pixels para frente
			turnGunRight(5);	
		}			
		scan();
	}
	
	/**
	 * onHitWall: O que fazer quando você atingir uma parede.
	 */
	public void onHitWall(HitWallEvent e) {
		double x = getX(); //Retorna a posição X do robô. (0,0) é no canto inferior esquerdo.
		double y = getY(); //Retorna a posição Y do robô. (0,0) é no canto inferior esquerdo.
		double lar = getBattleFieldWidth(); //Retorna a largura do campo de batalha.
		double alt = getBattleFieldHeight(); //Retorna a altura do campo de batalha.
		if ((x == lar)||(x == 0)){
			setAhead (100); //O robô se moverá 100 pixels para frente.
		}
		if ((y == alt)||(y == 0)){
			setBack (50); //O robô se moverá 50 pixels para trás.
		}
	}	
	
	/**
	 * onBulletMissed: É executado quando uma de suas balas colide com a parede(erra o tiro).
	 */
	public void onBulletMissed(BulletMissedEvent e) {		
		setBack(40); //O robô se moverá 40 pixels para trás.	
	}
	
	/**
	 * onHitRobot: O que fazer quando você bate em outro robô.
	 */
	public void onHitRobot(HitRobotEvent e) {
		//Normaliza um ângulo para um ângulo relativo. O ângulo normalizado estará no intervalo de -180 a 180, onde 180 em si não está incluído.
		//Assegura que nossa arma esteja apontando para o robô antes de disparar, 
		//E também que a arma esteja fria antes de disparar.
		double gunTurnAngle = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		//'normalRelativeAngleDegrees'-Função muito utilizada para normalizar o ângulo de um dos componentes do robô em relação à um referencial.
		//'e.getBearing'-Retorna o ângulo do robô inimigo em questão.
		//'getHeading'-Retorna a direção que o chassi do robo está apontando, em graus.
		//'getRadarHeading'-Retorna o ângulo em graus que o radar está virado.
		
		setTurnGunRight(gunTurnAngle); //Gira o canhão para a direita na quantidade informada ângulo em graus que o canhão deverá girar
		setFire(3); //Força do tiro, e subtraido da energia do meu robô.
		setBack(50); //O robô se moverá 50 pixels para trás.
	} 
	
	/**
	 * onHitByBullet: O que fazer quando seu tiro atinge um adversário.
	 */
	public void onBulletHit(BulletHitEvent e) {
		setBack(50); //O robô se moverá 50 pixels para trás.
	}
	
	/**
	 * onWin: É executado quando seu robô ganha o round.
	 */
	public void onWin(WinEvent e) {	
		setTurnRight(36000); //Gira o chassi do robô para a direita. 
	}	
}