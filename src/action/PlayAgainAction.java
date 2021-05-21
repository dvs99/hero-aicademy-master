package action;

public class PlayAgainAction extends Action {

	@Override
	public String toString() {
		return "PlayAgainAction []";
	}

	@Override
	public int hashCode() {
		return 2;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && getClass() == obj.getClass();
	}
}
