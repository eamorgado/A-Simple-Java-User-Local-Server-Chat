public class Wait{ //delay
	Wait(int t){
		try {Thread.sleep(t);
			} catch (InterruptedException e) {e.printStackTrace();}
	}
}
