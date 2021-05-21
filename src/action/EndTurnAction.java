package action;

public class EndTurnAction extends Action {

	@Override
	public String toString() {
		return "EndTurnAction []";
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && getClass() == obj.getClass();
	}
}
