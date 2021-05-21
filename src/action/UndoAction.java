package action;

public class UndoAction extends Action {

	@Override
	public String toString() {
		return "UndoAction []";
	}

	@Override
	public int hashCode() {
		return 3;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && getClass() == obj.getClass();
	}
}
