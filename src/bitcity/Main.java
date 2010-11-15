package bitcity;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map mapa = new Map();
		
	
		
		System.out.println("id: " + mapa.addSemaphore(10, 50));
		System.out.println("id: " + mapa.addSemaphore(50, 50));
		System.out.println("id: " + mapa.addSemaphore(100, 50));
		System.out.println("id: " + mapa.addSemaphore(150, 50));
		System.out.println("id: " + mapa.addSemaphore(10, 100));
		
		mapa.addStreet(300, 300, 150, 150);
		mapa.addStreet(200, 50, 200, 150);
		
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
