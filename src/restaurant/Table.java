package restaurant;

public class Table {
	CustomerAgent occupiedBy;
	int tableNumber;
	int guiPosX;
	int guiPosY;

	Table(int tableNumber) {
		this.tableNumber = tableNumber;
		guiPosX = 0;
		guiPosY = 0;
	}
	
	Table(int tableNumber, int posX, int posY) {
		this.tableNumber = tableNumber;
		guiPosX = posX;
		guiPosY = posY;
	}

	void setOccupant(CustomerAgent cust) {
		occupiedBy = cust;
	}
	
	public int getTableNumber(){
		return tableNumber;
	
	}
	public int getPosX(){
		return guiPosX;
	}
	
	public int getPosY(){
		return guiPosY;
	
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	CustomerAgent getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}
