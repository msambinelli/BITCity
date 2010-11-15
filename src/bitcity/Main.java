package bitcity;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map mapa = new Map();
		
		System.out.println(mapa.addSemaphore(10, 50));
		System.out.println(mapa.addSemaphore(50, 50));
		System.out.println(mapa.addSemaphore(100, 50));
		System.out.println(mapa.addSemaphore(150, 50));
		System.out.println(mapa.addSemaphore(200, 50));
		
		int count = 0;
		while (true){
			for (int i= 0; i < mapa.sizeSemaphore(); i++){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (count == 0){
					mapa.openSemaphore(i);
				} else {
					mapa.closeSemaphore(i);
				}
				mapa.repaint();
			}
			count = 1 - count; 
		}
	}

}
